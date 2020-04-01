package com.example.groupproject

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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registration)

        name = findViewById(R.id.name) as EditText
        surname = findViewById(R.id.surname) as EditText
        email2 = findViewById(R.id.email2) as EditText
        password2 = findViewById(R.id.password2) as EditText

        var pref = getSharedPreferences("UserInfo", 0)

        registrate.setOnClickListener() {
            var name = name.text.toString()
            var surname = surname.text.toString()
            var email2 = email2.text.toString()
            var password2 = password2.text.toString()
            var editor = pref.edit()

            if (email.length() > 1) {
                editor.putString("name", name)
                editor.putString("surname", surname)
                editor.putString("email2", email2)
                editor.putString("password2", password2)
                editor.commit()

                var intent = Intent(activity, HomeFragment::class.java)
                startActivity(intent)
            } else {
                Toast.makeText(this, "Fill the columns!", Toast.LENGTH_LONG).show()
            }
        }
    }
}
