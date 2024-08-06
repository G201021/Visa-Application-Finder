package com.example.visafind

import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import java.io.Serializable
import java.util.Calendar

class formsActivity : AppCompatActivity() {

    private lateinit var database: DatabaseReference


    private lateinit var radioGroup1: RadioGroup
    private lateinit var radioGroup2: RadioGroup
    private lateinit var radioGroup3: RadioGroup
    private lateinit var radioGroup4: RadioGroup
    private lateinit var radioGroup5: RadioGroup
    private lateinit var radioGroup6: RadioGroup
    private lateinit var radioGroup7: RadioGroup
    private lateinit var radioGroup8: RadioGroup
    private lateinit var radioGroup9: RadioGroup

    private lateinit var Button11: Button
    private lateinit var Button12: Button
    private lateinit var radioGroup13: RadioGroup
    private lateinit var radioGroup14: RadioGroup
    private lateinit var radioGroup15: RadioGroup
    private lateinit var radioGroup16: RadioGroup
    private lateinit var radioGroup17: RadioGroup
    private lateinit var radioGroup18: RadioGroup
    private lateinit var radioGroup19: RadioGroup
    private lateinit var radioGroup20: RadioGroup
    private lateinit var radioGroup21: RadioGroup
    private lateinit var radioGroup22: RadioGroup

