package com.example.visafind

import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.text.InputFilter
import android.text.InputType
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import java.util.Calendar

// passport information of the user will go here

// will want this information to be stored in the "Documentation" page on the main page

//this page will go back to the previous screen before this page or proceed to the next page, which should be the "overview" page

// this page will store passport number, passport expiration date, and passport issue date

class passportinfoActivity : AppCompatActivity() {

    private lateinit var editTextExpirationDate: Button //date picker
    private lateinit var userexpirationDate: TextView
    private lateinit var editTextPassportIssuedDate: Button
    private lateinit var yourPassportIssueDate: TextView
    private lateinit var nextButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_passportinfo)

        val editPassportNumber: EditText = findViewById(R.id.editPassportNumber) //passport number limit no more than 9
        editPassportNumber.filters = arrayOf(InputFilter.LengthFilter(9))
        editPassportNumber.inputType = InputType.TYPE_CLASS_NUMBER

        editTextExpirationDate = findViewById(R.id.editTextExpirationDate)  // retrieving reference from xml file, element EditText
        userexpirationDate = findViewById(R.id.yourExpDate)
        editTextPassportIssuedDate = findViewById(R.id.editTextPassportIssuedDate)
        yourPassportIssueDate = findViewById(R.id.yourPassIssuDate)
        nextButton = findViewById(R.id.button2)

        //editTextExpirationDate.inputType = InputType.TYPE_NULL //disabling text input but did not seem to work when element in EditText

        editTextExpirationDate.setOnClickListener { // setting a listener so when it is pressed, calls function for date picker
            DatePickerDialog()
        }

        editTextPassportIssuedDate.setOnClickListener{
            DatePickerDialog2()
        }

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets


        }
        nextButton.setOnClickListener {//next button transitions to the next activity

                val intent = Intent(this, overviewpageActivity::class.java)
                startActivity(intent)
        }
    }
    private fun DatePickerDialog (){// button function waiting for interaction, will call android calendar prompt
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        val DatePickerDialog = DatePickerDialog(this,// will help in display the date that the user chose
                { _, selectedYear, selectedMonth, selectedDay ->
                    val selectedDate = "$selectedDay/${selectedMonth + 1}/$selectedYear"
                    userexpirationDate.text = "Selected Date: $selectedDate"// the user date will be displayed in this id
                }, year, month, day)

        DatePickerDialog.show()
    }

    private fun DatePickerDialog2(){// button function waiting for interaction, will call android calendar prompt
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        val DatePickerDialog = DatePickerDialog(this,// will help in display the date that the user chose
            { _, selectedYear, selectedMonth, selectedDay ->
                val selectedDate = "$selectedDay/${selectedMonth + 1}/$selectedYear"
                yourPassportIssueDate.text = "Selected Date: $selectedDate"// the user date will be displayed in this id
            }, year, month, day)

        DatePickerDialog.show()
    }
}