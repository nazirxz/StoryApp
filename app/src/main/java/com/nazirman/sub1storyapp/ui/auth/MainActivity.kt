package com.nazirman.sub1storyapp.ui.auth

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import android.view.View
import android.widget.Toast
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import com.nazirman.sub1storyapp.R
import com.nazirman.sub1storyapp.ViewModelFactory
import com.nazirman.sub1storyapp.api.SharedViewModel
import com.nazirman.sub1storyapp.api.ApiConfig
import com.nazirman.sub1storyapp.api.LoginResponse
import com.nazirman.sub1storyapp.databinding.ActivityMainBinding
import com.nazirman.sub1storyapp.model.UserAuth
import com.nazirman.sub1storyapp.ui.story.StoryActivity
import com.nazirman.sub1storyapp.utils.UserPreference
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

class MainActivity : AppCompatActivity() {
    private lateinit var mainViewModel: SharedViewModel
    private lateinit var activityMainBinding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityMainBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(activityMainBinding.root)

        setupViewModel()
        activityMainBinding.btnLogin.setOnClickListener {
            validasi()
            val inputEmail = activityMainBinding.edtEmail.text.toString()
            val inputPassword = activityMainBinding.edtPassword.text.toString()

            login(inputEmail, inputPassword)
        }
        activityMainBinding.btnRegister.setOnClickListener{
            startActivity(Intent(this@MainActivity,
                SignUpActivity::class.java))
        }
    }

    fun validasi() {
        if ( activityMainBinding.edtEmail.text!!.isEmpty()) {
            activityMainBinding.edtEmail.error = "Kolom Email tidak boleh kosong"
            activityMainBinding.edtEmail.requestFocus()
            return
        } else if ( activityMainBinding.edtPassword.text!!.isEmpty()) {
            activityMainBinding.edtPassword.error = "Kolom Password tidak boleh kosong"
            activityMainBinding.edtPassword.requestFocus()
            return
        } else if ( activityMainBinding.edtPassword.text.toString().length < 6) {
            activityMainBinding.edtPassword.setError("Minimum password ialah 6 karakter")
            activityMainBinding.edtPassword.requestFocus()
            activityMainBinding.edtPassword.isEnabled = true
        }
    }


    private fun setupViewModel() {
        mainViewModel = ViewModelProvider(
            this,
            ViewModelFactory(UserPreference.getInstance(dataStore))
        )[SharedViewModel::class.java]

        mainViewModel.getUser().observe(this) { user ->
            if(user.isLogin) {
                val intent = Intent(this, StoryActivity::class.java)
                startActivity(intent)
                finish()
            }
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
            activityMainBinding.progressBar.visibility = View.VISIBLE
        } else {
            activityMainBinding.progressBar.visibility = View.GONE
        }
    }
    companion object {
        private const val TAG = "Main Activity"
    }
}