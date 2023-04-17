package com.example.mytestapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_pet_sitters.*

class PetSittersActivity : AppCompatActivity() {

    //Call to DB
    lateinit var handler: DBHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pet_sitters)

        handler = DBHelper(this)

        val petSitter = intent.getSerializableExtra("petSitter") as PetSitter

        //Mostrar el toolbar
        MyToolBar().show(this,"${petSitter.name}",true,true)


        val scheduleButton : Button = findViewById(R.id.btn_schedule)

        scheduleButton.setOnClickListener {
            if (handler.getPetSitterActiveCount() > 0){
                Toast.makeText(this,"You already have a Pet Sitter service active", Toast.LENGTH_SHORT).show()
            }
            if (handler.getPetSitterActiveCount() <= 0) {
                val userLoggedInCredentials = handler.selectUserLoggedIn()
                if (userLoggedInCredentials != null){
                    handler.insertDBpetSitterActive(petSitter.name, petSitter.address, petSitter.rating, petSitter.image, petSitter.age,petSitter.typeOfPets, petSitter.review_1, petSitter.review_2, petSitter.review_3, userLoggedInCredentials.email)
                    val intent : Intent = Intent(this, MapActivity::class.java)
                    startActivity(intent)
                    Toast.makeText(this, "Success Schedule", Toast.LENGTH_SHORT).show()
                }
            }
        }

        pet_sitter_name.text = petSitter.name
        pet_sitter_age.text = "${petSitter.age}" + " years"
        pet_sitter_address.text =petSitter.address
        pet_sitter_img.setImageResource(petSitter.image)
        pet_sitter_pets.text =petSitter.typeOfPets
        pet_sitter_rating.rating = petSitter.rating
        txt_review_01.text = '"'+ petSitter.review_1 + '"'
        txt_review_02.text = '"' + petSitter.review_2 + '"'
        txt_review_03.text = '"' + petSitter.review_3 + '"'

    }
}