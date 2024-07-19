package com.example.myapplication.Adapters

import android.view.LayoutInflater
import android.view.View
import androidx.annotation.NonNull
 // Adjust package name if needed

import android.content.Context

import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.myapplication.Domains.Cast
import com.example.myapplication.R

class CastListAdapter(private val casts: ArrayList<Cast>) :
    RecyclerView.Adapter<CastListAdapter.ViewHolder>() {

    private lateinit var context: Context

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val pic: ImageView = itemView.findViewById(R.id.itemimage)
        val nameTxt: TextView = itemView.findViewById(R.id.nameTxt) // Assuming you have a TextView with this ID
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        context = parent.context
        val inflate = LayoutInflater.from(context).inflate(R.layout.viewholder_actors, parent, false)
        return ViewHolder(inflate)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        Glide.with(context)
            .load(casts[position].PicUrl)
            .into(holder.pic)
        holder.nameTxt.text = casts[position].Actor
    }

    override fun getItemCount(): Int {
        return casts.size
    }
}
