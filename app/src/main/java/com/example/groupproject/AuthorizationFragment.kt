package com.example.groupproject

import android.R
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.example.groupproject.api.RequestToken
import com.example.groupproject.api.RetrofitMoviesService
import com.example.groupproject.api.Session
import com.example.groupproject.api.Validation
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


/**
 * A simple [Fragment] subclass.
 */
class AuthorizationFragment : Fragment() {

    private val APP_PREFERENCES = "appsettings"
    private val APP_SESSION = "session_id"

    private lateinit var accountFragment:AccountFragment

    private lateinit var username: EditText
    private lateinit var password: EditText

    lateinit var requestToken: String
    lateinit var validation: Validation
    lateinit var requestTokenResult : RequestToken
    private lateinit var pref: SharedPreferences
    private lateinit var signIN: Button

    var session_id : String = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(com.example.groupproject.R.layout.fragment_authorization,container,false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        activity?.let {
            pref = it.getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE)
            username = it.findViewById(com.example.groupproject.R.id.etUsername)
            password = it.findViewById(com.example.groupproject.R.id.etPassword)
            signIN = it.findViewById(com.example.groupproject.R.id.btRegistrate)

            accountFragment = AccountFragment()

        }
        signIN.setOnClickListener(){
            initNewToken()
        }
    }


    private fun initNewToken(){
        RetrofitMoviesService.getMovieApi().getToken(BuildConfig.MOVIE_DB_API_TOKEN).enqueue(object:
            Callback<RequestToken> {
            override fun onFailure(call : Call<RequestToken>, t : Throwable){
                requestToken = ""
                Toast.makeText(activity?.applicationContext,"TokenRequest Failure", Toast.LENGTH_LONG).show()
            }
            override fun onResponse(call : Call<RequestToken>, response : Response<RequestToken>){
                if (response.isSuccessful){
                    val result = response.body()
                    if (result!=null){
                        requestToken = result.request_token
                        validation = Validation(username.text.toString(),password.text.toString(),requestToken)
                        initValidation()
                    } else {
                        Toast.makeText(activity?.applicationContext,"TokenRequest Failure", Toast.LENGTH_LONG).show()
                    }
                }
            }
        })
    }

    private fun initValidation(){
        RetrofitMoviesService.getMovieApi().validation(BuildConfig.MOVIE_DB_API_TOKEN,validation).enqueue(object :
            Callback<RequestToken> {
            override fun onFailure(call : Call<RequestToken>, t : Throwable){
                Toast.makeText(activity?.applicationContext,"Wrong validation", Toast.LENGTH_LONG).show()
            }

            override fun onResponse(call : Call<RequestToken>, response : Response<RequestToken>){
                if (response.isSuccessful){
                    requestTokenResult = RequestToken(requestToken)
                    initSession()
                }
                else{
                    Toast.makeText(activity?.applicationContext,"Wrong validation", Toast.LENGTH_LONG).show()
                }
            }
        })

    }

    private fun initSession(){
        RetrofitMoviesService.getMovieApi().createSession(BuildConfig.MOVIE_DB_API_TOKEN,requestTokenResult).enqueue(object :
            Callback<Session> {
            override fun onFailure(call : Call<Session>, t : Throwable){
                Toast.makeText(activity?.applicationContext,"No session id", Toast.LENGTH_LONG).show()
            }

            override fun onResponse(call : Call<Session>, response : Response<Session>){
                if (response.isSuccessful){
                    session_id = response.body()?.session_id.toString()
                    editSharedPref()
                    val bundle:Bundle =  Bundle()
                    bundle.putString("user_name", username.text.toString())
                    bundle.putString("session_id", session_id.removeRange(5, session_id.length))
                    accountFragment.arguments = bundle
                    setFragment(accountFragment)
                }
                else{
                    Toast.makeText(activity?.applicationContext,"No session id", Toast.LENGTH_LONG).show()
                }
            }
        })

    }

    private fun setFragment(fragment: Fragment){
        val fragmentTransaction:FragmentTransaction = fragmentManager?.beginTransaction()!!
        fragmentTransaction.replace(com.example.groupproject.R.id.main_frame, fragment)
        fragmentTransaction.commit()
    }

    private fun editSharedPref(){
        val tempEdit = pref.edit()
        tempEdit.putString("username", username.text.toString())
        tempEdit.putString(APP_SESSION, session_id)
        tempEdit.apply()
    }
}
