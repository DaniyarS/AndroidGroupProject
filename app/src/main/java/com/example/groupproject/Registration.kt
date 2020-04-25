//package com.example.groupproject
//
//import android.content.Context
//import android.content.Intent
//import androidx.appcompat.app.AppCompatActivity
//import android.os.Bundle
//import android.util.Log
//import android.view.View
//import android.widget.EditText
//import android.widget.Toast
//import kotlinx.android.synthetic.main.activity_registration.*
//
//class Registration : AppCompatActivity() {
//
//    private lateinit var name: EditText
//    private lateinit var surname: EditText
//    private lateinit var email2: EditText
//    private lateinit var password2: EditText
//    private var context: Context? = null
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_registration)
//
//        context = this
//        name = findViewById<EditText>(R.id.name)
//        surname = findViewById<EditText>(R.id.surname)
//        email2 = findViewById<EditText>(R.id.email)
//        password2 = findViewById<EditText>(R.id.password)
//
//        val preferences = getSharedPreferences("UserInfo", 0)
//
//        registrate.setOnClickListener() {
//            Log.d("HELLO", "registrate button clicked")
//            val name = name.text.toString()
//            val surname = surname.text.toString()
//            val email = email.text.toString()
//            val password = password.text.toString()
//            val editor = preferences.edit()
//
//            if (email.length > 1) {
//                val value = "$name,$surname,$password,$email"
//                editor.putString(email, value)
//                Log.d("HELLO", value)
//                editor.apply()
//                val hello = preferences.getString(email, "test_variant")
//                Log.d("HELLO GET STRING", hello)
//
//                registration.visibility = View.GONE
//                val intent = Intent(context, HomeFragment::class.java)
//                startActivity(intent)
//                Toast.makeText(this, "You registered successfully", Toast.LENGTH_SHORT).show()
//            } else {
//                Toast.makeText(this, "Fill the columns!", Toast.LENGTH_SHORT).show()
//            }
//        }
//    }
//}
