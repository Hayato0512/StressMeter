package com.hk.stressmeter

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Build
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.RequiresApi


import androidx.appcompat.app.AppCompatActivity
import com.floern.castingcsv.castingCSV
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.apache.commons.csv.CSVFormat
import org.apache.commons.csv.CSVParser
import java.io.*
import java.nio.charset.Charset
import java.nio.file.Files
import java.nio.file.Files.newBufferedReader
import java.nio.file.Path
import java.nio.file.Paths
import java.time.LocalDateTime

class ConfirmActivity: AppCompatActivity()  {
    private lateinit var submitButton:Button
    private lateinit var cancelButton: Button
    private lateinit var sp: SharedPreferences
    private lateinit var image:ImageView
    private var stressLevel:Int = 0;
    private var imageArray1 :ArrayList<Images> = ArrayList<Images>()
    private var imageArray2 :ArrayList<Images> = ArrayList<Images>()
    private var imageArray3 :ArrayList<Images> = ArrayList<Images>()
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_confirm)



        //data class to store into csv
        data class StressRecord (
            val timeStamp: String,
            val stress: String,
        )
        //initialize shared preference
        sp = getSharedPreferences("MyUserPrefs", Context.MODE_PRIVATE)
        val editor:SharedPreferences.Editor = sp.edit();

        //get the position where user clicked through sharedPreference
       val fetchedPosition = sp.getInt("position",-1);
        image = findViewById(R.id.fetched_image)
        val imageGroupNum:Int = sp.getInt("imageGroupNum",1)

        //set the stresslevel according to the position , and show the image
                if(imageGroupNum==1){
                    addImageData()
                    CoroutineScope(IO).launch{
                        withContext(IO){

                            for(item in imageArray1){
                                if(item.img_id==fetchedPosition){

                                    stressLevel = item.img_stress_level
                                    image.setImageResource(item.img_icon)
                                }
                            }
                        }
                    }
                }
                else if(imageGroupNum==2){
                    addImageData2()
                    CoroutineScope(IO).launch {
                        withContext(IO) {
                            for(item in imageArray2){
                                if(item.img_id==fetchedPosition){

                                    stressLevel = item.img_stress_level
                                    image.setImageResource(item.img_icon)
                                }
                            }
                        }
                    }
                }
                else{
                    addImageData3()
                    CoroutineScope(IO).launch {

                        withContext(IO) {

                            for(item in imageArray3){

                                if(item.img_id==fetchedPosition){

                                    stressLevel = item.img_stress_level
                                    image.setImageResource(item.img_icon)
                                }

                        }
                    }
                    }

                }





        submitButton = findViewById(R.id.submitButton)
        cancelButton = findViewById(R.id.cancelButton)
        submitButton.setOnClickListener(){


            val currentTime = LocalDateTime.now()
            val i = 0

//First, read all of data in the csv into data so that we don't loose any previous data when update
            val path = this.filesDir.absolutePath
            val data:MutableList<StressRecord> = ArrayList()
//            try {

                println("path is like this" + path)

                val realPath:Path = Paths.get("$path/stress.csv")
                val file = File("$path/stress.csv")
                if (file.exists()){
                    println("debug: the file exist.")
                   //if file exist, just read from it, and write it.


                }
                else{
                   //if no file, create empty first, and read and write
                    println("debug: the file doesn't exist.")
                    val file = File("$path/stress.csv")
                    val outputStream = file.outputStream()
                    castingCSV().toCSV(data,outputStream)


                }

            val bufferedReader =  Files.newBufferedReader(realPath)
            val csvParser = CSVParser(bufferedReader, CSVFormat.DEFAULT);

            for (csvRecord in csvParser) {
                val timeStamp = csvRecord.get(0);
                val stress = csvRecord.get(1);
                data.add(StressRecord( timeStamp, stress))
            }
            println("debug: file already created so no problem")



//add the new data into data to store
                    val newTimeStamp:String = currentTime.toString()
                    val newStressLevel = stressLevel.toString()
                    data.add(StressRecord( newTimeStamp, newStressLevel))
                    editor.putString("path", path)
                    editor.commit()



            //write data into the csv

            val outputStream = file.outputStream()
            castingCSV().toCSV(data,outputStream)
            editor.putString("pressedButtonType", "submit")
            editor.commit()
            finishAndRemoveTask();

        }
        //go back to mainactivity when cancel button is clicked
        cancelButton.setOnClickListener(){
            val intent= Intent(this, MainActivity::class.java)
            startActivity(intent);
            finish();
        }

    }

    @Throws(IOException::class)
    fun getFileFromAssets(context: Context, fileName: String): File = File(context.cacheDir, fileName)
        .also {
            if (!it.exists()) {
                it.outputStream().use { cache ->
                    context.assets.open(fileName).use { inputStream ->
                        inputStream.copyTo(cache)
                    }
                }
            }
        }

    //FUNCTIONS TO GET ALL THE IMAGES AND STORE THEM TO SHOW
    private fun addImageData(){
        val img0 = Images();
        img0.img_icon = R.drawable.psm_anxious;
        img0.img_id = 0
        img0.img_stress_level = 6;
        imageArray1.add(img0)
        val img1 = Images();
        img1.img_icon = R.drawable.psm_hiking3;
        img1.img_id = 1
        img1.img_stress_level = 8;
        imageArray1.add(img1)
        val img2 = Images();
        img2.img_id = 2
        img2.img_stress_level = 14;
        img2.img_icon = R.drawable.psm_stressed_person3;
        imageArray1.add(img2)
        val img3 = Images();
        img3.img_id = 3
        img3.img_stress_level = 16;
        img3.img_icon = R.drawable.psm_lonely2;
        imageArray1.add(img3)
        val img4 = Images();
        img4.img_id = 4
        img4.img_stress_level = 5;
        img4.img_icon = R.drawable.psm_dog_sleeping;
        imageArray1.add(img4)
        val img5 = Images();
        img5.img_id = 5
        img5.img_stress_level = 7;
        img5.img_icon = R.drawable.psm_running4;
        imageArray1.add(img5)
        val img6 = Images();
        img6.img_icon = R.drawable.psm_alarm_clock;
        img6.img_id = 6
        img6.img_stress_level = 13;
        imageArray1.add(img6)
        val img7 = Images();
        img7.img_icon = R.drawable.psm_headache;
        img7.img_id = 7
        img7.img_stress_level = 15;
        imageArray1.add(img7)
        val img8 = Images();
        img8.img_icon = R.drawable.psm_baby_sleeping;
        img8.img_id = 8
        img8.img_stress_level = 2;
        imageArray1.add(img8)
        val img9 = Images();
        img9.img_icon = R.drawable.psm_puppy;
        img9.img_id = 9
        img9.img_stress_level = 4;
        imageArray1.add(img9)
        val img10 = Images();
        img10.img_icon = R.drawable.psm_stressed_cat;
        img10.img_id = 10
        img10.img_stress_level = 10;
        imageArray1.add(img10)
        val img11 = Images();
        img11.img_icon = R.drawable.psm_angry_face;
        img11.img_id = 11
        img11.img_stress_level = 12;
        imageArray1.add(img11)
        val img12 = Images();
        img12.img_icon = R.drawable.psm_bar;
        img12.img_id = 12
        img12.img_stress_level = 1;
        imageArray1.add(img12)
        val img13 = Images();
        img13.img_icon = R.drawable.psm_running3;
        img13.img_id = 13
        img13.img_stress_level = 3;
        imageArray1.add(img13)
        val img14 = Images();
        img14.img_icon = R.drawable.psm_neutral_child;
        img14.img_id = 14
        img14.img_stress_level = 9;
        imageArray1.add(img14)
        val img15 = Images();
        img15.img_icon = R.drawable.psm_headache2;
        img15.img_id = 15
        img15.img_stress_level = 11;
        imageArray1.add(img15)


    }

    private fun addImageData2(){
        val img0 = Images();
        img0.img_icon = R.drawable.psm_mountains11;
        img0.img_id = 0
        img0.img_stress_level = 6;
        imageArray2.add(img0)
        val img1 = Images();
        img1.img_icon = R.drawable.psm_wine3;
        img1.img_id = 1
        img1.img_stress_level = 8;
        imageArray2.add(img1)
        val img2 = Images();
        img2.img_id = 2
        img2.img_stress_level = 14;
        img2.img_icon = R.drawable.psm_barbed_wire2;
        imageArray2.add(img2)
        val img3 = Images();
        img3.img_id = 3
        img3.img_stress_level =16;
        img3.img_icon = R.drawable.psm_clutter;
        imageArray2.add(img3)
        val img4 = Images();
        img4.img_id = 4
        img4.img_stress_level = 5;
        img4.img_icon = R.drawable.psm_blue_drop;
        imageArray2.add(img4)
        val img5 = Images();
        img5.img_id = 5
        img5.img_stress_level = 7;
        img5.img_icon = R.drawable.psm_to_do_list;
        imageArray2.add(img5)
        val img6 = Images();
        img6.img_icon = R.drawable.psm_stressed_person7;
        img6.img_id = 6
        img6.img_stress_level = 13;
        imageArray2.add(img6)
        val img7 = Images();
        img7.img_icon = R.drawable.psm_stressed_person6;
        img7.img_id = 7
        img7.img_stress_level = 15;
        imageArray2.add(img7)
        val img8 = Images();
        img8.img_icon = R.drawable.psm_yoga4;
        img8.img_id = 8
        img8.img_stress_level = 2;
        imageArray2.add(img8)
        val img9 = Images();
        img9.img_icon = R.drawable.psm_bird3;
        img9.img_id = 9
        img9.img_stress_level = 4;
        imageArray2.add(img9)
        val img10 = Images();
        img10.img_icon = R.drawable.psm_stressed_person8;
        img10.img_id = 10
        img10.img_stress_level = 10;
        imageArray2.add(img10)
        val img11 = Images();
        img11.img_icon = R.drawable.psm_exam4;
        img11.img_id = 11
        img11.img_stress_level = 12;
        imageArray2.add(img11)
        val img12 = Images();
        img12.img_icon = R.drawable.psm_kettle;
        img12.img_id = 12
        img12.img_stress_level = 1;
        imageArray2.add(img12)
        val img13 = Images();
        img13.img_icon = R.drawable.psm_lawn_chairs3;
        img13.img_id = 13
        img13.img_stress_level = 3;
        imageArray2.add(img13)
        val img14 = Images();
        img14.img_icon = R.drawable.psm_to_do_list3;
        img14.img_id = 14
        img14.img_stress_level = 9;
        imageArray2.add(img14)
        val img15 = Images();
        img15.img_icon = R.drawable.psm_work4;
        img15.img_id = 15
        img15.img_stress_level = 11;
        imageArray2.add(img15)


    }

    private fun addImageData3(){
        val img0 = Images();
        img0.img_icon = R.drawable.psm_talking_on_phone2;
        img0.img_id = 0
        img0.img_stress_level = 6;
        imageArray3.add(img0)
        val img1 = Images();
        img1.img_icon = R.drawable.psm_stressed_person;
        img1.img_id = 1
        img1.img_stress_level = 8;
        imageArray3.add(img1)
        val img2 = Images();
        img2.img_id = 2
        img2.img_stress_level = 14;
        img2.img_icon = R.drawable.psm_stressed_person12;
        imageArray3.add(img2)
        val img3 = Images();
        img3.img_id = 3
        img3.img_stress_level = 16;
        img3.img_icon = R.drawable.psm_lonely;
        imageArray3.add(img3)
        val img4 = Images();
        img4.img_id = 4
        img4.img_stress_level = 5;
        img4.img_icon = R.drawable.psm_gambling4;
        imageArray3.add(img4)
        val img5 = Images();
        img5.img_id = 5
        img5.img_stress_level = 7;
        img5.img_icon = R.drawable.psm_clutter3;
        imageArray3.add(img5)
        val img6 = Images();
        img6.img_icon = R.drawable.psm_reading_in_bed2;
        img6.img_id = 6
        img6.img_stress_level = 13;
        imageArray3.add(img6)
        val img7 = Images();
        img7.img_icon = R.drawable.psm_stressed_person4;
        img7.img_id = 7
        img7.img_stress_level = 15;
        imageArray3.add(img7)
        val img8 = Images();
        img8.img_icon = R.drawable.psm_lake3;
        img8.img_id = 8
        img8.img_stress_level = 2;
        imageArray3.add(img8)
        val img9 = Images();
        img9.img_icon = R.drawable.psm_cat;
        img9.img_id = 9
        img9.img_stress_level = 4;
        imageArray3.add(img9)
        val img10 = Images();
        img10.img_icon = R.drawable.psm_puppy3;
        img10.img_id = 10
        img10.img_stress_level = 10;
        imageArray3.add(img10)
        val img11 = Images();
        img11.img_icon = R.drawable.psm_neutral_person2;
        img11.img_id = 11
        img11.img_stress_level = 12;
        imageArray3.add(img11)
        val img12 = Images();
        img12.img_icon = R.drawable.psm_beach3;
        img12.img_id = 12
        img12.img_stress_level = 1;
        imageArray3.add(img12)
        val img13 = Images();
        img13.img_icon = R.drawable.psm_peaceful_person;
        img13.img_id = 13
        img13.img_stress_level = 3;
        imageArray3.add(img13)
        val img14 = Images();
        img14.img_icon = R.drawable.psm_alarm_clock2;
        img14.img_id = 14
        img14.img_stress_level = 9;
        imageArray3.add(img14)
        val img15 = Images();
        img15.img_icon = R.drawable.psm_sticky_notes2;
        img15.img_id = 15
        img15.img_stress_level = 11;
        imageArray3.add(img15)


    }
}











