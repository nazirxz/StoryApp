package com.nazirman.sub1storyapp.ui.auth

import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.nazirman.sub1storyapp.R
import com.nazirman.sub1storyapp.api.ApiConfig
import com.nazirman.sub1storyapp.api.RegisterResponse
import com.nazirman.sub1storyapp.databinding.ActivitySignupBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SignUpActivity: AppCompatActivity() {
    private lateinit var registerBinding :ActivitySignupBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        registerBinding = ActivitySignupBinding.inflate(layoutInflater)
        setContentView(registerBinding.root)

        registerBinding.edtNama.type = "name"
        registerBinding.edtEmail.type = "email"
        registerBinding.edtPassword.type = "password"

        registerBinding.btnRegister.setOnClickListener {
            val inputName = registerBinding.edtNama.text.toString()
            val inputEmail = registerBinding.edtEmail.text.toString()
            val inputPassword = registerBinding.edtPassword.text.toString()

            createAccount(inputName, inputEmail, inputPassword)
        }
    }
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.settings, menu)

        val addMenu = menu.findItem(R.id.menu_add)
        val logoutMenu = menu.findItem(R.id.menu_logout)

        addMenu.isVisible = false
        logoutMenu.isVisible = false

        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            R.id.menu_language -> {
                val intent = Intent(Settings.ACTION_LOCALE_SETTINGS)
                startActivity(intent)
                return true
            }
        }
        return true
    }


    private fun createAccount(inputName: String, inputEmail: String, inputPassword: String) {
        showLoading(true)

        val client = ApiConfig.getApiService().createAccount(inputName, inputEmail, inputPassword)
        client.enqueue(object: Callback<RegisterResponse> {
            override fun onResponse(
                call: Call<RegisterResponse>,
                response: Response<RegisterResponse>
            ) {
                showLoading(false)
                val responseBody = response.body()
                Log.d(TAG, "onResponse: $responseBody")
                if(response.isSuccessful && responseBody?.message == "User created") {
                    Toast.makeText(this@SignUpActivity, getString(R.string.register_success), Toast.LENGTH_SHORT).show()
                    val intent = Intent(this@SignUpActivity, MainActivity::class.java)
                    startActivity(intent)
                } else {
                    Log.e(TAG, "onFailure1: ${response.message()}")
                    Toast.makeText(this@SignUpActivity, getString(R.string.register_fail), Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<RegisterResponse>, t: Throwable) {
                showLoading(false)
                Log.e(TAG, "onFailure2: ${t.message}")
                Toast.makeText(this@SignUpActivity, getString(R.string.register_fail), Toast.LENGTH_SHORT).show()
            }

        })
    }

    private fun showLoading(isLoading: Boolean) {
        if (isLoading) {
            registerBinding.pb.visibility = View.VISIBLE
        } else {
            registerBinding.pb.visibility = View.GONE
        }
    }

    companion object {
        private const val TAG = "Register Activity"
    }
}