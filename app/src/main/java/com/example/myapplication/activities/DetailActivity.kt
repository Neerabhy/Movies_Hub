@file:Suppress("DEPRECATION")

package com.example.myapplication.activities

import android.R
import android.annotation.SuppressLint
import android.content.ActivityNotFoundException
import android.content.Intent
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.view.ViewOutlineProvider
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.GranularRoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.example.myapplication.Adapters.CastListAdapter
import com.example.myapplication.Adapters.CategoryEachFilmAdapter
import com.example.myapplication.Domains.Film
import com.example.myapplication.databinding.ActivityDetailBinding
import eightbitlab.com.blurview.RenderScriptBlur

@Suppress("DEPRECATION")
class DetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        binding = ActivityDetailBinding.inflate(layoutInflater)

        setContentView(binding.root)
        setVariable()
        WindowCompat.setDecorFitsSystemWindows(window, false)
    }

    @SuppressLint("SetTextI18n")
    private fun setVariable() {
        val film =
            intent.getSerializableExtra("object") as? Film ?: return // Safe cast and null check

        val requestOptions = RequestOptions().transform(
            CenterCrop(),
            GranularRoundedCorners(0F, 0F, 50F, 50F)
        )
        Glide.with(this)
            .load(film.Poster)
            .apply(requestOptions)
            .into(binding.filmPic)

        binding.titleTxt.text = film.Title
        binding.imdbTxt.text = "IMDB ${film.Imdb}"
        binding.movieTimesTxt.text = "${film.Year} - ${film.Time}"
        binding.movieSummary.text = film.Description

        binding.waatchTrailerBtn.setOnClickListener {
            val id = film.Trailer.replace("https://www.youtube.com/watch?v=", "")
            val appIntent = Intent(Intent.ACTION_VIEW, Uri.parse("vnd.youtube:$id"))
            val webIntent = Intent(Intent.ACTION_VIEW, Uri.parse(film.Trailer))
            try {
                startActivity(appIntent)
            } catch (ex: ActivityNotFoundException) {
                startActivity(webIntent) // Use webIntent in case of exception
            }
        }

        binding.backImg.setOnClickListener { finish() }

        val radius = 10f
        val decorView = window.decorView
        val rootView = decorView.findViewById<ViewGroup>(R.id.content)
        val windowBackground = decorView.background

        binding.blurView.setupWith(rootView, RenderScriptBlur(this))
            .setFrameClearDrawable(windowBackground)
            .setBlurRadius(radius)
        binding.blurView.outlineProvider = ViewOutlineProvider.BACKGROUND
        binding.blurView.clipToOutline = true

        if (film.Genre != null) {
            binding.genreView.adapter = CategoryEachFilmAdapter(film.Genre)
            binding.genreView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        }

        if (film.Casts != null) {
            binding.CastView.adapter = CastListAdapter(film.Casts)
            binding.CastView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        }
    }
}
