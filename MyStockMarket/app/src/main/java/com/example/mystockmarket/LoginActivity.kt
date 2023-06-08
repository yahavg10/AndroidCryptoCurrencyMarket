package com.example.mystockmarket

import android.content.Context
import android.content.Intent
import android.os.Binder
import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.mystockmarket.databinding.ActivityLoginBinding
import com.example.mystockmarket.ui.theme.MyStockMarketTheme
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.snapshots
import com.google.firebase.database.GenericTypeIndicator

class LoginActivity : ComponentActivity() {
    private lateinit var binding: ActivityLoginBinding
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var dbref: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        firebaseAuth = FirebaseAuth.getInstance()
        dbref = FirebaseDatabase.getInstance().getReference()

        // Retrieve the "id" extra from the intent
        val usid = intent.getStringExtra("id").toString()

        // Fetch user data from the database
        dbref.child(usid).get().addOnSuccessListener { snapshot ->
            val name = snapshot.child("name").value

            // If user data is not found, display login form
            if (name == null) {
                // Set up the click listener for the login button
                binding.loginButton.setOnClickListener {
                    val email = binding.loginEmail.text.toString()
                    val password = binding.loginPassword.text.toString()

                    if (email.isNotEmpty() && password.isNotEmpty()) {
                        firebaseAuth.signInWithEmailAndPassword(email, password)
                            .addOnCompleteListener { task ->
                                if (task.isSuccessful) {
                                    val sharedPreferences = getSharedPreferences("sharedPrefs", Context.MODE_PRIVATE)
                                    val usid = sharedPreferences.getString(email, null)

                                    // Start the main activity
                                    val intent = Intent(this, MainActivity::class.java).also { it1 ->
                                        it1.putExtra("id", usid)
                                        startActivity(it1)
                                    }
                                } else {
                                    Toast.makeText(
                                        this,
                                        task.exception.toString(),
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                            }
                    } else {
                        Toast.makeText(this, "One or more fields are empty", Toast.LENGTH_SHORT)
                            .show()
                    }
                }
            } else {
                // If user data is found, display logged-in state
                binding.loginButton.setText("Already Logged in").toString()

                // Set up click listeners for other navigation buttons
                binding.loginButton.setOnClickListener {
                    val intent = Intent(this, MainActivity::class.java).also { it1 ->
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
                binding.searchNavigation.setOnClickListener {
                    val sIntent = Intent(this, SearchActivity::class.java).also { it1 ->
                        it1.putExtra("id", usid)
                        startActivity(it1)
                    }
                }
            }

            // Set up click listeners for other navigation buttons
            binding.RegisterRedirectText.setOnClickListener {
                val intent = Intent(this, RegisterActivity::class.java).also { it1 ->
                    startActivity(it1)
                }
            }
            binding.forgotpassText.setOnClickListener {
                val loginIntent = Intent(this, ForgotActivity::class.java).also { it1 ->
                    startActivity(it1)
                }
            }
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
        }
    }
}

