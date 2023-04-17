package com.example.mytestapp

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.constraintlayout.motion.widget.TransitionBuilder.validate
import androidx.core.content.FileProvider
import kotlinx.android.synthetic.main.activity_add_pet.*
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_profile.*
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.io.ObjectOutputStream

class AddPetActivity : AppCompatActivity() {
    //Call to DB
    lateinit var handler: DBHelper
    private lateinit var editTextName: EditText
    private lateinit var editTextSpecies: EditText
    private lateinit var editTextAge: EditText
    private lateinit var buttonAddPet: Button
    private lateinit var uri: Uri
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_pet)

        editTextName = findViewById(R.id.editTextName)
        editTextSpecies = findViewById(R.id.editTextSpecies)
        editTextAge = findViewById(R.id.editTextAge)
        buttonAddPet = findViewById(R.id.createPet)
        
        handler = DBHelper(this)
        val userLoggedInCredentials = handler.selectUserLoggedIn()


        buttonAddPet.setOnClickListener {
            val name = editTextName.text.toString()
            val species = editTextSpecies.text.toString()
            val age = editTextAge.text.toString().toInt()
            val foto = BitmapFactory.decodeResource(resources, R.drawable._puppy_image)


            val filePath = savePhotoToFile(foto)

            val newpet = Pet(name, species, age, filePath)
            if (userLoggedInCredentials != null) {
                val user = handler.selectUserData(userLoggedInCredentials.email, userLoggedInCredentials.password)
                if (user != null) {
                    handler.insertPetData(user.email,newpet)
                    Toast.makeText(this,"Pet added successfully", Toast.LENGTH_SHORT).show()
                }
            }
            val intent = Intent(this, PetListActivity::class.java)
            startActivity(intent)
        }

        editTextName.addTextChangedListener(textWatcher)
        editTextSpecies.addTextChangedListener(textWatcher)
        editTextAge.addTextChangedListener(textWatcher)

        /*setOnClickListener{
                val takePicture = registerForActivityResult(ActivityResultContracts.TakePicture()) { ifSuccess ->
                if (ifSuccess) {
                    // The photo was successfully captured, you can now use the photo file
                    // which is saved in the uri variable.
                    // For example, you can set the image view to show the photo:
                    imageView.setImageURI(uri)
                }
            }



            fun takePhoto() {
                // Create a file to save the captured photo.
                val file = File(externalMediaDirs.firstOrNull(), "photo.jpg")

                // Create a URI from the file.
                uri = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    FileProvider.getUriForFile(
                        this,
                        "${BuildConfig.APPLICATION_ID}.provider",
                        file
                    )
                } else {
                    Uri.fromFile(file)
                }

                // Launch the camera app and capture the photo.
                takePicture.launch(uri)
            }

        }*/



    }

    //Only show button when text is filled
    private val textWatcher = object : TextWatcher {
        override fun afterTextChanged(s: Editable?) {
            buttonAddPet.isEnabled = editTextName.text.isNotEmpty() && editTextSpecies.text.isNotEmpty() && editTextAge.text.isNotEmpty()
            buttonAddPet.alpha = if (buttonAddPet.isEnabled) 1.0f else 0.5f
        }
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
    }

    private fun savePhotoToFile(bitmap: Bitmap): String {
        // Get the directory where the photo will be saved
        val directory = getExternalFilesDir(Environment.DIRECTORY_PICTURES)

        // Generate a unique file name for the photo
        val fileName = "${System.currentTimeMillis()}.jpg"

        // Create a file with the directory and file name
        val file = File(directory, fileName)

        // Save the bitmap to the file
        FileOutputStream(file).use { outputStream ->
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
        }

        // Return the file path
        return file.absolutePath
    }


}