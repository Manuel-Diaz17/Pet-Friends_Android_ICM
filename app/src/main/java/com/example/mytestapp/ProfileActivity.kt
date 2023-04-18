package com.example.mytestapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.widget.Button
import android.widget.Toast
import androidx.constraintlayout.motion.widget.TransitionBuilder.validate
import com.google.android.material.textfield.TextInputLayout
import kotlinx.android.synthetic.main.activity_profile.*
import kotlinx.android.synthetic.main.activity_profile.et_address
import kotlinx.android.synthetic.main.activity_profile.et_lastname
import kotlinx.android.synthetic.main.activity_profile.et_name
import kotlinx.android.synthetic.main.activity_profile.et_phone
import kotlinx.android.synthetic.main.activity_sign_up.*

class ProfileActivity : AppCompatActivity() {
    lateinit var handler: DBHelper
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        MyToolBar().show(this, "Profile", true, true)

        handler = DBHelper(this)

        val userLoggedInCredentials = handler.selectUserLoggedIn()
        Log.d("login", userLoggedInCredentials.toString())

        if (userLoggedInCredentials != null) {
            val user = handler.selectUserData(userLoggedInCredentials.email, userLoggedInCredentials.password)
            if (user != null) {
                txt_profile.text = user.name + " " + user.lastname
                et_email.setText(user.email)
                et_name.setText(user.name)
                et_lastname.setText(user.lastname)
                et_address.setText(user.address)
                et_phone.setText(user.phone)
                Log.d("Name:", user.toString())
            }
        }

        val btnEditProfile: Button = findViewById(R.id.btn_edit_profile)
        btnEditProfile.setOnClickListener {
            validate()
        }

    }

    private fun validate(){
        //TextInputs
        val name = txf_name_profile
        val lastname =  txf_lastname_profile
        val address = txf_address_profile
        val phone = txf_phone_profile
        val result = arrayOf(validateThing(name),validateThing(lastname),validateThing(address),validateThing(phone))

        if(false in result){
            Toast.makeText(this,"Failed to register User", Toast.LENGTH_SHORT).show()
            return
        }

        handler.updateUserData(
            txf_email_profile.editText?.text.toString(),
            txf_name_profile.editText?.text.toString(),
            txf_lastname_profile.editText?.text.toString(),
            txf_address_profile.editText?.text.toString(),
            txf_phone_profile.editText?.text.toString())

        Toast.makeText(this,"User Updated Success", Toast.LENGTH_SHORT).show()


    }

    private fun validateThing(data: TextInputLayout):Boolean{
        val thing = data.editText?.text.toString()
        return if(thing.isEmpty()){
            data.error = "Field can not be empty"
            false
        }else{
            data.error = null
            true
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle item selection
        when (item.itemId) {
            android.R.id.home -> {
                // Do something when the up button is clicked
                onBackPressed() // For example, go back to the previous activity
                return true
            }
            else -> return super.onOptionsItemSelected(item)
        }
    }

}
