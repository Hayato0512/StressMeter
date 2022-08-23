package com.hk.stressmeter.ui.home

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.Button
import android.widget.GridView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.floern.castingcsv.castingCSV
import com.hk.stressmeter.ConfirmActivity
import com.hk.stressmeter.ImageListAdapter
import com.hk.stressmeter.Images
import com.hk.stressmeter.R

import com.hk.stressmeter.databinding.FragmentHomeBinding
import com.hk.stressmeter.ui.gallery.GalleryFragment
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File

class HomeFragment : Fragment() {
    private lateinit var tempButton: Button
    private lateinit var clearButton: Button
    private lateinit var sp: SharedPreferences
    private lateinit var imageToPass:Images
    private var _binding: FragmentHomeBinding? = null
    private var imageList: GridView? = null;
    private var imageArray1 :ArrayList<Images> = ArrayList<Images>()
    private var imageArray2 :ArrayList<Images> = ArrayList<Images>()
    private var imageArray3 :ArrayList<Images> = ArrayList<Images>()
    private var adapter: ImageListAdapter? = null;
    private var imageGroupNum:Int = 1;

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val homeViewModel =
            ViewModelProvider(this).get(HomeViewModel::class.java)

        //initiate sharedPreference to communicate with the result fragment
        sp = requireActivity().getSharedPreferences("MyUserPrefs", Context.MODE_PRIVATE)
        val editor: SharedPreferences.Editor = sp.edit()
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        imageList = root.findViewById(R.id.gv_test)

        //fetch images and store to list so that we can show them.
        CoroutineScope(IO).launch{
            withContext(IO){

                addImageData()
                addImageData2()
                addImageData3()
            }
        }
        //imageList adapter process
        adapter = ImageListAdapter(imageArray1, requireActivity())
        imageList?.adapter = adapter;
        tempButton = root.findViewById(R.id.temp_button);
        clearButton = root.findViewById(R.id.clear_button);

        //when user click on an image, start confirm activity
       imageList?.setOnItemClickListener {
           parent:AdapterView<*>, view:View,position:Int, id:Long->

                   editor.putInt("position", position)
                   editor.putInt("imageGroupNum", imageGroupNum)
                   editor.commit()
                   val intent= Intent(requireActivity(), ConfirmActivity::class.java)
                   startActivity(intent);
                       requireActivity().finishActivity(107)
                       System.exit(0)
       }



        //to switch among 3 image lists.
        tempButton.setOnClickListener(){
            println("hey")

            if(imageGroupNum===1){
                adapter = ImageListAdapter(imageArray2, requireActivity())
                imageList?.adapter = adapter;
                imageGroupNum = 2;
            }
            else if(imageGroupNum==2){
                adapter = ImageListAdapter(imageArray3, requireActivity())
                imageList?.adapter = adapter;
                imageGroupNum = 3;

            }
            else{
                adapter = ImageListAdapter(imageArray1, requireActivity())
                imageList?.adapter = adapter;
                imageGroupNum = 1;

            }
        }

        //clear all the data in the csv file when click clear button
        clearButton.setOnClickListener(){
            val path= sp.getString("path",null)
            if(path==null){
               println("debug: sorry, you cannot clear when you don't have data yet")
            }
            else{
                //if the path exist, then just clear the data
                val data:MutableList<GalleryFragment.StressRecord> = ArrayList()
                val file = File("$path/stress.csv")
                val outputStream = file.outputStream()
                castingCSV().toCSV(data,outputStream)
            }
        }

