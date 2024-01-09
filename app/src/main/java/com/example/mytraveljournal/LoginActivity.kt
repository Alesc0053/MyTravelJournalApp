package com.example.mytraveljournal

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.mytraveljournal.TravelData.userData.User
import com.example.mytraveljournal.TravelData.userData.UserViewModel

class LoginActivity : AppCompatActivity() {

    private lateinit var mUserViewModel: UserViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Thread.sleep(2000)
        installSplashScreen()

        setContentView(R.layout.activity_login2)
        mUserViewModel = ViewModelProvider(this).get(UserViewModel::class.java)

        findViewById<Button>(R.id.register).setOnClickListener {
            // Move the extraction inside the OnClickListener
            val name = findViewById<EditText>(R.id.email).text.toString()
            val password = findViewById<EditText>(R.id.password).text.toString()

            if (checkConent(name, password)) {
                val newUser = User(name = name, password = password)
                mUserViewModel.insertUser(newUser)
                Toast.makeText(this@LoginActivity, "User added", Toast.LENGTH_SHORT).show()
            }
        }

        findViewById<Button>(R.id.login).setOnClickListener {
            val name = findViewById<EditText>(R.id.email).text.toString()
            val password = findViewById<EditText>(R.id.password).text.toString()
            mUserViewModel.loginUser(name, password)

            mUserViewModel.loginResult.observe(this, Observer { user ->
                if (user != null) {
                    val userId = user.id

                    val intent = Intent(this@LoginActivity, MainActivity::class.java)

                    intent.putExtra("userId", userId)
                    intent.putExtra("userName", name)
                    startActivity(intent)


                } else {
                    Toast.makeText(
                        this@LoginActivity,
                        "Incorrect name or password",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            })
        }


    }

    fun checkConent(name: String, password: String): Boolean {
        return !(TextUtils.isEmpty(name) && TextUtils.isEmpty(password))
    }
}