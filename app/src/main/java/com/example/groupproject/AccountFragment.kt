package com.example.groupproject

import android.content.Intent
import android.content.SharedPreferences
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

    override fun onViewCreated(
        view: View, savedInstanceState: Bundle?,
        sharedPreferences: SharedPreferences
    ) {
        activity?.let {


            email = fragmentView.findViewById(R.id.email)
            password = fragmentView.findViewById(R.id.password)

            var pref = getSharedPreferences("UserInfo", 0)

            create.setOnClickListener() {
                var intent = Intent(activity, Registration::class.java)
                startActivity(intent)
            }

            login.setOnClickListener() {
                var Email = email.text.toString()
                var Pass = password.text.toString()

                var email = pref.getString("email", "")
                var password = pref.getString("password", "")

                if (Email.equals(email) && Pass.equals(password)) {
                    var intent = Intent(activity, HomeFragment::class.java)
                    startActivity(intent)
                }
            }


        }
    }
}