package com.example.groupproject

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.FragmentTransaction
import kotlinx.android.synthetic.main.fragment_account.*


/**
 * A simple [Fragment] subclass.
 */

class AccountFragment : Fragment() {



    private lateinit var tvSession: TextView
    private lateinit var tvUser: TextView
    private lateinit var ivLogo1: ImageView
    private lateinit var ivLogo2: ImageView
    private lateinit var btRegistrate2: Button
    private lateinit var authorizationFragment: AuthorizationFragment
    private lateinit var tvusername: TextView
    private lateinit var tvSessionn: TextView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_account, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        activity?.let {

        //initializing values
            tvUser = it.findViewById(R.id.tvNameuser)
            tvSession = it.findViewById(R.id.tvSessionId)
            ivLogo1 = it.findViewById(R.id.ivLogo)
            ivLogo2 = it.findViewById(R.id.ivLogo2)
            btRegistrate2 = it.findViewById(R.id.btRegistrate2)
            tvusername = it.findViewById(R.id.tvusername)
            tvSessionn = it.findViewById(R.id.tvSessionn)


            authorizationFragment = AuthorizationFragment()

            //Restoring saved data
            val sessionPreference = SessionPreference(it.applicationContext)
            var loginCount = sessionPreference.getLoginCount()

            //Checking login
            if (loginCount == 0){
                tvUser.visibility = View.GONE
                tvSession.visibility = View.GONE
                ivLogo1.visibility = View.GONE
                tvusername.visibility = View.GONE
                tvSessionn.visibility = View.GONE
                Toast.makeText(
                    it.applicationContext,
                    "Details are empty. Please, sign in first",
                    Toast.LENGTH_SHORT
                ).show()
            }
            else {
                ivLogo2.visibility = View.GONE
                btRegistrate2.visibility = View.GONE
                tvUser.text = sessionPreference.getUsername()
                tvSession.text = sessionPreference.getSessionId()
            }
            btRegistrate2.setOnClickListener {
                setFragment(authorizationFragment)
            }
        }
    }

    private fun setFragment(fragment: Fragment){
        val fragmentTransaction: FragmentTransaction = fragmentManager?.beginTransaction()!!
        fragmentTransaction.replace(R.id.main_frame, fragment)
        fragmentTransaction.commit()
    }
}