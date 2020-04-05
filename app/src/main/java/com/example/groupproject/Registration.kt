package com.example.groupproject

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.example.groupproject.api.RequestToken
import com.example.groupproject.api.RetrofitMoviesService
import com.example.groupproject.api.Session
import com.example.groupproject.api.Validation
import kotlinx.android.synthetic.main.activity_registration.*
import kotlinx.android.synthetic.main.fragment_account.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class Registration : AppCompatActivity() {

    private lateinit var name: EditText
    private lateinit var surname: EditText
    private lateinit var email2: EditText
    private lateinit var password2: EditText
    private var context: Context? = null

    lateinit var requestToken: String
    lateinit var validation: Validation
    lateinit var requestTokenResult : RequestToken
    private lateinit var sPreferences: SharedPreferences
    private lateinit var signUp: Button

    var session_id : String = ""
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registration)

        sPreferences = getSharedPreferences("shared_preferences", Context.MODE_PRIVATE)

        if (sPreferences.contains("session_id")) {
            val intent = Intent(this,MainActivity::class.java)
            startActivity(intent)
            finish()
        }
        context = this

        name = findViewById<EditText>(R.id.name)
        surname = findViewById<EditText>(R.id.surname)
        email2 = findViewById<EditText>(R.id.email2)
        password2 = findViewById<EditText>(R.id.password2)
        signUp = findViewById(R.id.registrate)
//        val preferences = getSharedPreferences("UserInfo", 0)

//        registrate.setOnClickListener() {
//            val name = name.text.toString()
//            val surname = surname.text.toString()
//            val email2 = email2.text.toString()
//            val password2 = password2.text.toString()
//            val editor = preferences.edit()
//
//            if (tvEmail.length() > 1) {
//                editor.putString("name", name)
//                editor.putString("surname", surname)
//                editor.putString("email2", email2)
//                editor.putString("password2", password2)
//                editor.apply()
//
//                val intent = Intent(context, HomeFragment::class.java)
//                startActivity(intent)
//            } else {
//                Toast.makeText(this, "Fill the columns!", Toast.LENGTH_LONG).show()
//            }
//        }



        signUp.setOnClickListener(){
            initNewToken()
        }
    }

    private fun initNewToken(){
        RetrofitMoviesService.getMovieApi().getToken(BuildConfig.MOVIE_DB_API_TOKEN).enqueue(object :
            Callback<RequestToken> {
            override fun onFailure(call : Call<RequestToken>, t : Throwable){
                requestToken = ""
            }

            override fun onResponse(call : Call<RequestToken>, response : Response<RequestToken>){
                    if (response.isSuccessful){
                        val result = response.body()

                        if (result!=null){
                            requestToken = result.request_token
                            validation = Validation(name.text.toString(),password2.text.toString(),requestToken)
                            initValidation()
                        }
                    }
            }
        })
    }

    private fun initValidation(){
        RetrofitMoviesService.getMovieApi().validation(BuildConfig.MOVIE_DB_API_TOKEN,validation).enqueue(object :
            Callback<RequestToken> {
            override fun onFailure(call : Call<RequestToken>, t : Throwable){

            }

            override fun onResponse(call : Call<RequestToken>, response : Response<RequestToken>){
                if (response.isSuccessful){
                    requestTokenResult = RequestToken(requestToken)
                    initSession()
                }
                else{

                }
            }
        })

    }

    private fun initSession(){
        RetrofitMoviesService.getMovieApi().createSession(BuildConfig.MOVIE_DB_API_TOKEN,requestTokenResult).enqueue(object :
            Callback<Session> {
            override fun onFailure(call : Call<Session>, t : Throwable){

            }

            override fun onResponse(call : Call<Session>, response : Response<Session>){
                if (response.isSuccessful){
                    session_id = response.body()?.session_id.toString()

                    val tempVar = sPreferences.edit()
                    tempVar.putString("name",name.text.toString())
                    tempVar.putString("session_id",session_id)
                    tempVar.apply()
                    val toActivity = Intent(this@Registration,MainActivity::class.java)
                    startActivity(toActivity)
                }
                else{

                }
            }
        })

    }
}
