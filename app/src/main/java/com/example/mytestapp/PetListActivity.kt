package com.example.mytestapp

import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.get
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.activity_pet_list.*
import kotlinx.android.synthetic.main.activity_pet_sitters_list.*
import kotlinx.android.synthetic.main.pet_details.view.*
import kotlinx.android.synthetic.main.pet_sitters_details.view.txt_name
import java.io.File

class PetListActivity : AppCompatActivity(), RecyclerViewInterface {

    lateinit var handler: DBHelper
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pet_list)

        //Mostrar el toolbar
        MyToolBar().show(this,"Your Pets",true,true)

        handler = DBHelper(this)
        val userLoggedInCredentials = handler.selectUserLoggedIn()
        val userPetList = handler.retrievePetData("lol")

        val adapter = PetAdapter(this,userPetList,this)
        my_listPets.layoutManager = LinearLayoutManager(this)

        my_listPets.adapter = adapter



        if(userPetList.isEmpty()) {
            setContentView(R.layout.fragment_no_pet)
            val noPetButton: Button = findViewById(R.id.addPet)
            noPetButton.setOnClickListener {
                val intent = Intent(this, AddPetActivity::class.java)
                startActivity(intent)
            }
        }
        else{
            val addNewPet : Button = findViewById(R.id.addPetList)
            addNewPet.setOnClickListener {
                val intent = Intent(this, AddPetActivity::class.java)
                startActivity(intent)
            }
        }

    }

    override fun onPetClick(position: Int, pet : Pet) {
        val intent = Intent(this, PetActivity::class.java)
        intent.putExtra("pet", pet)
        startActivity(intent)
    }
}

class PetAdapter(private  val mContext: Context, private val listPets: List<Pet>, private  val recyclerViewInterface: RecyclerViewInterface) :
    RecyclerView.Adapter<PetAdapter.PetViewHolder>() {


    class PetViewHolder(itemView: View, private  val recyclerViewInterface: RecyclerViewInterface ) : RecyclerView.ViewHolder(itemView) {
        val nameTextView: TextView = itemView.txt_name
        val speciesTextView: TextView = itemView.txt_species
        val ageTextView: TextView = itemView.num_age
        val photoImageView: ImageView = itemView.pet_img // new ImageView for photo


        fun bind(pet: Pet) {
            nameTextView.text = pet.name
            speciesTextView.text = pet.species
            ageTextView.text = "${pet.age} years"

            // Load the photo from the file system
            val file = File(pet.foto)
            if (file.exists()) {
                val bitmap = BitmapFactory.decodeFile(file.absolutePath)
                photoImageView.setImageBitmap(bitmap)
            }
            itemView.setOnClickListener {
                if (recyclerViewInterface != null) {
                    val pos = adapterPosition
                    if (pos != RecyclerView.NO_POSITION) {
                        recyclerViewInterface.onPetClick(pos, pet)
                    }
                }
            }

        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PetViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.pet_details, parent, false)
        return PetViewHolder(view, recyclerViewInterface)
    }

    override fun onBindViewHolder(holder: PetViewHolder, position: Int) {
        val pet = listPets[position]
        holder.bind(pet)

    }

    override fun getItemCount(): Int {
        return listPets.size
    }


    }


