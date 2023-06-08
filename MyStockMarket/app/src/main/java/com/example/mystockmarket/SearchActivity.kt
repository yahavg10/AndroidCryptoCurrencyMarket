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
import java.util.*

class SearchActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var searchView: SearchView
    private lateinit var binding: ActivitySearchBinding
    private var mList = ArrayList<CoinData>()
    private lateinit var adapter: LanguageAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySearchBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Get the user ID from the intent
        val usid = intent.getStringExtra("id").toString()

        // Initialize views
        recyclerView = findViewById(R.id.recyclerView)
        searchView = findViewById(R.id.searchView)

        // Set up RecyclerView
        recyclerView.setHasFixedSize(true)
        recyclerView.layoutManager = LinearLayoutManager(this)

        // Add data to the list
        addDataToList()

        // Set up adapter for RecyclerView
        adapter = LanguageAdapter(mList)
        recyclerView.adapter = adapter

        // Set item click listener for the adapter
        adapter.setOnItemClickListener(object : LanguageAdapter.onItemClickListener {
            override fun onItemClick(position: Int) {
                // Handle item click
                Toast.makeText(this@SearchActivity, "You clicked on " + adapter.mList[position].title, Toast.LENGTH_SHORT).show()

                if (usid != null) {
                    // Save selected item to shared preferences
                    val sharedPreferences = getSharedPreferences("sharedPrefs", Context.MODE_PRIVATE)
                    val editor = sharedPreferences.edit()
                    editor.apply {
                        putString(usid, adapter.mList[position].title)
                    }.apply()

                    // Start CoinActivity
                    val loginIntent = Intent(this@SearchActivity, CoinActivity::class.java).also { it1 ->
                        it1.putExtra("id", usid)
                        startActivity(it1)
                    }
                }
            }
        })

        // Set query text listener for the search view
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                // Filter the list based on the search query
                filterList(newText)
                return true
            }
        })

        // Set click listeners for navigation buttons
        binding.settingsNavigation.setOnClickListener {
            val loginIntent = Intent(this, SettingsActivity::class.java).also { it1 ->
                it1.putExtra("id", usid)
                startActivity(it1)
            }
        }

        binding.homeNavigation.setOnClickListener {
            val loginIntent = Intent(this, MainActivity::class.java).also { it1 ->
                it1.putExtra("id", usid)
                startActivity(it1)
            }
        }

        binding.LoginNavigation.setOnClickListener {
            val loginIntent = Intent(this, LoginActivity::class.java).also { it1 ->
                it1.putExtra("id", usid)
                startActivity(it1)
            }
        }

        binding.watchlistNavigation.setOnClickListener {
            val loginIntent = Intent(this, WatchListActivity::class.java).also { it1 ->
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
        // Set up the socket connection
        SocketHandler.setSocket()
        SocketHandler.establishConnection()
        val mSocket = SocketHandler.getSocket()

        // Emit a socket event to get the list
        mSocket.emit("getList")
        var i = 6

        // Listen for the socket event to receive the list
        mSocket.on("getList") { args ->
            if (args[0] != null) {
                val list = args[0] as String

                runOnUiThread {
                    // Update the UI with the received data
                    while (i < 600) {
                        val nlist = list.slice(i..1000)
                        val nlist2 = nlist.substringAfter(",\"").substringBefore('"')
                        i += 6
                        mList.add(CoinData(nlist2, R.drawable.bbitcoin))
                    }
                }
            }
        }
        //SocketHandler.closeConnection()
    }
}
