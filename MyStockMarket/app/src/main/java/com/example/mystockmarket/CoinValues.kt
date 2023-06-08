package com.example.mystockmarket

import android.util.JsonWriter
import android.util.Log
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.TimeZone
import com.google.gson.Gson
import com.google.gson.JsonArray
import com.google.gson.JsonElement
import java.util.concurrent.CountDownLatch

data class DataPoint(val date: String, val price: Double)

/**
 * Retrieves the values of a specific coin over a specified time period from my js server.
 * @param nameCoin The name of the coin.
 * @param timeC The time period to retrieve the values for (e.g., "Day", "Month", "Year").
 * @return A list of pairs representing the time (integer value) and corresponding price (double value) of the coin.
 */
fun getCoinValues(nameCoin: String, timeC: String) : List<Pair<Int, Double>> {
    var pairs = mutableListOf<Pair<Int, Double>>()

    // Set up the socket connection
    SocketHandler.setSocket()
    SocketHandler.establishConnection()
    val mSocket = SocketHandler.getSocket()
    val latch = CountDownLatch(1) // Countdown latch to wait for the event listener
    var i =3
    // Emit the "getCoin" event with the coin name and time period
    mSocket.emit("getCoin", nameCoin, timeC)

    // Event listener to handle the response from the server
    mSocket.on("getCoin") { args ->
        if (args[0] != null) {
            val list = args[0] as String
            val gson = Gson()

            // Convert the JSON string to an array of DataPoint objects
            val dataPoints = gson.fromJson(list, Array<DataPoint>::class.java)

            // Process each data point
            for (dataPoint in dataPoints) {
                val dateDirty = dataPoint.date
                val price = dataPoint.price

                // Parse the date string into a Date object
                val dateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
                dateFormat.timeZone = TimeZone.getTimeZone("UTC")
                val date = dateFormat.parse(dateDirty)

                // Convert the Date object to a Calendar object
                val calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"))
                calendar.time = date

                // Extract the necessary time components
                val mon = calendar.get(Calendar.MONTH) + 1
                val day = calendar.get(Calendar.DAY_OF_MONTH)
                val hours = calendar.get(Calendar.HOUR_OF_DAY)
                val minutes = calendar.get(Calendar.MINUTE)
                val seconds = calendar.get(Calendar.SECOND)

                // Determine the time period and add the corresponding pair to the list
                if (timeC == "Month") {
                    pairs += Pair(day, price)
                    pairs += Pair(day, price)
                }
                if (timeC == "Day") {
                    pairs += Pair(hours, price)
                }
                if (timeC == "Year" && i % 3 == 0) {
                    pairs += Pair(mon, price)
                    pairs += Pair(mon, price)
                }
                i += 1
            }
        }
        latch.countDown() // Notify that the event listener has completed
    }

    latch.await() // Wait for the event listener to complete before proceeding

    return pairs
}
