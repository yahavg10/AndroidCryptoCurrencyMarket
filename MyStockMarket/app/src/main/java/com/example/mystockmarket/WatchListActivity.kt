package com.example.mystockmarket

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Window
import android.view.WindowManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mystockmarket.databinding.ActivityMainBinding
import com.example.mystockmarket.databinding.ActivitySearchBinding
import com.example.mystockmarket.databinding.ActivityWatchlistBinding
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import java.util.*

class WatchListActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var searchView: SearchView
    private lateinit var binding: ActivityWatchlistBinding
    private var mList = ArrayList<CoinData>()
    private lateinit var adapter: LanguageAdapter
    private lateinit var dbref: DatabaseReference
    var usid = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWatchlistBinding.inflate(layoutInflater)
        setContentView(binding.root)


        usid = intent.getStringExtra("id").toString()
        recyclerView = findViewById(R.id.recyclerView)
        searchView = findViewById(R.id.searchView)

        recyclerView.setHasFixedSize(true)
        recyclerView.layoutManager = LinearLayoutManager(this)
        addDataToList()
        adapter = LanguageAdapter(mList)
        recyclerView.adapter = adapter



        adapter.setOnItemClickListener(object: LanguageAdapter.onItemClickListener{
            override fun onItemClick(position: Int) {
                Toast.makeText(this@WatchListActivity, "you clicked on " + adapter.mList[position].title,Toast.LENGTH_SHORT).show()
                if(usid != null)
                {
                    val sharedPreferences = getSharedPreferences("sharedPrefs", Context.MODE_PRIVATE)
                    val editor = sharedPreferences.edit()
                    editor.apply {
                        putString(usid, adapter.mList[position].title)
                    }.apply()
                    val loginIntent = Intent(this@WatchListActivity, CoinActivity::class.java).also{ it1 ->
                        it1.putExtra("id",usid)
                        startActivity(it1)
                    }
                }
            }

        })

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                filterList(newText)
                return true
            }

        })

        binding.searchNavigation.setOnClickListener{
            val loginIntent = Intent(this, SearchActivity::class.java).also{ it1 ->
                it1.putExtra("id", usid)
                startActivity(it1)
            }
        }
        binding.settingsNavigation.setOnClickListener{
            val loginIntent = Intent(this, SettingsActivity::class.java).also{ it1 ->
                it1.putExtra("id", usid)
                startActivity(it1)
            }
        }
        binding.homeNavigation.setOnClickListener{
            val loginIntent = Intent(this, MainActivity::class.java).also{ it1 ->
                it1.putExtra("id", usid)
                startActivity(it1)
            }
        }
        binding.LoginNavigation.setOnClickListener{
            val loginIntent = Intent(this, LoginActivity::class.java).also{ it1 ->
                it1.putExtra("id", usid)
                startActivity(it1)
            }
        }
    }

    private fun filterList(query: String?) {
        if (query != null) {
            val filteredList = ArrayList<CoinData>()
            for (i in mList) {
                if (i.title.lowercase(Locale.ROOT).contains(query) || i.title.contains(query)) {
                    filteredList.add(i)
                }
            }

            if (filteredList.isEmpty()) {
                Toast.makeText(this, "No Data found", Toast.LENGTH_SHORT).show()
            } else {
                adapter.setFilteredList(filteredList)
            }
        }
    }

    private fun addDataToList() {
        dbref = FirebaseDatabase.getInstance().getReference()
        dbref.child(usid).get().addOnSuccessListener {
            var coins = it.child("coins").value.toString()
            if(coins != "")
            {
                val names = coins.split(",")
                for (name in names) {
                    mList.add(CoinData(name, R.drawable.bbitcoin))
                }
            }
        }
    }
}