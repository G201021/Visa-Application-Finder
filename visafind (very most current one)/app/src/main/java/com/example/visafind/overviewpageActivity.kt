package com.example.visafind

import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.text.Html
import android.util.Log
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.getValue
import java.io.File.separator

class overviewpageActivity : AppCompatActivity() {
    private lateinit var textAnswers: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_overviewpage)

        textAnswers = findViewById(R.id.textViewAnswers)

        val answers = intent.getSerializableExtra("answers") as? LinkedHashMap<String, String>
        if (answers != null) {
            displayAnswers(answers)
        } else {
            textAnswers.text = "No answers received."
        }
    }

    private fun displayAnswers(answers: LinkedHashMap<String, String>) {
        val stringBuilder = StringBuilder()
        for (entry in answers.entries) {
            val key = entry.key
            val value = entry.value
            stringBuilder.append("<b>$key:</b> $value<br>")
        }
        textAnswers.text = Html.fromHtml(stringBuilder.toString(), Html.FROM_HTML_MODE_LEGACY)
    }
}