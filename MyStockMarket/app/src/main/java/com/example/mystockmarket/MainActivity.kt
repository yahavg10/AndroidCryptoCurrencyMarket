package com.example.mystockmarket

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.JsonReader
import android.util.Log
import android.widget.TextView
import androidx.activity.ComponentActivity
import com.example.mystockmarket.databinding.ActivityMainBinding
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

@Suppress("DEPRECATION")
class MainActivity : ComponentActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var dbref: DatabaseReference
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        dbref = FirebaseDatabase.getInstance().getReference()
        val usid = intent.getStringExtra("id").toString()
        if (usid != null) {
            dbref.child(usid).get().addOnSuccessListener {
                val name = it.child("name").value
                if(name !=null)
                {
                    val textView = findViewById<TextView>(R.id.namewelcome)
                    textView.setText(name.toString()).toString()
                    binding.iconpic.setBackgroundResource(R.drawable.avatar2)
                    binding.r1.setOnClickListener{
                        val sIntent = Intent(this, SearchActivity::class.java).also{ it1 ->
                            it1.putExtra("id",usid)
                            startActivity(it1)
                        }
                    }
                    binding.search.setOnClickListener{
                        val sIntent = Intent(this, SearchActivity::class.java).also{ it1 ->
                            it1.putExtra("id",usid)
                            startActivity(it1)
                        }
                    }
                    binding.searchNavigation.setOnClickListener{
                        val sIntent = Intent(this, SearchActivity::class.java).also{ it1 ->
                            it1.putExtra("id",usid)
                            startActivity(it1)
                        }
                    }
                    binding.watchlistNavigation.setOnClickListener{
                        val loginIntent = Intent(this, WatchListActivity::class.java).also{ it1 ->
                            it1.putExtra("id", usid)
                            startActivity(it1)
                        }
                    }
                    val pairs = getCoinValues("BTC", "Year")
                    binding.bittxt.setText("Bitcoin: " + pairs[pairs.count()-1].second + "$")

                    val pairs2 = getCoinValues("DOGE", "Year")
                    binding.dodgetxt.setText("Dodge: " + pairs2[pairs2.count()-1].second + "$")

                    val pairs3 = getCoinValues("EUR", "Year")
                    binding.ethtxt.setText("Euro: " + pairs3[pairs3.count()-1].second + "$")

                    binding.bitbtn.setOnClickListener{
                        val sharedPreferences = getSharedPreferences("sharedPrefs", Context.MODE_PRIVATE)
                        val editor = sharedPreferences.edit()
                        editor.apply {
                            putString(usid, "BTC")
                        }.apply()
                        val loginIntent = Intent(this@MainActivity, CoinActivity::class.java).also{ it1 ->
                            it1.putExtra("id",usid)
                            startActivity(it1)
                        }
                    }
                    binding.eurobtn.setOnClickListener{
                        val sharedPreferences = getSharedPreferences("sharedPrefs", Context.MODE_PRIVATE)
                        val editor = sharedPreferences.edit()
                        editor.apply {
                            putString(usid, "EUR")
                        }.apply()
                        val loginIntent = Intent(this@MainActivity, CoinActivity::class.java).also{ it1 ->
                            it1.putExtra("id",usid)
                            startActivity(it1)
                        }
                    }
                    binding.dodgebtn.setOnClickListener{
                        val sharedPreferences = getSharedPreferences("sharedPrefs", Context.MODE_PRIVATE)
                        val editor = sharedPreferences.edit()
                        editor.apply {
                            putString(usid, "DOGE")
                        }.apply()
                        val loginIntent = Intent(this@MainActivity, CoinActivity::class.java).also{ it1 ->
                            it1.putExtra("id",usid)
                            startActivity(it1)
                        }
                    }
                    binding.LoginNavigation.setOnClickListener{
                        val loginIntent = Intent(this, LoginActivity::class.java).also{ it1 ->
                            it1.putExtra("id","")
                            startActivity(it1)
                        }
                    }
                }
            }
        }


        binding.iconpic.setOnClickListener{
            val loginIntent = Intent(this, LoginActivity::class.java).also{ it1 ->
                it1.putExtra("id",usid)
                startActivity(it1)
            }
        }

        binding.settingsNavigation.setOnClickListener{
            val loginIntent = Intent(this, SettingsActivity::class.java).also{ it1 ->
                it1.putExtra("id",usid)
                startActivity(it1)
            }
        }
    }
}
