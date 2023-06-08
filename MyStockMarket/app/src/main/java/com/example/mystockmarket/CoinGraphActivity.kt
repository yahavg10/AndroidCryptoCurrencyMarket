package com.example.mystockmarket

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Divider
import androidx.compose.material3.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.mystockmarket.ui.theme.LineChartComposeTheme

// CoinGraphActivity is a ComponentActivity subclass responsible for displaying a coin's graph.
class CoinGraphActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Retrieve necessary data from the intent
        val usid = intent.getStringExtra("id").toString()
        val timestep = intent.getStringExtra("step").toString()

        // Access shared preferences to get the coin name
        val sharedPreferences = getSharedPreferences("sharedPrefs", Context.MODE_PRIVATE)
        val coinName = sharedPreferences.getString(usid, null)

        // Get the list of date-price pairs for the coin
        val pairs = getCoinValues(coinName.toString(), timestep)

        // Set the content of the activity using Jetpack Compose
        setContent {
            LineChartComposeTheme {
                var data = listOf<Pair<Int, Double>>()
                for ((date, price) in pairs) {
                    data += Pair(date, price)
                }

                // Add some spacing and a divider
                Spacer(modifier = Modifier.height(40.dp))
                Divider()
                Spacer(modifier = Modifier.height(40.dp))

                // Create a column to hold the chart
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.LightGray)
                        .padding(16.dp),
                    verticalArrangement = Arrangement.Center
                ) {
                    // Add some spacing and a divider
                    Spacer(modifier = Modifier.height(40.dp))
                    Divider()
                    Spacer(modifier = Modifier.height(40.dp))

                    // Display the QuadLineChart with the data
                    QuadLineChart(
                        data = data,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(300.dp)
                            .align(CenterHorizontally)
                    )
                }
            }

            // Add some spacing and a divider
            Spacer(modifier = Modifier.height(40.dp))
            Divider()
            Spacer(modifier = Modifier.height(40.dp))

            // Create a button to navigate back to the CoinActivity
            Box(modifier = Modifier.fillMaxWidth().wrapContentHeight(), contentAlignment = Alignment.Center) {
                Button(
                    onClick = {
                        // Handle button click event here
                        val intent = Intent(this@CoinGraphActivity, CoinActivity::class.java).also { it1 ->
                            it1.putExtra("id", usid)
                            startActivity(it1)
                        }
                    }
                ) {
                    Text(text = "Back To Coin")
                }
            }
        }
    }
}
