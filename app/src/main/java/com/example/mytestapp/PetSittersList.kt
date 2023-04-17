package com.example.mytestapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.widget.Toolbar
import androidx.constraintlayout.motion.widget.TransitionBuilder.validate
import kotlinx.android.synthetic.main.activity_pet_sitters_list.*

class PetSittersList : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pet_sitters_list)

        //Mostrar el toolbar
        MyToolBar().show(this,"Sitters",true,true)

        //Mostrar los datos de los cuidadores guardados en local

        val petSitter1 = PetSitter("Phillip","Agueda, Aveiro",
            4.2f,R.drawable.img_user_01,27,"Dogs and Cats","The best caretaker","Highly recommended","Phillip is the best")

        val petSitter2 = PetSitter("Vanessa & Ian","Mealhada, Aveiro",
                4.7f,R.drawable.img_user_02,22,"Birds and Raccoons","The best caregivers","Highly recommended","Vanessa e Ian are the best")

        val petSitter3 = PetSitter("Rita & Phil","Aveiro, Aveiro",
            5.0f,R.drawable.img_user_03,24,"Rats and Snakes","The best caregivers","Highly recommended","Rita y Phil are the best")

        val listPetSitters = listOf(petSitter1,petSitter2,petSitter3)

        val adapter = PetSitterAdapter(this, listPetSitters)

        my_list.adapter = adapter

        my_list.setOnItemClickListener{ parent, view, position, id ->
            val intent = Intent(this,PetSittersActivity::class.java)
            intent.putExtra("petSitter",listPetSitters[position])
            startActivity(intent)
        }


    }
    override fun onBackPressed() {

    }
}