    private lateinit var SpinnerEmployStat: Spinner
    private lateinit var textViewSelectedDate: TextView
    private lateinit var selectDatePrompt: Button
    private lateinit var selectDatePrompt2: Button
    private lateinit var nextButton: Button
    private var selectedDate: String = ""
    private lateinit var selectedDateTextView: TextView
    private lateinit var selectedDateTextView2: TextView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_forms)


        database = FirebaseDatabase.getInstance().reference

        radioGroup1 = findViewById(R.id.radioGroup1) // radio group for the employment status
        radioGroup2 = findViewById(R.id.radioGroup2)
        radioGroup3 = findViewById(R.id.radioGroup3)
        radioGroup4 = findViewById(R.id.radioGroup4)
        radioGroup5 = findViewById(R.id.radioGroup5)
        radioGroup6 = findViewById(R.id.radioGroup6)
        radioGroup7 = findViewById(R.id.radioGroup7)
        radioGroup8 = findViewById(R.id.radioGroup8)
        radioGroup9 = findViewById(R.id.radioGroup9)
        SpinnerEmployStat = findViewById(R.id.radioGroup10)
        Button11 = findViewById(R.id.selectDatePrompt)
        Button12 = findViewById(R.id.selectDatePrompt12)
        radioGroup13 = findViewById(R.id.radioGroup13)
        radioGroup14 = findViewById(R.id.radioGroup14)
        radioGroup15 = findViewById(R.id.radioGroup15)
        radioGroup16 = findViewById(R.id.radioGroup16)
        radioGroup17 = findViewById(R.id.radioGroup17)
        radioGroup18 = findViewById(R.id.radioGroup18)
        radioGroup19 = findViewById(R.id.radioGroup19)
        radioGroup20 = findViewById(R.id.radioGroup20)
        radioGroup21 = findViewById(R.id.radioGroup21)
        radioGroup22 = findViewById(R.id.radioGroup22)
        selectedDateTextView = findViewById(R.id.textView_Date)
        selectedDateTextView2 = findViewById(R.id.textView_Date12)

        SpinnerEmployStat = findViewById(R.id.radioGroup10) // tied to the id in the layoutfile
        textViewSelectedDate = findViewById(R.id.textView_Date) // tied to the textview element in the layout file
        selectDatePrompt = findViewById(R.id.selectDatePrompt) //button for date seledtion
        nextButton = findViewById(R.id.nextButton00)
        selectDatePrompt2 = findViewById(R.id.selectDatePrompt12)

        val adapterEmployStat = ArrayAdapter.createFromResource(
            this, R.array.employmentStatus_item, android.R.layout.simple_spinner_dropdown_item
        )
        adapterEmployStat.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)    //adapter for the spinner, question 10
        SpinnerEmployStat.adapter = adapterEmployStat

        selectDatePrompt.setOnClickListener {// button function waiting for interaction, will call android calendar prompt, question 11
            val calendar = Calendar.getInstance()
            val year = calendar.get(Calendar.YEAR)
            val month = calendar.get(Calendar.MONTH)
            val day = calendar.get(Calendar.DAY_OF_MONTH)


            val datePickerDialog =
                DatePickerDialog(this,// will help in display the date that the user chose
                    { _, selectedYear, selectedMonth, selectedDay ->
                        val selectedDate = "$selectedDay/${selectedMonth + 1}/$selectedYear"
                        textViewSelectedDate.text =
                            "Selected Date: $selectedDate"// the user date will be displayed in this id
                    }, year, month, day
                )

            datePickerDialog.show()
        }

        selectDatePrompt2.setOnClickListener {      //calendar prompt for question 12
            val calendar = Calendar.getInstance()
            val year = calendar.get(Calendar.YEAR)
            val month = calendar.get(Calendar.MONTH)
            val day = calendar.get(Calendar.DAY_OF_MONTH)

            val datePickerDialog =
                DatePickerDialog(this,// will help in display the date that the user chose
                    { _, selectedYear, selectedMonth, selectedDay ->
                        val selectedDate = "$selectedDay/${selectedMonth + 1}/$selectedYear"
                        selectedDateTextView2.text =
                            "Selected Date: $selectedDate"// the user date will be displayed in this id
                    }, year, month, day
                )

            datePickerDialog.show()
        }

        nextButton.setOnClickListener {//next button transitions to the next activity
            if (validateInputs()) {
                val answers = getUserAnswers()
                val intent = Intent(this, overviewpageActivity::class.java)
                intent.putExtra("answers", answers as Serializable)
                intent.putExtra("selectedDate", selectedDate)

                saveAnswersToFirebase(answers)
                startActivity(intent)
            } else {
                Toast.makeText(
                    this,
                    "Please answer all questions and select a date.",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->    //regenerated by the IDE when created a new activity
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

        private fun validateInputs(): Boolean {
            return isRadioGroupChecked(radioGroup1) &&
                    isRadioGroupChecked(radioGroup2) &&
                    isRadioGroupChecked(radioGroup3) &&
                    isRadioGroupChecked(radioGroup4) &&
                    isRadioGroupChecked(radioGroup5) &&
                    isRadioGroupChecked(radioGroup6) &&
                    isRadioGroupChecked(radioGroup7) &&
                    isRadioGroupChecked(radioGroup8) &&
                    isRadioGroupChecked(radioGroup9) &&
                    isRadioGroupChecked(radioGroup13) &&
                    isRadioGroupChecked(radioGroup14) &&
                    isRadioGroupChecked(radioGroup15) &&
                    isRadioGroupChecked(radioGroup16) &&
                    isRadioGroupChecked(radioGroup17) &&
                    isRadioGroupChecked(radioGroup18) &&
                    isRadioGroupChecked(radioGroup19) &&
                    isRadioGroupChecked(radioGroup20) &&
                    isRadioGroupChecked(radioGroup21) &&
                    isSpinnerSelected() &&
                    isDateSelected()
        }

    private fun saveAnswersToFirebase(answers: LinkedHashMap<String, String>){
        val userId = FirebaseAuth.getInstance().currentUser?.uid ?: "unknow_user"
        database.child("user_responses").child(userId).setValue(answers)
            .addOnCompleteListener {task ->
                if (task.isSuccessful) {
                    Toast.makeText(this, "Responses saved successfully.", Toast.LENGTH_SHORT).show()
                }else{
                    Toast.makeText(this, "Failed to save responses", Toast.LENGTH_SHORT).show()

                }
            }


    }

    private fun isRadioGroupChecked(radioGroup: RadioGroup): Boolean{
        return radioGroup.checkedRadioButtonId != -1
    }

    private fun isSpinnerSelected(): Boolean {
        return SpinnerEmployStat.selectedItemPosition !=0

    }
    private fun isDateSelected(): Boolean {
        return textViewSelectedDate.text.toString() != "Selected Date: "
    }

    class SerialzableMap : Serializable {
        var map: LinkedHashMap<String, String> = LinkedHashMap()
    }
    private fun allQuestionsAnswered(): Boolean {  //unused
        return radioGroup1.checkedRadioButtonId != -1 &&
                radioGroup2.checkedRadioButtonId != -1 &&
                radioGroup3.checkedRadioButtonId != -1 &&
                radioGroup4.checkedRadioButtonId != -1 &&
                radioGroup5.checkedRadioButtonId != -1 &&
                radioGroup6.checkedRadioButtonId != -1 &&
                radioGroup7.checkedRadioButtonId != -1 &&
                radioGroup8.checkedRadioButtonId != -1 &&
                radioGroup9.checkedRadioButtonId != -1 &&
                //radioGroup10.checkedRadioButtonId != -1 &&
                //radioGroup11.checkedRasioButtonId != -1 &&
                radioGroup13.checkedRadioButtonId != -1 &&
                radioGroup14.checkedRadioButtonId != -1 &&
                radioGroup15.checkedRadioButtonId != -1 &&
                radioGroup16.checkedRadioButtonId != -1 &&
                radioGroup17.checkedRadioButtonId != -1 &&
                radioGroup18.checkedRadioButtonId != -1 &&
                radioGroup19.checkedRadioButtonId != -1 &&
                radioGroup20.checkedRadioButtonId != -1 &&
                radioGroup21.checkedRadioButtonId != -1 &&
                SpinnerEmployStat.selectedItem != null &&
                ::Button11.isInitialized
                ::Button12.isInitialized
    }

    private fun getUserAnswers(): LinkedHashMap<String, String> {
        val answers = LinkedHashMap<String, String>()

        answers["Question 2: Have you applied for this visa before?"] = findViewById<RadioButton>(radioGroup1.checkedRadioButtonId).text.toString()
        answers["Question 3: Do you own any assets in your home country?"] = findViewById<RadioButton>(radioGroup2.checkedRadioButtonId).text.toString()
        answers["Question 4: Are you currently a student?"] = findViewById<RadioButton>(radioGroup3.checkedRadioButtonId).text.toString()
        answers["Question 5: Have you travel to this country before?"] = findViewById<RadioButton>(radioGroup4.checkedRadioButtonId).text.toString()
        answers["Question 6: Do you have travel insurance for your trip?"] = findViewById<RadioButton>(radioGroup5.checkedRadioButtonId).text.toString()
        answers["Question 7: Do you have sufficient financial support?"] = findViewById<RadioButton>(radioGroup6.checkedRadioButtonId).text.toString()
        answers["Question 8: Do you have any criminal records?"] = findViewById<RadioButton>(radioGroup7.checkedRadioButtonId).text.toString()
        answers["Question 9: Have you applied visa for this country before?"] = findViewById<RadioButton>(radioGroup8.checkedRadioButtonId).text.toString()
        answers["Question 10: If yes, was your previous visa application approved or denied?"] = findViewById<RadioButton>(radioGroup9.checkedRadioButtonId).text.toString()
        answers["Questioin 11: What is your employment Status"] = SpinnerEmployStat.selectedItem.toString()
        answers["Question 12: when do you depart from your destination"]
        answers["Question 13: Can you provide proof of your business relationship with the host company?"] = findViewById<RadioButton>(radioGroup13.checkedRadioButtonId).text.toString()
        answers["Question 14: Are you planning to attend any specific trade shows or business events?"] = findViewById<RadioButton>(radioGroup14.checkedRadioButtonId).text.toString()
        answers["Question 15: Do you have a detailed business plan or investment proposal?"] = findViewById<RadioButton>(radioGroup15.checkedRadioButtonId).text.toString()
        answers["Question 16: Are you planning to establish a new business or invest in an existing one?"] = findViewById<RadioButton>(radioGroup16.checkedRadioButtonId).text.toString()
        answers["Question 18: Are you familiar with the country's legal requirements for foreign investors?"] = findViewById<RadioButton>(radioGroup17.checkedRadioButtonId).text.toString()
        answers["Question 19: Do you need assistance with legal or financial services for your investment?"] = findViewById<RadioButton>(radioGroup18.checkedRadioButtonId).text.toString()
        answers["Question 20: Have you previously lived and worked in other countries as a digital nomad?"] = findViewById<RadioButton>(radioGroup19.checkedRadioButtonId).text.toString()
        answers["Question 21: Are you planning to rent a place to stay, or will you use coworking spaces?"] = findViewById<RadioButton>(radioGroup20.checkedRadioButtonId).text.toString()
        answers["Question 22: Seeking a path to citizenship?"] = findViewById<RadioButton>(radioGroup21.checkedRadioButtonId).text.toString()
        answers["Selected Date"] = selectedDate

        return answers
    }


}