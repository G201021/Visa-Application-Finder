package com.example.visafind

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.visafind.databinding.ActivityLoginBinding
import com.google.firebase.auth.FirebaseAuth  // declaration of firebase functions into the code


class LoginActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth  // late iniziation for variable authorizatoin
    private lateinit var binding: ActivityLoginBinding //

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState) // block of code generated automatically when new empty activity is created
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()       // firebase instance

        binding.buttonLogin.setOnClickListener {     ////////////////////////////
            val email = binding.editTextEmail.text.toString()
            val password = binding.editTextPassword.text.toString()

            if (email.isNotEmpty() && password.isNotEmpty()) { // if text boxes are empty for email  and password it will send toast to the user
                loginUser(email, password)
            } else {
                Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show()
            }
        } ////////////////////////// when the login button is clicked, it registers the email and password of the user.
        // if both text boxes are filled, it calls for the loginUser function



        binding.textViewRegister.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
        }
    }



    //code block copied and pasted from the firebase documentation
    private fun loginUser(email: String, password: String) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    startActivity(Intent(this, MainActivity::class.java))
                    finish()
                } else {
                    Toast.makeText(this, "Authentication failed", Toast.LENGTH_SHORT).show()
                }
            }
    }
}


/*
auth.signInWithEmailAndPassword(email, password)
    .addOnCompleteListener(this) { task ->
        if (task.isSuccessful) {
            Log.d(TAG, "signInWithEmail:success")
            val user = auth.currentUser
            updateUI(user)
        } else {
            Log.w(TAG, "signInWithEmail:failure", task.exception)
            Toast.makeText(
                baseContext,
                "Authentication failed.",
                Toast.LENGTH_SHORT,
            ).show()
 */