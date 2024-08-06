package com.example.visafind

import android.content.Intent
import android.os.Bundle
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.Spinner
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import android.view.View
import android.widget.AutoCompleteTextView
import android.widget.Toast


class countryActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_country)

        val backButton: Button = findViewById(R.id.buttonPrevious)
        val spinnerContinent: Spinner = findViewById(R.id.spinner1)
        val spinnerNations: Spinner = findViewById(R.id.spinner2)
        val autoCompleteTextView: AutoCompleteTextView = findViewById(R.id.autoComplete_text)


        backButton.setOnClickListener{
            onBackPressed()
        }

        val countries = arrayOf(
            "Afghanistan", "Albania", "Algeria", "Andorra", "Angola", "Antigua and Barbuda",
            "Argentina", "Armenia", "Australia", "Austria", "Azerbaijan", "Bahamas", "Bahrain",
            "Bangladesh", "Barbados", "Belarus", "Belgium", "Belize", "Benin", "Bhutan",
            "Bolivia", "Bosnia and Herzegovina", "Botswana", "Brazil", "Brunei", "Bulgaria",
            "Burkina Faso", "Burundi", "Cabo Verde", "Cambodia", "Cameroon", "Canada",
            "Central African Republic", "Chad", "Chile", "China", "Colombia", "Comoros",
            "Congo, Democratic Republic of the", "Congo, Republic of the", "Costa Rica", "Croatia",
            "Cuba", "Cyprus", "Czech Republic", "Denmark", "Djibouti", "Dominica",
            "Dominican Republic", "East Timor (Timor-Leste)", "Ecuador", "Egypt", "El Salvador",
            "Equatorial Guinea", "Eritrea", "Estonia", "Eswatini (Swaziland)", "Ethiopia",
            "Fiji", "Finland", "France", "Gabon", "Gambia", "Georgia", "Germany", "Ghana",
            "Greece", "Grenada", "Guatemala", "Guinea", "Guinea-Bissau", "Guyana", "Haiti",
            "Honduras", "Hungary", "Iceland", "India", "Indonesia", "Iran", "Iraq", "Ireland",
            "Israel", "Italy", "Jamaica", "Japan", "Jordan", "Kazakhstan", "Kenya", "Kiribati",
            "Korea, North", "Korea, South", "Kosovo", "Kuwait", "Kyrgyzstan", "Laos", "Latvia",
            "Lebanon", "Lesotho", "Liberia", "Libya", "Liechtenstein", "Lithuania", "Luxembourg",
            "Madagascar", "Malawi", "Malaysia", "Maldives", "Mali", "Malta", "Marshall Islands",
            "Mauritania", "Mauritius", "Mexico", "Micronesia", "Moldova", "Monaco", "Mongolia",
            "Montenegro", "Morocco", "Mozambique", "Myanmar (Burma)", "Namibia", "Nauru", "Nepal",
            "Netherlands", "New Zealand", "Nicaragua", "Niger", "Nigeria", "North Macedonia (formerly Macedonia)",
            "Norway", "Oman", "Pakistan", "Palau", "Panama", "Papua New Guinea", "Paraguay",
            "Peru", "Philippines", "Poland", "Portugal", "Qatar", "Romania", "Russia", "Rwanda",
            "Saint Kitts and Nevis", "Saint Lucia", "Saint Vincent and the Grenadines", "Samoa",
            "San Marino", "Sao Tome and Principe", "Saudi Arabia", "Senegal", "Serbia", "Seychelles",
            "Sierra Leone", "Singapore", "Slovakia", "Slovenia", "Solomon Islands", "Somalia",
            "South Africa", "South Sudan", "Spain", "Sri Lanka", "Sudan", "Suriname", "Sweden",
            "Switzerland", "Syria", "Taiwan", "Tajikistan", "Tanzania", "Thailand", "Togo", "Tonga",
            "Trinidad and Tobago", "Tunisia", "Turkey", "Turkmenistan", "Tuvalu", "Uganda", "Ukraine",
            "United Arab Emirates", "United Kingdom", "United States", "Uruguay", "Uzbekistan",
            "Vanuatu", "Vatican City (Holy See)", "Venezuela", "Vietnam", "Yemen", "Zambia", "Zimbabwe"
        )

        val adapter = ArrayAdapter (
            this, android.R.layout.simple_spinner_dropdown_item, countries
        )
        autoCompleteTextView.setAdapter(adapter)



        autoCompleteTextView.setOnItemClickListener { parent, view, position, id ->
            val selectedCountry = parent.getItemAtPosition(position).toString()
            Toast.makeText(this, "Selected Country: $selectedCountry", Toast.LENGTH_SHORT).show()

            spinnerContinent.visibility = View.VISIBLE
            spinnerNations.visibility = View.VISIBLE


        val adapterContinent = ArrayAdapter.createFromResource(
            this, R.array.continents_items, android.R.layout.simple_spinner_item
        )

        adapterContinent.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerContinent.adapter = adapterContinent

        spinnerContinent.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long){
                val selectedContinent = parent.getItemAtPosition(position).toString()
                val countryArrayId = when(selectedContinent) {
                    "Europe" -> R.array.nations_europe_items
                    "Asia" -> R.array.nations_asia_items
                    "Africa" -> R.array.nations_africa_items
                    "North America" -> R.array.nations_south_america_and_caribbean
                    else -> R.array.nations_europe_items
                }
                val countryAdapter = ArrayAdapter.createFromResource(
                    this@countryActivity,
                    countryArrayId,
                    android.R.layout.simple_spinner_item
                )
                countryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                spinnerNations.adapter = countryAdapter
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                TODO("Not yet implemented")
            }

        }


            val button = findViewById<Button>(R.id.buttonNext) // this is tied to the "formsactivity" button on the xml file.
            button.setOnClickListener { // waiting for user input to transition to the next page
                // Define the action to start the new activity here
                val intent = Intent(this, formsActivity::class.java)  //this block of code transitions to the activity page
                startActivity(intent)
            }

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
            }

        }
    }
}