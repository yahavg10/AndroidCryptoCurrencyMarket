package com.example.mystockmarket

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import androidx.activity.ComponentActivity
import com.example.mystockmarket.databinding.ActivityRegisterBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class RegisterActivity : ComponentActivity() {
    private lateinit var binding: ActivityRegisterBinding
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var dbref: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Inflate the layout for the activity using view binding
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Get a reference to the Firebase Realtime Database
        dbref = FirebaseDatabase.getInstance().getReference()

        // Get an instance of FirebaseAuth for user authentication
        firebaseAuth = FirebaseAuth.getInstance()

        // Set click listeners for navigation buttons
        binding.searchNavigation.setOnClickListener{
            val sIntent = Intent(this, SearchActivity::class.java).also{ it1 ->
                startActivity(it1)
            }
        }
        binding.settingsNavigation.setOnClickListener{
            val loginIntent = Intent(this, SettingsActivity::class.java).also{ it1 ->
                startActivity(it1)
            }
        }
        binding.homeNavigation.setOnClickListener{
            val loginIntent = Intent(this, MainActivity::class.java).also{ it1 ->
                startActivity(it1)
            }
        }
        binding.LoginNavigation.setOnClickListener{
            val loginIntent = Intent(this, LoginActivity::class.java).also{ it1 ->
                startActivity(it1)
            }
        }

        // Set click listener for the login redirect text
        binding.LoginRedirectText.setOnClickListener{
            val loginIntent = Intent(this, LoginActivity::class.java)
            startActivity(loginIntent)
        }

        // Set click listener for the register button
        binding.registerButton.setOnClickListener{
            val email = binding.registerEmail.text.toString()
            val password = binding.registerPassword.text.toString()
            val name =  binding.registername.text.toString()
            val phonenum =  binding.registerPhone.text.toString()
            val confirm = binding.registerConfirm.text.toString()

            // Validate the input fields
            if(ValidateFields(email,password,phonenum,confirm,name))
            {
                // Check the email structure
                if(!checkEmail(email))
                {
                    Toast.makeText(this, "bad email struct" ,Toast.LENGTH_SHORT).show()
                }
                else if(MatchPass(password,confirm))
                {
                    // Create a new user in Firebase Authentication
                    firebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener{
                        if(it.isSuccessful) {
                            // Generate a unique ID for the user in the Firebase Realtime Database
                            val usid = dbref.push().key!!
                            // Create a User object with the provided information and save it to the database
                            dbref.child(usid).setValue(User(usid, name, email, phonenum, password,""))
                                .addOnCompleteListener {
                                    Toast.makeText(this, "Added Successfully", Toast.LENGTH_SHORT)
                                        .show()

                                    // Store the user ID in shared preferences for future use
                                    val sharedPreferences = getSharedPreferences("sharedPrefs", Context.MODE_PRIVATE)
                                    val editor = sharedPreferences.edit()
                                    editor.apply {
                                        putString(email, usid)
                                    }.apply()

                                    // Retrieve the user ID from shared preferences and start the main activity
                                    val usid = sharedPreferences.getString(email, null)
                                    val mainintent = Intent(this, MainActivity::class.java).also { it1 ->
                                        it1.putExtra("id", usid)
                                        startActivity(it1)
                                    }
                                }
                        }
                        else{
                            Toast.makeText(this, it.exception.toString(),Toast.LENGTH_SHORT).show()
                        }
                    }
                }else{
                    Toast.makeText(this, "password does not match" ,Toast.LENGTH_SHORT).show()
                }
            }else{
                Toast.makeText(this, "one or more fields are empty" ,Toast.LENGTH_SHORT).show()
            }
        }
    }
}
