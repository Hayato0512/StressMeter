package com.hk.stressmeter.ui.gallery

import android.content.Context
import android.content.SharedPreferences
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TableLayout
import android.widget.TableRow
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.data.*
import com.hk.stressmeter.R
import com.hk.stressmeter.databinding.FragmentGalleryBinding
import org.apache.commons.csv.CSVFormat
import org.apache.commons.csv.CSVParser
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
import java.util.*


class GalleryFragment : Fragment() {
    private lateinit var sp: SharedPreferences
    lateinit var lineChart:LineChart
    lateinit var timeStampArray:MutableList<String>
    lateinit var stressLevelArray: MutableList<String>
    private var _binding: FragmentGalleryBinding? = null

    private val binding get() = _binding!!
    data class StressRecord (
        val timeStamp: String,
        val stress: String,
    )

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val galleryViewModel =
            ViewModelProvider(this).get(GalleryViewModel::class.java)

        //to store timeStamps and StressLevel to use for the chart
         timeStampArray= ArrayList()
        stressLevelArray = ArrayList()
        _binding = FragmentGalleryBinding.inflate(inflater, container, false)
        val root: View = binding.root

//use sharedpreference to get the path to the csv file that we are writing
        sp = requireActivity().getSharedPreferences("MyUserPrefs", Context.MODE_PRIVATE)
        val editor: SharedPreferences.Editor = sp.edit()
    val path = sp.getString("path","")
        val realPath: Path = Paths.get("$path/stress.csv")
        val bufferedReader =  Files.newBufferedReader(realPath)
        val csvParser = CSVParser(bufferedReader, CSVFormat.DEFAULT);


        //first, read csv to fetch previous data and store that into array
        for (csvRecord in csvParser) {
            val timeStamp = csvRecord.get(0);
            val stress = csvRecord.get(1);
            if(!timeStamp.equals("timeStamp")){
                timeStampArray.add(timeStamp)
                stressLevelArray.add(stress)
            }
        }

        return root
    }

    //when the view is created, this will be called
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //initiate barChart
        val barChart:BarChart = requireActivity().findViewById(R.id.BarChart);

        val visitors:ArrayList<BarEntry> =  ArrayList<BarEntry>();
       println("debug: size of timeStampArray"+ timeStampArray.size)
        for(i in 0.. timeStampArray.size-1){
            println("debug: "+ i)
            visitors.add(BarEntry(i.toFloat(),stressLevelArray[i].toFloat()))
        }

        //set data set with the arrays
        val barDataSet: BarDataSet = BarDataSet(visitors, "Stress Level")
        barDataSet.setValueTextColor(Color.BLACK)
        barDataSet.setValueTextSize(16f);

        val barData: BarData = BarData(barDataSet)

        barChart.setFitBars(true)
        barChart.setData(barData)
        barChart.getDescription().setText("Stress Meter")
        barChart.animateY(2000)


        //initiate the table
        initTable()
    }

    //for the tableLayout to display time and stress in a table
    fun initTable(){
        val stk = requireActivity().findViewById(com.hk.stressmeter.R.id.table_main) as TableLayout
        val tbrow0 = TableRow(requireActivity())
        val tv0 = TextView(requireActivity())
        tv0.text = " Time"
        tv0.setTextColor(Color.BLACK)
        tbrow0.addView(tv0)
        val tv1 = TextView(requireActivity())
        tv1.text = " stress level"
        tv1.setTextColor(Color.BLACK)
        tbrow0.addView(tv1)
        stk.addView(tbrow0)


        for(i in 0..timeStampArray.size-1){
            val tbrow = TableRow(requireActivity())
            val t1v = TextView(requireActivity())
            t1v.text = timeStampArray[i]
            t1v.setTextColor(Color.BLACK)
            t1v.gravity = Gravity.CENTER
            tbrow.addView(t1v)
            val t2v = TextView(requireActivity())
            t2v.text = stressLevelArray[i]
            t2v.setTextColor(Color.BLACK)
            t2v.gravity = Gravity.CENTER
            tbrow.addView(t2v)
            stk.addView(tbrow)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}