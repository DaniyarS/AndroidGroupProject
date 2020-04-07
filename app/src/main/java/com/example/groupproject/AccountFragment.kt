package com.example.groupproject

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView

/**
 * A simple [Fragment] subclass.
 */
class AccountFragment : Fragment() {

    private lateinit var tvSession: TextView
    private lateinit var tvUser: TextView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_account, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        activity?.let {

            tvUser = it.findViewById(R.id.tvNameuser)
            tvSession = it.findViewById(R.id.tvSessionId)

            val bundle: Bundle? = arguments
            if (bundle != null){
                tvUser.text = bundle.getString("user_name")
                tvSession.text = bundle.getString("session_id")
            }

           // val preferences = context?.getSharedPreferences("UserInfo", 0)

        }
    }
}