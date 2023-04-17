package com.example.mytestapp

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import androidx.core.content.ContextCompat.startActivity
import kotlinx.android.synthetic.main.activity_requests.view.*
import kotlinx.android.synthetic.main.pet_sitter_request_details.view.*
import kotlinx.android.synthetic.main.pet_sitters_details.view.*

class PetSitterRequestsAdapter(

    private val mContext: Context, private val listPetSitterRequests:List<PetSitter>
    ): ArrayAdapter<PetSitter>(mContext,0,listPetSitterRequests) {

    //Call to DB
    lateinit var handler: DBHelper

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val layout = LayoutInflater.from(mContext).inflate(R.layout.pet_sitter_request_details, parent, false)

        val petSitter = listPetSitterRequests[position]

        layout.txt_name_pet_sitter_active.text = petSitter.name
        layout.txt_address_pet_sitter_active.text =  petSitter.address
        layout.img_pet_sitter_active.setImageResource(petSitter.image)

        return layout
    }
}