package com.example.groupproject

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.example.groupproject.api.RequestToken
import com.example.groupproject.api.RetrofitMoviesService
import com.example.groupproject.api.Session
import com.example.groupproject.api.Validation
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class Authorization : AppCompatActivity() {

    private val APP_PREFERENCES = "appsettings"
    private val APP_SESSION = "session_id"

    private lateinit var username: EditText
    private lateinit var password: EditText

    lateinit var requestToken: String
    lateinit var validation: Validation
    lateinit var requestTokenResult : RequestToken
    private lateinit var pref: SharedPreferences
    private lateinit var signIN: Button

    var session_id : String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_authorization)

        pref = getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE)
        initComponents()

    }

    private fun initComponents(){
        username = findViewById(R.id.etUsername)
        password = findViewById(R.id.etPassword)
        signIN = findViewById(R.id.btRegistrate)

        signIN.setOnClickListener(){
            initNewToken()
        }
    }

    private fun initNewToken(){
        RetrofitMoviesService.getMovieApi().getToken(BuildConfig.MOVIE_DB_API_TOKEN).enqueue(object: Callback<RequestToken> {
            override fun onFailure(call : Call<RequestToken>, t : Throwable){
                requestToken = ""
                Toast.makeText(this@Authorization,"TokenRequest Failure", Toast.LENGTH_LONG).show()
            }
            override fun onResponse(call : Call<RequestToken>, response : Response<RequestToken>){
                if (response.isSuccessful){
                    val result = response.body()
                    if (result!=null){
                        requestToken = result.request_token
                        validation = Validation(username.text.toString(),password.text.toString(),requestToken)
                        initValidation()
                    } else {
                        Toast.makeText(this@Authorization,"TokenRequest Failure", Toast.LENGTH_LONG).show()
                    }
                }
            }
        })
    }

    private fun initValidation(){
        RetrofitMoviesService.getMovieApi().validation(BuildConfig.MOVIE_DB_API_TOKEN,validation).enqueue(object :
            Callback<RequestToken> {
            override fun onFailure(call : Call<RequestToken>, t : Throwable){
                Toast.makeText(this@Authorization,"Wrong validation",Toast.LENGTH_LONG).show()
            }

            override fun onResponse(call : Call<RequestToken>, response : Response<RequestToken>){
                if (response.isSuccessful){
                    requestTokenResult = RequestToken(requestToken)
                    initSession()
                }
                else{
                    Toast.makeText(this@Authorization,"Wrong validation",Toast.LENGTH_LONG).show()
                }
            }
        })

    }

    private fun initSession(){
        RetrofitMoviesService.getMovieApi().createSession(BuildConfig.MOVIE_DB_API_TOKEN,requestTokenResult).enqueue(object :
            Callback<Session> {
            override fun onFailure(call : Call<Session>, t : Throwable){
                Toast.makeText(this@Authorization,"No session id",Toast.LENGTH_LONG).show()
            }

            override fun onResponse(call : Call<Session>, response : Response<Session>){
                if (response.isSuccessful){
                    session_id = response.body()?.session_id.toString()
                    editSharedPref()
                    signInSuccessIntent()
                }
                else{
                    Toast.makeText(this@Authorization,"No session id",Toast.LENGTH_LONG).show()
                }
            }
        })

    }

    private fun signInSuccessIntent(){
        val toActivity = Intent(this@Authorization,MainActivity::class.java)
        startActivity(toActivity)
    }

    private fun editSharedPref(){
        val tempEdit = pref.edit()
        tempEdit.putString("username", username.text.toString())
        tempEdit.putString(APP_SESSION, session_id)
        tempEdit.apply()
    }
}
