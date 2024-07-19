package com.example.myapplication.Adapters

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
//import android.view.Viewport.android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.example.myapplication.Domains.Film
import com.example.myapplication.R
import com.example.myapplication.activities.DetailActivity
import com.example.myapplication.activities.MainActivity

class FilmListAdapter(private var films: ArrayList<Film>, context: Context) :
    RecyclerView.Adapter<FilmListAdapter.ViewHolder>() {

    private lateinit var context: Context



class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    val titleTxt: TextView = itemView.findViewById(R.id.nameTxt)
    val pic: ImageView = itemView.findViewById(R.id.pic)
}override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
    context = parent.context
    val inflate = LayoutInflater.from(parent.context)
        .inflate(R.layout.film_viewholder, parent, false)
    return ViewHolder(inflate)
}


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        Log.d("Image URL", films[position].Poster)
        val film = films[position]
        holder.titleTxt.text = film.Title

        val requestOptions = RequestOptions().transform(CenterCrop(), RoundedCorners(60))

        Glide.with(context)
            .load(film.Poster)
            .apply(requestOptions)
            .into(holder.pic)

        holder.itemView.setOnClickListener {
            val intent = Intent(context, DetailActivity::class.java)
            intent.putExtra("object", film) // Pass the 'film' object directly
            context.startActivity(intent)
        }
    }

override fun getItemCount(): Int {
    return films.size
}
}