        val textView: TextView = binding.textHome
        homeViewModel.text.observe(viewLifecycleOwner) {
            textView.text = it
        }
        return root
    }

    //FUNCTIONS TO GET ALL THE IMAGES
    private suspend fun addImageData2(){
        val img0 = Images();
            img0.img_icon = R.drawable.psm_mountains11;
            img0.img_id = 0
        imageArray2.add(img0)
        val img1 = Images();
        img1.img_icon = R.drawable.psm_wine3;
        img1.img_id = 1
        imageArray2.add(img1)
        val img2 = Images();
        img2.img_id = 2
        img2.img_icon = R.drawable.psm_barbed_wire2;
        imageArray2.add(img2)
        val img3 = Images();
        img3.img_id = 3
        img3.img_icon = R.drawable.psm_clutter;
        imageArray2.add(img3)
        val img4 = Images();
        img4.img_id = 4
        img4.img_icon = R.drawable.psm_blue_drop;
        imageArray2.add(img4)
        val img5 = Images();
        img5.img_id = 5
        img5.img_icon = R.drawable.psm_to_do_list;
        imageArray2.add(img5)
        val img6 = Images();
        img6.img_icon = R.drawable.psm_stressed_person7;
        img6.img_id = 6
        imageArray2.add(img6)
        val img7 = Images();
        img7.img_icon = R.drawable.psm_stressed_person6;
        img7.img_id = 7
        imageArray2.add(img7)
        val img8 = Images();
        img8.img_icon = R.drawable.psm_yoga4;
        img8.img_id = 8
        imageArray2.add(img8)
        val img9 = Images();
        img9.img_icon = R.drawable.psm_bird3;
        img9.img_id = 9
        imageArray2.add(img9)
        val img10 = Images();
        img10.img_icon = R.drawable.psm_stressed_person8;
        img10.img_id = 10
        imageArray2.add(img10)
        val img11 = Images();
        img11.img_icon = R.drawable.psm_exam4;
        img11.img_id = 11
        imageArray2.add(img11)
        val img12 = Images();
        img12.img_icon = R.drawable.psm_kettle;
        img12.img_id = 12
        imageArray2.add(img12)
        val img13 = Images();
        img13.img_icon = R.drawable.psm_lawn_chairs3;
        img13.img_id = 13
        imageArray2.add(img13)
        val img14 = Images();
        img14.img_icon = R.drawable.psm_to_do_list3;
        img14.img_id = 14
        imageArray2.add(img14)
        val img15 = Images();
        img15.img_icon = R.drawable.psm_work4;
        img15.img_id = 15
        imageArray2.add(img15)


    }

    private suspend fun addImageData3(){
        val img0 = Images();
        img0.img_icon = R.drawable.psm_talking_on_phone2;
        img0.img_id = 0
        imageArray3.add(img0)
        val img1 = Images();
        img1.img_icon = R.drawable.psm_stressed_person;
        img1.img_id = 1
        imageArray3.add(img1)
        val img2 = Images();
        img2.img_id = 2
        img2.img_icon = R.drawable.psm_stressed_person12;
        imageArray3.add(img2)
        val img3 = Images();
        img3.img_id = 3
        img3.img_icon = R.drawable.psm_lonely;
        imageArray3.add(img3)
        val img4 = Images();
        img4.img_id = 4
        img4.img_icon = R.drawable.psm_gambling4;
        imageArray3.add(img4)
        val img5 = Images();
        img5.img_id = 5
        img5.img_icon = R.drawable.psm_clutter3;
        imageArray3.add(img5)
        val img6 = Images();
        img6.img_icon = R.drawable.psm_reading_in_bed2;
        img6.img_id = 6
        imageArray3.add(img6)
        val img7 = Images();
        img7.img_icon = R.drawable.psm_stressed_person4;
        img7.img_id = 7
        imageArray3.add(img7)
        val img8 = Images();
        img8.img_icon = R.drawable.psm_lake3;
        img8.img_id = 8
        imageArray3.add(img8)
        val img9 = Images();
        img9.img_icon = R.drawable.psm_cat;
        img9.img_id = 9
        imageArray3.add(img9)
        val img10 = Images();
        img10.img_icon = R.drawable.psm_puppy3;
        img10.img_id = 10
        imageArray3.add(img10)
        val img11 = Images();
        img11.img_icon = R.drawable.psm_neutral_person2;
        img11.img_id = 11
        imageArray3.add(img11)
        val img12 = Images();
        img12.img_icon = R.drawable.psm_beach3;
        img12.img_id = 12
        imageArray3.add(img12)
        val img13 = Images();
        img13.img_icon = R.drawable.psm_peaceful_person;
        img13.img_id = 13
        imageArray3.add(img13)
        val img14 = Images();
        img14.img_icon = R.drawable.psm_alarm_clock2;
        img14.img_id = 14
        imageArray3.add(img14)
        val img15 = Images();
        img15.img_icon = R.drawable.psm_sticky_notes2;
        img15.img_id = 15
        imageArray3.add(img15)


    }
    private suspend fun addImageData(){
        withContext(IO){
            println("hey this withContext is working")
            val img0 = Images();
            img0.img_icon = R.drawable.psm_anxious;
            img0.img_id = 0
            imageArray1.add(img0)
            val img1 = Images();
            img1.img_icon = R.drawable.psm_hiking3;
            img1.img_id = 1
            imageArray1.add(img1)
            val img2 = Images();
            img2.img_id = 2
            img2.img_icon = R.drawable.psm_stressed_person3;
            imageArray1.add(img2)
            val img3 = Images();
            img3.img_id = 3
            img3.img_icon = R.drawable.psm_lonely2;
            imageArray1.add(img3)
            val img4 = Images();
            img4.img_id = 4
            img4.img_icon = R.drawable.psm_dog_sleeping;
            imageArray1.add(img4)
            val img5 = Images();
            img5.img_id = 5
            img5.img_icon = R.drawable.psm_running4;
            imageArray1.add(img5)
            val img6 = Images();
            img6.img_icon = R.drawable.psm_alarm_clock;
            img6.img_id = 6
            imageArray1.add(img6)
            val img7 = Images();
            img7.img_icon = R.drawable.psm_headache;
            img7.img_id = 7
            imageArray1.add(img7)
            val img8 = Images();
            img8.img_icon = R.drawable.psm_baby_sleeping;
            img8.img_id = 8
            imageArray1.add(img8)
            val img9 = Images();
            img9.img_icon = R.drawable.psm_puppy;
            img9.img_id = 9
            imageArray1.add(img9)
            val img10 = Images();
            img10.img_icon = R.drawable.psm_stressed_cat;
            img10.img_id = 10
            imageArray1.add(img10)
            val img11 = Images();
            img11.img_icon = R.drawable.psm_angry_face;
            img11.img_id = 11
            imageArray1.add(img11)
            val img12 = Images();
            img12.img_icon = R.drawable.psm_bar;
            img12.img_id = 12
            imageArray1.add(img12)
            val img13 = Images();
            img13.img_icon = R.drawable.psm_running3;
            img13.img_id = 13
            imageArray1.add(img13)
            val img14 = Images();
            img14.img_icon = R.drawable.psm_neutral_child;
            img14.img_id = 14
            imageArray1.add(img14)
            val img15 = Images();
            img15.img_icon = R.drawable.psm_headache2;
            img15.img_id = 15
            imageArray1.add(img15)
        }


    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

//
}