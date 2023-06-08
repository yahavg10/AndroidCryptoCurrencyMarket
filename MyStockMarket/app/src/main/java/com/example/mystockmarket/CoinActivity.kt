package com.example.mystockmarket

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.PopupMenu
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Divider
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldColors
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.mystockmarket.databinding.ActivityCoinBinding
import com.example.mystockmarket.databinding.ActivityMainBinding
import com.example.mystockmarket.ui.theme.LineChartComposeTheme

class CoinActivity : ComponentActivity() {
    private lateinit var binding: ActivityCoinBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Inflate the layout
        binding = ActivityCoinBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Get the user ID from the intent
        val usid = intent.getStringExtra("id").toString()

        // Get the coin name from SharedPreferences
        val sharedPreferences = getSharedPreferences("sharedPrefs", Context.MODE_PRIVATE)
        val coinName = sharedPreferences.getString(usid, null).toString()
        var step = "Day"

        // Set the coin name in the text field
        binding.coinnameText.setText(coinName)

        // Get the current price of the coin
        val pairs = getCoinValues(coinName, "Day")
        binding.currpriceText.setText("Current Price Tag: " + pairs[pairs.count()-1].second + "$")

        // Set up the time step selection button
        binding.button.setOnClickListener{
            val popupMenu: PopupMenu = PopupMenu(this,binding.button)
            popupMenu.menuInflater.inflate(R.menu.popup_menu,popupMenu.menu)
            popupMenu.setOnMenuItemClickListener(PopupMenu.OnMenuItemClickListener { item ->
                step = item.title.toString()
                true
            })
            popupMenu.show()
        }

        // Set up the favourite button click listener
        binding.StarRep.setOnClickListener{
            updateCoins(usid, coinName)
        }

        // Set up the graph button click listener
        binding.graph.setOnClickListener{
            val gIntent = Intent(this@CoinActivity, CoinGraphActivity::class.java).also{ it1 ->
                it1.putExtra("id",usid)
                it1.putExtra("step",step)
                startActivity(it1)
            }
        }
        // Set up the search navigation button click listener
        binding.searchNavigation.setOnClickListener{
            val sIntent = Intent(this, SearchActivity::class.java).also{ it1 ->
                it1.putExtra("id",usid)
                startActivity(it1)
            }
        }
        // Set up the settings navigation button click listener
        binding.settingsNavigation.setOnClickListener{
            val loginIntent = Intent(this, SettingsActivity::class.java).also{ it1 ->
                it1.putExtra("id",usid)
                startActivity(it1)
            }
        }
        // Set up the home navigation button click listener
        binding.homeNavigation.setOnClickListener{
            val loginIntent = Intent(this, MainActivity::class.java).also{ it1 ->
                it1.putExtra("id",usid)
                startActivity(it1)
            }
        }
        // Set up the login navigation button click listener
        binding.LoginNavigation.setOnClickListener{
            val loginIntent = Intent(this, LoginActivity::class.java).also{ it1 ->
                it1.putExtra("id",usid)
                startActivity(it1)
            }
        }
        // Set up the watchlist navigation button click listener
        binding.watchlistNavigation.setOnClickListener{
            val loginIntent = Intent(this, WatchListActivity::class.java).also{ it1 ->
                it1.putExtra("id",usid)
                startActivity(it1)
            }
        }
    }
}
