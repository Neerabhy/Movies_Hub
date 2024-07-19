package com.example.myapplication.activities

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.CompositePageTransformer
import androidx.viewpager2.widget.MarginPageTransformer
import androidx.viewpager2.widget.ViewPager2
import com.example.myapplication.Adapters.FilmListAdapter
import com.example.myapplication.Adapters.SlidersAdapter
import com.example.myapplication.Domains.Film
import com.example.myapplication.Domains.SliderItems
import com.example.myapplication.R
import com.example.myapplication.databinding.ActivityMainBinding
import com.google.firebase.FirebaseApp
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlin.math.abs


class MainActivity : AppCompatActivity() {
    private lateinit var database: FirebaseDatabase
    lateinit var binding: ActivityMainBinding
    val sliderHandler = Handler(Looper.getMainLooper())

    val sliderRunnable = object : Runnable {
        override fun run() {
            val adapter = binding.viewPager2.adapter
            if (adapter != null) {
                binding.viewPager2.currentItem =
                    (binding.viewPager2.currentItem + 1) % adapter.itemCount
                supportActionBar?.hide()
                sliderHandler.postDelayed(this, 2000) // Re-schedule
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        FirebaseApp.initializeApp(this)
        binding = ActivityMainBinding.inflate(layoutInflater)
        enableEdgeToEdge()
        supportActionBar?.hide()
        setContentView(binding.root)
        database = FirebaseDatabase.getInstance()



        WindowCompat.setDecorFitsSystemWindows(window, false)
        initBanner()
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        initBanner();
        initTopMoving();
        initUpcoming();
    }
        private fun initUpcoming() {
            val myRef  : DatabaseReference= database.getReference("Upcomming")
            binding.progressBarUpcomingMovies.visibility = View.VISIBLE
            val items : ArrayList<Film> = ArrayList()
            myRef.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {
                        for (sliderSnapshot in snapshot.children) {
                            val film = sliderSnapshot.getValue(Film::class.java)
                            film?.let { items.add(it) } // Add film if it's not null
                        }
                        if (items.isNotEmpty()) {
                            binding.recyclerViewUpcoming.layoutManager = LinearLayoutManager(
                                this@MainActivity, // Use 'this@MainActivity' for context
                                LinearLayoutManager.HORIZONTAL,
                                false
                            )
                            binding.recyclerViewUpcoming.adapter =
                                FilmListAdapter(items, this@MainActivity)
                        }
                        binding.progressBarUpcomingMovies.visibility = View.GONE
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    binding.progressBarTop.visibility = View.GONE // Use the correct progress bar
                }
            })
        }

        private fun initTopMoving() {
            val myRef : DatabaseReference = database.getReference("Items")
            binding.progressBarTop.visibility = View.VISIBLE
            val items : ArrayList<Film> = ArrayList()
            myRef.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {
                        for (sliderSnapshot in snapshot.children) {
                            val film = sliderSnapshot.getValue(Film::class.java)
                            items.add(film!!) // Add film if it's not null
                        }
                        if (items.isNotEmpty()) {
                            binding.recyclerViewTopMovies.layoutManager = LinearLayoutManager(
                                this@MainActivity, // Use 'this@MainActivity' for context
                                LinearLayoutManager.HORIZONTAL,
                                false
                            )
                            binding.recyclerViewTopMovies.adapter =
                                FilmListAdapter(items, this@MainActivity)
                        }

                        binding.progressBarTop.visibility = View.GONE
                    }
                }
                override fun onCancelled(error: DatabaseError) {
                    binding.progressBarTop.visibility = View.GONE // Use the correct progress bar
                }
            })
        }
        private fun initBanner() {
            val myRef: DatabaseReference = database.getReference("Banners")
            binding.progressBarBanner.visibility = View.VISIBLE
            val items: ArrayList<SliderItems> = ArrayList()
            myRef.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {
                        for (sliderSnapshot in snapshot.children) {
                            val sliderItems = sliderSnapshot.getValue(SliderItems::class.java)
                            items.add(sliderItems!!)
                        }
                        banners(items)
                        binding.progressBarBanner.visibility = View.GONE
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    binding.progressBarBanner.visibility = View.GONE
                }
            })
        }

        private fun banners(sliderItems: ArrayList<SliderItems>) {
            binding.viewPager2.adapter =
                SlidersAdapter(sliderItems, binding.viewPager2) // Add 'this' for context
            binding.viewPager2.clipToPadding = false
            binding.viewPager2.clipChildren = false
            binding.viewPager2.setOffscreenPageLimit(3)
            binding.viewPager2.getChildAt(0).setOverScrollMode(RecyclerView.OVER_SCROLL_NEVER)

            val compositePageTransformer = CompositePageTransformer()
            compositePageTransformer.addTransformer(MarginPageTransformer(40))
            compositePageTransformer.addTransformer { page, position ->
                val r = 1 - abs(position)
                page.scaleY = 0.85f + r * 0.15f
            }

            binding.viewPager2.setPageTransformer(compositePageTransformer)
            binding.viewPager2.currentItem = 1
            binding.viewPager2.registerOnPageChangeCallback(object :
                ViewPager2.OnPageChangeCallback() {
                override fun onPageSelected(position: Int) {
                    super.onPageSelected(position)
                    sliderHandler.removeCallbacks(sliderRunnable)
                }
            })

        }

        override fun onPause() {
            super.onPause()
            sliderHandler.removeCallbacks(sliderRunnable)
        }

        override fun onResume() {
            super.onResume()
            sliderHandler.postDelayed(sliderRunnable, 2000)
        }
    }



