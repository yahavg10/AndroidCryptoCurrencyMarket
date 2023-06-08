package com.example.mystockmarket

import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.mystockmarket.databinding.ActivityForgotBinding
import com.example.mystockmarket.databinding.ActivityLoginBinding
import com.example.mystockmarket.ui.theme.MyStockMarketTheme
import com.google.firebase.auth.FirebaseAuth

class ForgotActivity : ComponentActivity() {

    private lateinit var binding: ActivityForgotBinding
    private lateinit var firebaseAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Inflate the layout using View Binding
        binding = ActivityForgotBinding.inflate(layoutInflater)

        // Get an instance of FirebaseAuth
        firebaseAuth = FirebaseAuth.getInstance()

        // Set the content view to the inflated layout
        setContentView(binding.root)

        // Set a click listener on the loginButton
        binding.loginButton.setOnClickListener{
            val email = binding.loginEmail.text.toString()

            // Send password reset email using FirebaseAuth
            firebaseAuth.sendPasswordResetEmail(email).addOnCompleteListener {
                if (it.isSuccessful) {
                    Toast.makeText(this, "Check your email", Toast.LENGTH_SHORT).show()
                    val logIntent = Intent(this, LoginActivity::class.java)
                    startActivity(logIntent)
                } else {
                    Toast.makeText(this, it.exception.toString(), Toast.LENGTH_SHORT).show()
                }
            }
        }

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
    }
}
