package com.example.mytestapp

import android.content.Intent
import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.Button
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_pet.*
import java.io.File

class PetActivity : AppCompatActivity() {
    lateinit var handler: DBHelper
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pet)

        val pet = intent.getSerializableExtra("pet") as Pet
        val DeletePet : Button = findViewById(R.id.btn_deletePet)
        handler = DBHelper(this)


        //Mostrar el toolbar
        MyToolBar().show(this, "${pet.name}", true, true)

        pet_name.text = pet.name
        pet_age.text = "${pet.age}" + " years"
        pet_species.text = pet.species
        // Load the photo from the file system
        val file = File(pet.foto)
        if (file.exists()) {
            val bitmap = BitmapFactory.decodeFile(file.absolutePath)
            pet_img_profile.setImageBitmap(bitmap)

        }


        DeletePet.setOnClickListener {
            if (handler.deletePet(pet)) {
                Toast.makeText(this,"Pet deleted successfully", Toast.LENGTH_SHORT).show()
                val intent = Intent(this, PetListActivity::class.java)
                startActivity(intent)
            } else {
                Toast.makeText(this,"Error: Try again", Toast.LENGTH_SHORT).show()
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