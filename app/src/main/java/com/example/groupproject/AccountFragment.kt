package com.example.groupproject

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
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

            val preferences = context?.getSharedPreferences("UserInfo", 0)

            create.setOnClickListener() {
                val intent = Intent(activity, Registration::class.java)
                startActivity(intent)
            }

            btLogin.setOnClickListener() {
                val userEmail = email.text.toString()
                val userPass = password.text.toString()

                val email = preferences?.getString("email", "")
                val password = preferences?.getString("password", "")

                if (userEmail == email && userPass == password) {
                    val intent = Intent(activity, HomeFragment::class.java)
                    startActivity(intent)
                }
            }
        }
    }
}