package com.example.mytestapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_pet_sitters_list.*
import kotlinx.android.synthetic.main.activity_requests.*
import kotlinx.android.synthetic.main.pet_sitter_request_details.view.*

class RequestsActivity : AppCompatActivity() {

    private lateinit var petSitter: PetSitter

    //Call to DB
    lateinit var handler: DBHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_requests)

        //Mostrar el toolbar
        MyToolBar().show(this,"Your Requests",true,true)

        see_location_btn.visibility = View.GONE
        cancel_button.visibility = View.GONE

        handler = DBHelper(this)

        val userLoggedInCredentials = handler.selectUserLoggedIn()
        if (userLoggedInCredentials != null){
            val petSitterActive = handler.selectPetSitterActive(userLoggedInCredentials.email)
            if (petSitterActive != null) {
                petSitter = petSitterActive
                val listPetSittersActive = mutableListOf(petSitter)
                val adapter = PetSitterRequestsAdapter(this, listPetSittersActive)
                my_list_requests.adapter = adapter

                cancel_button.visibility = View.VISIBLE
                see_location_btn.visibility = View.VISIBLE
                text_no_services.visibility = View.GONE

                val cancelButton : Button = findViewById(R.id.cancel_button)
                cancelButton.setOnClickListener {
                    if (userLoggedInCredentials != null) {
                        if (handler.getPetSitterActiveCount(userLoggedInCredentials.email) > 0) {
                            handler.deletePetSitterActiveByEmail(userLoggedInCredentials.email)
                            listPetSittersActive.clear()
                            adapter.notifyDataSetChanged()
                            cancel_button.visibility = View.GONE
                            see_location_btn.visibility = View.GONE
                            text_no_services.visibility = View.VISIBLE
                            Toast.makeText(this, "Your Pet Sitter service active was cancelled", Toast.LENGTH_SHORT).show()
                        }
                    }
                }

                val seeLocationButton : Button = findViewById(R.id.see_location_btn)
                seeLocationButton.setOnClickListener {
                    val intent = Intent(this,MapActivity::class.java)
                    startActivity(intent)
                }
            }

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