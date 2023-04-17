package com.example.mytestapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.core.util.PatternsCompat
import com.example.mytestapp.databinding.ActivitySignUpBinding
import com.google.android.material.textfield.TextInputLayout
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_sign_up.*
import kotlinx.android.synthetic.main.pet_sitters_details.*
import java.util.regex.Pattern

class SignUpActivity : AppCompatActivity() {

    //Call to DB
    lateinit var handler: DBHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

        handler = DBHelper(this)

        val btnSignUp: Button = findViewById(R.id.btn_signup)
        btnSignUp.setOnClickListener {
            validateChk()
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }




    }

    private fun validateChk():Boolean{
        if(cb_terms.isChecked){
            validate()
            return true
        }else{
            Toast.makeText(this,"Accept the privacy permissions.",Toast.LENGTH_SHORT).show()
            return false
        }
    }

    private fun validate(){
        //TextInputs
        val name = txf_name
        val lastname =  txf_lastname
        val address = txf_address
        val phone = txf_phone
        val result = arrayOf(validateEmail(),validatePassword(),validateThing(name),validateThing(lastname),validateThing(address),validateThing(phone))

        if(false in result){
            Toast.makeText(this,"Failed to register User",Toast.LENGTH_SHORT).show()
            return
        }

            handler.insertDB(
                txf_email.editText?.text.toString(),
                txf_password.editText?.text.toString(),
                txf_name.editText?.text.toString(),
                txf_lastname.editText?.text.toString(),
                txf_address.editText?.text.toString(),
                txf_phone.editText?.text.toString())

            Toast.makeText(this,"User Sign Up Success",Toast.LENGTH_SHORT).show()


    }

    private fun validateEmail():Boolean{
        val email = txf_email.editText?.text.toString()
        /*return if(email.isEmpty()){
            txf_email.error = "Field can not be empty"
            false
        }else if(!PatternsCompat.EMAIL_ADDRESS.matcher(email).matches()){
            txf_email.error = "Please enter a valid email"
            false
        }else{
            txf_email.error = null
            true
        }*/
        return true
    }
    private fun validateThing(data:TextInputLayout):Boolean{
        val thing = data.editText?.text.toString()
        return if(thing.isEmpty()){
            data.error = "Field can not be empty"
            false
        }else{
            data.error = null
            true
        }
    }

    private fun validatePassword():Boolean{
        val password = txf_password.editText?.text.toString()
        /*val passwordRegex = Pattern.compile(
            "^" +
                    "(?=.*[0-9])" +
                    "(?=.*[a-z])" +
                    "(?=.*[A-Z])" +
                    "(?=.*[@#$%^&+=])" +
                    "(?=\\S+$)" +
                    ".{4,}" +
                    "$"
        )
        return if(password.isEmpty()){
            txf_password.error = "Field can not be empty"
            false
        }else if(!passwordRegex.matcher(password).matches()){
            txf_password.error = "Password is too weak"
            false
        }else{
            txf_password.error = null
            true
        }*/
        return true
    }
}