package com.example.mytestapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_pet_sitters.*

class PetSittersActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pet_sitters)

        val petSitter = intent.getSerializableExtra("petSitter") as PetSitter

        //Mostrar el toolbar
        MyToolBar().show(this,"${petSitter.name}",true,true)


        val scheduleButton : Button = findViewById(R.id.btn_schedule)

        scheduleButton.setOnClickListener { Toast.makeText(this,"Success Schedule", Toast.LENGTH_SHORT).show() }

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