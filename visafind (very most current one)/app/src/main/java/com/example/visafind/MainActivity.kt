package com.example.visafind

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.drawable.AnimationDrawable
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.bumptech.glide.Glide
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage

class MainActivity : AppCompatActivity() {

    private lateinit var drawerLayout: DrawerLayout
    private lateinit var actionBarDrawerToggle: ActionBarDrawerToggle
    private lateinit var auth: FirebaseAuth
    private lateinit var database: DatabaseReference
    private lateinit var storage: FirebaseStorage
    private lateinit var profileImageView: ImageView
    private lateinit var usernameTextView: TextView

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        FirebaseDatabase.getInstance().setPersistenceEnabled(true)

        auth = Firebase.auth
        database = FirebaseDatabase.getInstance().reference
        storage = FirebaseStorage.getInstance()

        profileImageView = findViewById(R.id.profileUserImage)
        usernameTextView = findViewById(R.id.userNameView)

        fetchUserData()

        setupUI()

        val buttonCountry = findViewById<Button>(R.id.buttonCountrySelect)
        buttonCountry.setOnClickListener {
            val intent = Intent(this, countryActivity::class.java)
            startActivity(intent)
        }

        val button = findViewById<Button>(R.id.button6)
        button.setOnClickListener {
            val intent = Intent(this, InfoActivity::class.java)
            startActivity(intent)
        }
    }

    private fun fetchUserData() {
        val currentUser = auth.currentUser
        if (currentUser != null) {
            val userId = currentUser.uid
            database.child("users").child(userId).get().addOnSuccessListener { dataSnapshot ->
                if (dataSnapshot.exists()) {
                    val firstName = dataSnapshot.child("firstName").getValue(String::class.java) ?: ""
                    val profileImageUrl = dataSnapshot.child("profileImageUrl").getValue(String::class.java) ?: ""

                    Log.d("MainActivity", "Fetched first name: $firstName")
                    Log.d("MainActivity", "Fetched profile image URL: $profileImageUrl")

                    usernameTextView.text = "Name: $firstName"

                    if (profileImageUrl.isNotEmpty()) {
                        Glide.with(this)
                            .load(profileImageUrl)
                            .placeholder(R.drawable.ic_profile_placeholder)
                            .into(profileImageView)
                    } else {
                        profileImageView.setImageResource(R.drawable.ic_profile_placeholder)
                    }
                } else {
                    Log.e("MainActivity", "User data not found")
                }
            }.addOnFailureListener { exception ->
                Log.e("MainActivity", "Error fetching user data", exception)
            }
        } else {
            usernameTextView.text = "Guest"
            profileImageView.setImageResource(R.drawable.ic_profile_placeholder)
        }
    }

    private fun setupUI() {
        drawerLayout = findViewById(R.id.drawerLayout)
        actionBarDrawerToggle = ActionBarDrawerToggle(this, drawerLayout, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        drawerLayout.addDrawerListener(actionBarDrawerToggle)

        val toolbar: Toolbar = findViewById(R.id.toolbar)
        toolbar.setBackgroundResource(R.drawable.toolbar_animation)

        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeButtonEnabled(true)
        supportActionBar?.setDisplayShowTitleEnabled(false)

        val constraintLayout = findViewById<DrawerLayout>(R.id.drawerLayout)    //background gradient animation (unused)
        val animationDrawable = constraintLayout.background as AnimationDrawable
        animationDrawable.setEnterFadeDuration(2500)
        animationDrawable.setExitFadeDuration(5000)
        animationDrawable.start()

        val animationDrawableTwo = toolbar.background as AnimationDrawable //toolbar gradient animation
        animationDrawableTwo.setEnterFadeDuration(2500)
        animationDrawableTwo.setExitFadeDuration(5000)
        animationDrawableTwo.start()

        val navigationView = findViewById<NavigationView>(R.id.navigationView)
        navigationView.setNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.nav_home -> {
                    // Handle "Back to login"
                    true
                }
                R.id.nav_getvisa -> {
                    // Handle "log-out"
                    true
                }
                R.id.nav_bus -> {
                    // Handle "Application status"
                    true
                }
                R.id.nav_plane -> {
                    // Handle "Settings"
                    true
                }
                R.id.nav_login -> {
                    // Handle "Account"
                    true
                }
                R.id.nav_profile -> {
                    // Handle "Renew passport"
                    true
                }
                R.id.nav_logout -> {
                    logOutUser()
                    true
                }
                R.id.nav_aboutUs -> {
                    // Handle "About Us"
                    var url = "https://www.multinationals.co/blog"
                    var intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                    startActivity(intent)
                    true
                }
                R.id.nav_contact -> {
                    // Handle "Contact"
                    var url = "https://www.multinationals.co/blog"
                    var intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                    startActivity(intent)
                    true
                }
                else -> false
            }
            drawerLayout.closeDrawers()
            true
        }
    }


    private fun logOutUser(){
        auth.signOut()
        val intent = Intent(this, LoginActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
        startActivity(intent)
        finish()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean { // Handle toolbar item clicks
        return when (item.itemId) {
            android.R.id.home -> {
                drawerLayout.openDrawer(GravityCompat.START)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onPostCreate(savedInstanceState: Bundle?) {
        super.onPostCreate(savedInstanceState)
        actionBarDrawerToggle.syncState()
    }

    override fun onConfigurationChanged(newConfig: android.content.res.Configuration) {
        super.onConfigurationChanged(newConfig)
        actionBarDrawerToggle.onConfigurationChanged(newConfig)
    }
}