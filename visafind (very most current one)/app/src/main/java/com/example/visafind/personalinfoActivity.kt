package com.example.visafind

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.database.DatabaseReference       //this helped me fetch data from the user that is logged in to the application
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.bumptech.glide.Glide

class personalinfoActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var database: DatabaseReference        // variable database reference
    private lateinit var storage: FirebaseStorage

    private lateinit var textViewName: TextView
    private lateinit var textViewLastName: TextView
    private lateinit var textViewEmail: TextView
    private lateinit var textViewDateOfBirth: TextView
    private lateinit var textViewCountry: TextView
    private lateinit var imageViewProfile: ImageView
    private lateinit var nextButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_personalinfo)

        // Initialize Firebase Auth and Firestore

        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance().reference         // initializing a reference to the firebase realtime database
        storage = FirebaseStorage.getInstance()

        textViewName = findViewById(R.id.textFirstName)
        textViewLastName = findViewById(R.id.textLastName)
        textViewEmail = findViewById(R.id.textEmail)
        imageViewProfile = findViewById(R.id.imageProfilePicture)
        textViewDateOfBirth = findViewById(R.id.textDateOfBirth)
        textViewCountry = findViewById(R.id.textCountry)
        nextButton = findViewById(R.id.button)

        nextButton.setOnClickListener {
            val intent = Intent(this, passportinfoActivity::class.java)
            startActivity(intent)
        }

        // Get the current user
        val currentUser = auth.currentUser
        if (currentUser != null) {
            fetchUserData(currentUser.uid)
        }
    }

    override fun onStart() {
        super.onStart()
        val currentUser = auth.currentUser
        if (currentUser != null) {
            fetchUserData(currentUser.uid)
        }
    }

    private fun fetchUserData(uid: String) {
        Log.d("personalinfoActivity", "Fetching data for user: $uid")
        database.child("users").child(uid).get().addOnSuccessListener { dataSnapshot ->
            if (dataSnapshot.exists()) {
                val firstName = dataSnapshot.child("firstName").getValue(String::class.java) ?: ""
                val lastName = dataSnapshot.child("lastName").getValue(String::class.java) ?: ""
                val email = dataSnapshot.child("email").getValue(String::class.java) ?: ""
                val dateOfBirth = dataSnapshot.child("dateOfBirth").getValue(String::class.java) ?: ""
                val profileImageUrl = dataSnapshot.child("profileImageUrl").getValue(String::class.java) ?: ""
                val currentCountry = dataSnapshot.child("currentCountry").getValue(String::class.java) ?: ""

                textViewName.text = "First Name: $firstName"
                textViewLastName.text = "Last Name: $lastName"
                textViewEmail.text = "Email: $email"
                textViewDateOfBirth.text = "Date of Birth: $dateOfBirth"
                textViewCountry.text = "current country: $currentCountry"


                if (profileImageUrl.isNotEmpty()){
                    Glide.with(this)
                        .load(profileImageUrl)
                        .into(imageViewProfile)
                }

                Log.d("personalinforActivity", "User data fetched successfulyy")

                } else {
                Log.e("personalInfoActivity", "User data not found")

                }
            }

            .addOnFailureListener { exception ->

                Log.e("personalInfoActivity", "Error fetching user data", exception)
            }

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }
}