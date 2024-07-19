package com.example.myapplication.Adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.example.myapplication.Domains.SliderItems
import com.example.myapplication.R

class SlidersAdapter(private var sliderItems: List<SliderItems>, private val viewPager2: ViewPager2) :
    RecyclerView.Adapter<SlidersAdapter.SliderViewHolder>() {

    private lateinit var context: Context
    private val runnable = Runnable {
        sliderItems = sliderItems + sliderItems // Efficient way to duplicate the list
        notifyDataSetChanged()}

    class SliderViewHolder(itemView: View,private val context: Context) : RecyclerView.ViewHolder(itemView) {
        private val imageView: ImageView = itemView.findViewById(R.id.imageSlide)
        private val nameTxt: TextView = itemView.findViewById(R.id.nameTxt)
        private val genreTxt: TextView = itemView.findViewById(R.id.genreTxt)
        private val ageTxt: TextView = itemView.findViewById(R.id.ageTxt)
        private val yearTxt: TextView = itemView.findViewById(R.id.yearTxt)
        private val timeTxt: TextView = itemView.findViewById(R.id.timeTxt)

        fun setImage(sliderItem: SliderItems) {
            val requestOptions = RequestOptions().transform(CenterCrop(), RoundedCorners(60))
            Glide.with(context)
                .load(sliderItem.image)
                .apply(requestOptions)
                .into(imageView)
            nameTxt.text = sliderItem.name
            genreTxt.text = sliderItem.genre
            ageTxt.text = sliderItem.age
            yearTxt.text = sliderItem.year
            timeTxt.text =sliderItem.time
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SliderViewHolder {
        context = parent.context
        return SliderViewHolder(
                LayoutInflater.from(context).inflate(R.layout.slider_viewholder, parent, false),
        context // Pass the context here
        )
    }


    override fun onBindViewHolder(holder: SliderViewHolder, position: Int) {
        holder.setImage(sliderItems[position])
        if (position == sliderItems.size - 2) {
            viewPager2.post(runnable)
        }
    }

    override fun getItemCount(): Int {
        return sliderItems.size
    }
}