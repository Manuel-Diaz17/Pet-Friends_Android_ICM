package com.example.mytestapp

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import java.io.ByteArrayOutputStream

class Pet(val name: String, val species: String, val age: Int, val foto: String): java.io.Serializable {

    /*fun ByteToBit(): Bitmap {
        return BitmapFactory.decodeByteArray(foto, 0, foto.size)
    }*/

  /*  companion object {
        fun from(name: String, species: String, age: Int, foto: Bitmap): Pet {
            val stream = ByteArrayOutputStream()
            foto.compress(Bitmap.CompressFormat.PNG, 100, stream)
            val byteArray = stream.toByteArray()
            return Pet(name, species, age, byteArray)
        }
    }*/
}