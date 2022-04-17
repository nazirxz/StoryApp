package com.nazirman.sub1storyapp.ui.auth

import android.content.ContentValues.TAG
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import android.view.View
import android.widget.Toast
import com.nazirman.sub1storyapp.R
import com.nazirman.sub1storyapp.SharedViewModel
import com.nazirman.sub1storyapp.StoryActivity
import com.nazirman.sub1storyapp.app.ApiConfig
import com.nazirman.sub1storyapp.app.LoginResponse
import com.nazirman.sub1storyapp.databinding.ActivityMainBinding
import com.nazirman.sub1storyapp.model.UserAuth
import kotlinx.android.synthetic.main.activity_main.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class MainActivity : AppCompatActivity() {
    private lateinit var mainViewModel: SharedViewModel
    private lateinit var activityMainBinding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityMainBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(activityMainBinding.root)

        btn_login.setOnClickListener {
            val inputEmail = activityMainBinding.edtEmail.text.toString()
            val inputPassword = activityMainBinding.edtPassword.text.toString()

            login(inputEmail, inputPassword)
        }
        btn_register.setOnClickListener{
            startActivity(Intent(this@MainActivity,
                SignUpActivity::class.java))
        }
    }
    private fun login(inputEmail: String, inputPassword: String) {
        showLoading(true)

        val client = ApiConfig.getApiService().login(inputEmail, inputPassword)
        client.enqueue(object: Callback<LoginResponse> {
            override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
                showLoading(false)
                val responseBody = response.body()
                Log.d(TAG, "onResponse: $responseBody")
                if(response.isSuccessful && responseBody?.message == "success") {
                    mainViewModel.saveUser(UserAuth(responseBody.loginResult.token, true))
                    Toast.makeText(this@MainActivity, getString(R.string.login_success), Toast.LENGTH_SHORT).show()
                    val intent = Intent(this@MainActivity, StoryActivity::class.java)
                    startActivity(intent)
                } else {
                    Log.e(TAG, "onFailure1: ${response.message()}")
                    Toast.makeText(this@MainActivity, getString(R.string.login_fail), Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                showLoading(false)
                Log.e(TAG, "onFailure2: ${t.message}")
                Toast.makeText(this@MainActivity, getString(R.string.login_fail), Toast.LENGTH_SHORT).show()
            }

        })
    }
    private fun showLoading(isLoading: Boolean) {
        if (isLoading) {
            progressBar.visibility = View.VISIBLE
        } else {
            progressBar.visibility = View.GONE
        }
    }

}