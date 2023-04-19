package com.example.mytestapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.Toast
import androidx.core.util.PatternsCompat
import com.google.android.material.textfield.TextInputLayout
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_sign_up.*
import java.util.regex.Pattern

class LoginActivity : AppCompatActivity() {

    //Call to DB
    lateinit var handler: DBHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val btnSignUp: Button = findViewById(R.id.btn_register)
        val btnLogin: Button = findViewById(R.id.btn_login)
        btnSignUp.setOnClickListener{
            val intent : Intent = Intent(this, SignUpActivity::class.java)
            startActivity(intent)
        }

        handler = DBHelper(this)

        //Call to DB to check the users

        btnLogin.setOnClickListener {
            validate()

        }
    }
    private fun validate(){
        //TextInputs
        val result = arrayOf(validateEmail(),validatePassword())

        if(false in result){
            return
        }
        if(handler.selectDB(txf_login_email.editText?.text.toString(),txf_login_password.editText?.text.toString())){
            Toast.makeText(this,"User Login Success",Toast.LENGTH_SHORT).show()
            val intent : Intent = Intent(this, MainActivity::class.java)
            startActivity(intent)

            if (handler.getUserLoggedInCount() > 0) {
                handler.deleteAllUserLoggedIn()
            }

            handler.insertDBuserLoggedIn(txf_login_email.editText?.text.toString(),txf_login_password.editText?.text.toString())

        }else{
            Toast.makeText(this,"Email/Password is wrong",Toast.LENGTH_SHORT).show()
        }

    }
    private fun validateEmail():Boolean{
        val email = txf_login_email.editText?.text.toString()
        return if(email.isEmpty()){
            txf_login_email.error = "Field can not be empty"
            false
        }else if(!PatternsCompat.EMAIL_ADDRESS.matcher(email).matches()){
            txf_login_email.error = "Please enter a valid email"
            false
        }else{
            txf_login_email.error = null
            true
        }
        return true
    }

    private fun validatePassword():Boolean{
        val password = txf_login_password.editText?.text.toString()

        return if(password.isEmpty()){
            txf_login_password.error = "Field can not be empty"
            false
        }else{
            txf_login_password.error = null
            true
        }
    }

}