package com.example.groupproject

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import kotlinx.android.synthetic.main.fragment_account.*

/**
 * A simple [Fragment] subclass.
 */
class AccountFragment : Fragment() {

    private lateinit var email: EditText
    private lateinit var password: EditText

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_account, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        activity?.let {

            email = it.findViewById(R.id.tvEmail)
            password = it.findViewById(R.id.tvPassword)

            val preferences = this.activity?.getSharedPreferences("UserInfo", 0)

            create.setOnClickListener() {
                val intent = Intent(activity, Registration::class.java)
                startActivity(intent)
            }

            btLogin.setOnClickListener() {
                val userEmail = email.text.toString()
                val userPass = password.text.toString()

                val hello = preferences?.getString(userEmail, "test_variant")

                Log.d("Hello from login", hello + "userEmail" + userEmail)
                if (hello == "test_variant") {
                    Toast.makeText(activity, "Registrate first", Toast.LENGTH_SHORT).show()
                } else {
                    val array = hello?.split(",")?.toTypedArray()
                    val password = array?.get(2)
                    if (password == userPass) {
                        val intent = Intent(activity, HomeFragment::class.java)
//                        startActivity(intent)
                        Toast.makeText(activity, "You have signed in", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(activity, "Incorrect password", Toast.LENGTH_SHORT).show()
                    }
                }

//                if (tvEmail.length()>1 && tvPassword.length()>1) {
//                    if (userEmail == email && userPass == password){
//                        authorization.visibility = View.GONE
//
//                        val intent = Intent(activity, HomeFragment::class.java)
//                        startActivity(intent)
//                        Toast.makeText(activity, "You have signed in", Toast.LENGTH_SHORT).show()
//                    } else {
//                        Toast.makeText(activity, "Please, fill the blank", Toast.LENGTH_SHORT).show()
//                    }
//                } else {
//                    Toast.makeText(activity, "Registrate first", Toast.LENGTH_SHORT).show()
//                }
            }
        }
    }
}