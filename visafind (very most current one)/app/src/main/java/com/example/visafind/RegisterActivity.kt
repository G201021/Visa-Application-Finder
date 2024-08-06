package com.example.visafind

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.Toast
import android.net.Uri
import android.os.Build.VERSION_CODES.R
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import androidx.activity.result.contract.ActivityResultContracts
import com.example.visafind.databinding.ActivityRegisterBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import java.util.UUID


class RegisterActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    private lateinit var database: FirebaseDatabase
    private lateinit var binding: ActivityRegisterBinding
    private lateinit var firstName: String
    private lateinit var lastName: String
    private lateinit var storage: FirebaseStorage
    private var profileImageUri: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        firstName = ""  // Initialize firstName and lastName variables
        lastName = ""
        setContentView(binding.root) // Set the content view to the root view of the binding variable

        auth = FirebaseAuth.getInstance() // Initialize Firebase Authentication and Firebase Database
        database = FirebaseDatabase.getInstance() // Initialize Firebase Authentication and Firebase Database
        storage = FirebaseStorage.getInstance()

        val pickImage = registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
            uri?.let {
                profileImageUri = it
                binding.profilePicture.setImageURI(it)
            }
        }
        binding.buttonSelectProfilePicture.setOnClickListener {
            pickImage.launch("image/*")
        }

        binding.buttonRegister.setOnClickListener {
            val email = binding.editTextEmail.text.toString()
            val password = binding.editTextPassword.text.toString()
            firstName = binding.textFirstName.text.toString()
            lastName = binding.textLastName.text.toString()

            if (email.isNotEmpty() && password.isNotEmpty()) {
                val dateOfBirth = binding.editTextDateOfBirth.text.toString()
                val currentCountry = binding.editTextCountry.text.toString()
                val hasCriminalRecord = binding.checkBoxCriminalRecord.isChecked
                val languagesSpoken = binding.editTextLanguage.text.toString()
                val hasHealthInsurance = binding.checkBoxHealthInsurance.isChecked

                registerUser(email, password, dateOfBirth, currentCountry, hasCriminalRecord, languagesSpoken, hasHealthInsurance)
            } else {
                Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun registerUser(email: String, password: String, dateOfBirth: String, currentCountry: String, hasCriminalRecord: Boolean, languagesSpoken: String, hasHealthInsurance: Boolean) {
        if (profileImageUri == null){
            Toast.makeText(this, "Please select a profile picture", Toast.LENGTH_SHORT).show()
            return
        }
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    val userId = auth.currentUser?.uid
                    if (userId != null) {
                        if (profileImageUri != null) {
                            uploadProfileImage(userId, profileImageUri!!) { imageUrl ->
                                val userRef = database.reference.child("users").child(userId)
                                val userData = hashMapOf<String, Any>(
                                    "email" to email,
                                    "firstName" to firstName,
                                    "lastName" to lastName,
                                    "dateOfBirth" to dateOfBirth,
                                    "currentCountry" to currentCountry,
                                    "hasCriminalRecord" to hasCriminalRecord,
                                    "languagesSpoken" to languagesSpoken,
                                    "hasHealthInsurance" to hasHealthInsurance,
                                    "profileImageUrl" to imageUrl
                                )
                                userRef.setValue(userData)
                                    .addOnSuccessListener {
                                        Toast.makeText(this, "Account Created", Toast.LENGTH_SHORT).show()
                                        // Redirect to login activity
                                        val intent = Intent(this, LoginActivity::class.java)
                                        startActivity(intent)
                                        finish() // optional, to close the current activity
                                    }
                                    .addOnFailureListener { e ->
                                        Toast.makeText(this, "Failed to register user: ${e.message}", Toast.LENGTH_SHORT).show()
                                    }
                            }
                        } else {
                            Toast.makeText(this, "Profile image not selected", Toast.LENGTH_SHORT).show()
                        }
                    }
                } else {
                    Toast.makeText(this, "Registration failed: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                }
            }
    }


    private fun uploadProfileImage(userId: String, profileImageUri: Uri, callback: (imageUrl: String) -> Unit) { // this function uploads and stores the profile image to firebase storage
        Log.d("RegisterActivity", "Uploading profile image for user: $userId")
        val storageRef = storage.reference.child("profile_images/$userId/${UUID.randomUUID()}.jpg")
        storageRef.putFile(profileImageUri)
            .addOnSuccessListener {
                Log.d("RegisterActivity", "Profile image upload successful")
                storageRef.downloadUrl.addOnSuccessListener { uri ->
                    Log.d("RegisterActivity", "Profile image download URL: $uri")
                    callback(uri.toString())
                }.addOnFailureListener { e ->
                    Log.e("RegisterActivity", "Failed to get download URL", e)
                    Toast.makeText(this, "Failed to get profile image URL: ${e.message}", Toast.LENGTH_SHORT).show()
                }
            }
            .addOnFailureListener { e ->
                Log.e("RegisterActivity", "Failed to upload profile picture", e)
                Toast.makeText(this, "Failed to upload profile picture: ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }
}
// the code below is taken also from the firebase documentation and managed to modify it a bit to work well with the rest of the code.

// the block of code form line 42 calls for the registration authentication for the registration process to work