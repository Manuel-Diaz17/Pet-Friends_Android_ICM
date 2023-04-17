package com.example.mytestapp

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import kotlinx.android.synthetic.main.pet_details.view.*
import kotlinx.android.synthetic.main.pet_sitters_details.view.*
import kotlinx.android.synthetic.main.pet_sitters_details.view.img_profile
import kotlinx.android.synthetic.main.pet_sitters_details.view.txt_name

class PetSitterAdapter(
    private val mContext: Context, private val listPetSitter:List<PetSitter>
    ):ArrayAdapter<PetSitter>(mContext,0,listPetSitter) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val layout = LayoutInflater.from(mContext).inflate(R.layout.pet_sitters_details, parent, false)


        val petSitter = listPetSitter[position]

        layout.txt_name.text = petSitter.name
        layout.txt_address.text =  petSitter.address
        layout.img_profile.setImageResource(petSitter.image)
        layout.rating.rating = petSitter.rating
        return layout
    }
}