package com.example.groupproject

import android.content.Context
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
import kotlinx.android.synthetic.main.fragment_authorization.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import retrofit2.Response
import kotlin.coroutines.CoroutineContext


/**
 * A simple [Fragment] subclass.
 */
class AuthorizationFragment : Fragment(), CoroutineScope {

    private val APP_PREFERENCES = "appsettings"
    private val APP_SESSION = "session_id"


    private lateinit var accountFragment:AccountFragment

    private lateinit var username: EditText
    private lateinit var password: EditText

    lateinit var requestToken: String
    lateinit var validation: Validation
    lateinit var requestTokenResult : RequestToken
    private lateinit var pref: SharedPreferences
    private lateinit var btSign: Button

    var sessionId : String = ""

    private val job = Job()

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + job

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_authorization,container,false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        activity?.let {
            pref = it.getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE)
            username = it.findViewById(R.id.etUsername)
            password = it.findViewById(R.id.etPassword)
            btSign = it.findViewById(R.id.btRegistrate)
            accountFragment = AccountFragment()
            progressBar2.hide()
        }
        btSign.setOnClickListener(){
            initNewTokenCoroutine()
            progressBar2.show()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        job.cancel()
    }

    private fun initNewTokenCoroutine(){
        launch {
            val response: Response<RequestToken> = RetrofitMoviesService.getMovieApi().getTokenCoroutine(
            BuildConfig.MOVIE_DB_API_TOKEN
            )
            if (response.isSuccessful){
                val result = response.body()
                if (result!=null){
                    requestToken = result.request_token
                    validation = Validation(username.text.toString(), password.text.toString(),requestToken)
                    initValidationCoroutine()
                } else {
                    Toast.makeText(activity?.applicationContext,"TokenRequest Failure", Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    private fun initValidationCoroutine(){
        launch {
            val response: Response<RequestToken> = RetrofitMoviesService.getMovieApi().validationCoroutine(
                BuildConfig.MOVIE_DB_API_TOKEN, validation
            )
            if (response.isSuccessful){
                requestTokenResult = RequestToken(requestToken)
                initSessionCoroutine()
            }else{
                Toast.makeText(activity?.applicationContext,"Wrong validation", Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun initSessionCoroutine(){
        launch {
            val response: Response<Session> = RetrofitMoviesService.getMovieApi()
                .createSessionCoroutine(BuildConfig.MOVIE_DB_API_TOKEN, requestTokenResult)
            if (response.isSuccessful){
                progressBar2.hide()
                sessionId = response.body()?.sessionId.toString()
                editSharedPref()

                val sessionPreference = SessionPreference(activity?.applicationContext!!)
                sessionPreference.setUsername(username.text.toString())
                sessionPreference.setSessionId(sessionId.removeRange(5, sessionId.length))
                sessionPreference.setRealSessionId(sessionId)
                var loginCount = sessionPreference.getLoginCount()
                loginCount++
                sessionPreference.setLoginCount(loginCount)

                setFragment(accountFragment)
            }
            else{
                Toast.makeText(activity?.applicationContext,"No session id", Toast.LENGTH_LONG).show()
                progressBar2.hide()
            }
        }
    }


    private fun setFragment(fragment: Fragment){
        val fragmentTransaction: FragmentTransaction? = fragmentManager?.beginTransaction()
        fragmentTransaction?.replace(R.id.main_frame, fragment)
        fragmentTransaction?.commit()
    }

    private fun editSharedPref(){
        val tempEdit = pref.edit()
        tempEdit.putString("username", username.text.toString())
        tempEdit.putString(APP_SESSION, sessionId)
        tempEdit.apply()
    }
}
