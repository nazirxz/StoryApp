package com.nazirman.sub1storyapp

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity
import com.nazirman.sub1storyapp.R
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import android.view.View
import android.widget.Button
import android.widget.Toast
import com.nazirman.sub1storyapp.app.ApiConfig
import kotlinx.android.synthetic.main.activity_signin.*
import okhttp3.ResponseBody


class SignInActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signin)

        btn_login.setOnClickListener {
            login()
        }
        btn_register.setOnClickListener{
            startActivity(Intent(this@SignInActivity,
                SignUpActivity::class.java))
        }
    }
    fun login() {
        if (edt_email.text.isEmpty()) {
            edt_email.error = "Kolom Email tidak boleh kosong"
            edt_email.requestFocus()
            return
        } else if (edt_password.text.isEmpty()) {
            edt_password.error = "Kolom Password tidak boleh kosong"
            edt_password.requestFocus()
            return
        } else if (edt_password.text.toString().length < 6) {
            edt_password.setError("Minimum password ialah 6 karakter")
            edt_password.requestFocus()
            edt_password.isEnabled = true
        }
    }
}