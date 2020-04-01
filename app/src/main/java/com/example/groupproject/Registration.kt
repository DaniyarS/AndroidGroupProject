package com.example.groupproject

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.EditText
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_registration.*
import kotlinx.android.synthetic.main.fragment_account.*

class Registration : AppCompatActivity() {

    private lateinit var name: EditText
    private lateinit var surname: EditText
    private lateinit var email2: EditText
    private lateinit var password2: EditText
    private var context: Context? = null
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registration)

        context = this

        name = findViewById<EditText>(R.id.name)
        surname = findViewById<EditText>(R.id.surname)
        email2 = findViewById<EditText>(R.id.email2)
        password2 = findViewById<EditText>(R.id.password2)

        val preferences = getSharedPreferences("UserInfo", 0)

        registrate.setOnClickListener() {
            val name = name.text.toString()
            val surname = surname.text.toString()
            val email2 = email2.text.toString()
            val password2 = password2.text.toString()
            val editor = preferences.edit()

            if (tvEmail.length() > 1) {
                editor.putString("name", name)
                editor.putString("surname", surname)
                editor.putString("email2", email2)
                editor.putString("password2", password2)
                editor.apply()

                val intent = Intent(context, HomeFragment::class.java)
                startActivity(intent)
            } else {
                Toast.makeText(this, "Fill the columns!", Toast.LENGTH_LONG).show()
            }
        }
    }
}
