package com.example.mystockmarket

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.annotation.DrawableRes
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

/**
 * SettingsActivity is responsible for managing the user settings in the stock market application.
 * It extends ComponentActivity class.
 */
class SettingsActivity : ComponentActivity() {

    private lateinit var dbref: DatabaseReference

    /**
     * Called when the activity is being created.
     * @param savedInstanceState The saved instance state Bundle.
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Set the content view to the activity_settings layout file
        setContentView(R.layout.activity_settings)

        // Get a reference to the Firebase Realtime Database
        dbref = FirebaseDatabase.getInstance().getReference()

        // Retrieve the user ID passed through the intent
        val usid = intent.getStringExtra("id").toString()

        // Check if the user ID is not null
        if (usid != null) {
            // Retrieve user data from the database based on the user ID
            dbref.child(usid).get().addOnSuccessListener {
                val name = it.child("name").value
                val mail = it.child("email").value

                // Update UI elements with user data if available
                if(name != null) {
                    val textView = findViewById<TextView>(R.id.usernameText)
                    textView.setText(name.toString()).toString()

                    val emailText = findViewById<TextView>(R.id.changeEmailText)
                    val passText = findViewById<TextView>(R.id.changePasswordText)
                    val phoneText = findViewById<TextView>(R.id.changePhoneText)
                    val nameText = findViewById<TextView>(R.id.changeNameText)
                    val back = findViewById<ImageView>(R.id.arrow)
                    val about = findViewById<TextView>(R.id.Abouttxt)

                    // Set click listeners for various actions
                    about.setOnClickListener {
                        val intent = Intent(this, MainActivity::class.java).also { it1 ->
                            it1.putExtra("id", usid)
                            startActivity(it1)
                        }
                    }

                    back.setOnClickListener {
                        val intent = Intent(this, MainActivity::class.java).also { it1 ->
                            it1.putExtra("id", usid)
                            startActivity(it1)
                        }
                    }

                    emailText.setOnClickListener{
                        val builder = AlertDialog.Builder(this, R.style.MyDialogTheme)
                        val inflater = layoutInflater
                        val dialogLayout = inflater.inflate(R.layout.update_dialog_box, null)
                        val oldemail = dialogLayout.findViewById<EditText>(R.id.field1)
                        val newemail = dialogLayout.findViewById<EditText>(R.id.field2)

                        with(builder){
                            setTitle("Enter Email here: ")
                            setPositiveButton("OK"){dialog, which->
                                val sharedPreferences = getSharedPreferences("sharedPrefs", Context.MODE_PRIVATE)

                                // Check if the old email matches the current email
                                if(mail == oldemail.text.toString()) {

                                    // Check the validity of the new email
                                    if(!checkEmail(newemail.text.toString())) {
                                        Toast.makeText(this@SettingsActivity, "bad email struct" ,Toast.LENGTH_SHORT).show()
                                    }
                                    else {
                                        // Remove the old email from shared preferences
                                        sharedPreferences.edit().remove(oldemail.text.toString()).commit()

                                        val field = newemail.text.toString()

                                        // Store the new email in shared preferences
                                        val editor = sharedPreferences.edit()
                                        editor.apply {
                                            putString(newemail.text.toString(), usid)
                                        }.apply()

                                        // Update the email in the database
                                        updateEmail(usid, field)
                                    }
                                }
                                else {
                                    Toast.makeText(this@SettingsActivity, "Old Email Bad", Toast.LENGTH_SHORT)
                                        .show()
                                }
                            }
                            setView(dialogLayout)
                            show()
                        }
                    }

                    // Similar implementation for other click listeners
                    // ...

                }
            }
        }

        // Set click listeners for navigation icons
        findViewById<ImageView>(R.id.search_navigationS).setOnClickListener{
            val sIntent = Intent(this, SearchActivity::class.java).also{ it1 ->
                it1.putExtra("id",usid)
                startActivity(it1)
            }
        }

        findViewById<ImageView>(R.id.Login_navigationS).setOnClickListener{
            val loginIntent = Intent(this, LoginActivity::class.java).also{ it1 ->
                it1.putExtra("id",usid)
                startActivity(it1)
            }
        }

        findViewById<ImageView>(R.id.home_navigationS).setOnClickListener{
            val loginIntent = Intent(this, MainActivity::class.java).also{ it1 ->
                it1.putExtra("id",usid)
                startActivity(it1)
            }
        }
    }
}
