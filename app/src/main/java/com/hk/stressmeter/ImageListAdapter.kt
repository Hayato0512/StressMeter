package com.hk.stressmeter

import android.app.Activity
import android.media.Image
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView

class ImageListAdapter(var langaugeList:List<Images>, var activity: Activity): BaseAdapter() {

    override fun getItem(position: Int): Any {
        return langaugeList.get(position)
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }
    override fun getCount(): Int {
        return langaugeList.size
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
    val view:View  =View.inflate(activity,R.layout.layout_adapter,null)

        val image:ImageView = view.findViewById(R.id.stress_image)
        val lang_pic=langaugeList.get(position).img_icon
        image.setImageResource(lang_pic)
        return view
    }
}