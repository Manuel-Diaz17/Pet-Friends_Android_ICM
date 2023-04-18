package com.example.mytestapp

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.result.contract.ActivityResultContracts.RequestPermission
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import kotlinx.android.synthetic.main.activity_add_pet.*
import kotlinx.android.synthetic.main.activity_pet.*
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

class AddPetActivity : AppCompatActivity() {
    //Call to DB
    lateinit var handler: DBHelper
    private lateinit var editTextName: EditText
    private lateinit var editTextSpecies: EditText
    private lateinit var editTextAge: EditText
    private lateinit var buttonAddPet: Button
    private lateinit var currentPhotoPath: String

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_pet)

        editTextName = findViewById(R.id.editTextName)
        editTextSpecies = findViewById(R.id.editTextSpecies)
        editTextAge = findViewById(R.id.editTextAge)
        buttonAddPet = findViewById(R.id.createPet)

        handler = DBHelper(this)
        val userLoggedInCredentials = handler.selectUserLoggedIn()
        var foto1 = BitmapFactory.decodeResource(resources, R.drawable._puppy_image)

        buttonAddPet.setOnClickListener {
            val id = UUID.randomUUID().toString()
            val name = editTextName.text.toString()
            val species = editTextSpecies.text.toString()
            val age = editTextAge.text.toString().toInt()
            val foto = foto1


            val filePath = savePhotoToFile(foto)


            val newpet = Pet(id, name, species, age, filePath)
            if (userLoggedInCredentials != null) {
                val user = handler.selectUserData(
                    userLoggedInCredentials.email,
                    userLoggedInCredentials.password
                )
                if (user != null) {
                    handler.insertPetData(user.email, newpet)
                    Toast.makeText(this, "Pet added successfully", Toast.LENGTH_SHORT).show()
                }
            }
            val intent = Intent(this, PetListActivity::class.java)
            startActivity(intent)
        }

        editTextName.addTextChangedListener(textWatcher)
        editTextSpecies.addTextChangedListener(textWatcher)
        editTextAge.addTextChangedListener(textWatcher)

        //Get from gallery
        val pickImage = registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
            uri?.let {
                val inputStream = contentResolver.openInputStream(uri)
                val bitmap = BitmapFactory.decodeStream(inputStream)
                foto1=bitmap
                img_pet_prof.setImageBitmap(bitmap)
            }
        }

        // Open camera
        val takePicture = registerForActivityResult(ActivityResultContracts.TakePicture()) { success ->
            if (success) {
                // Image capture succeeded, handle the result.
                val bitmap = BitmapFactory.decodeFile(currentPhotoPath)
                foto1 = bitmap
                img_pet_prof.setImageBitmap(bitmap)
            } else {
                // Image capture failed, advise user.
                Toast.makeText(this, "Failed to capture image", Toast.LENGTH_SHORT).show()
            }
        }
        addphoto.setOnClickListener{
            camera.visibility = View.VISIBLE
            gallery.visibility = View.VISIBLE
            addphoto.visibility = View.INVISIBLE
        }

        camera.setOnClickListener {
            camera.visibility = View.INVISIBLE
            gallery.visibility = View.INVISIBLE
            addphoto.visibility = View.VISIBLE

            val photoFile: File? = try {
                createImageFile()
            } catch (ex: IOException) {
                // Error occurred while creating the File
                null
            }

            photoFile?.also {
                val photoURI: Uri = FileProvider.getUriForFile(
                    this,
                    "com.example.mytestapp.fileprovider",
                    it
                )
                val cameraPermission = android.Manifest.permission.CAMERA
                if (ContextCompat.checkSelfPermission(this, cameraPermission) == PackageManager.PERMISSION_GRANTED) {
                    // Permission is already granted
                    takePicture.launch(photoURI)
                } else {
                    // Permission has not been granted, request it
                    ActivityCompat.requestPermissions(this, arrayOf(cameraPermission), 100)
                }

            }
        }

        gallery.setOnClickListener {
            camera.visibility = View.INVISIBLE
            gallery.visibility = View.INVISIBLE
            addphoto.visibility = View.VISIBLE
            pickImage.launch("image/*")
        }

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

    @Throws(IOException::class)
    private fun createImageFile(): File {
        // Create an image file name
        val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        val storageDir: File? = getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        return File.createTempFile(
            "JPEG_${timeStamp}_",
            ".jpg",
            storageDir
        ).apply {
            // Save a file: path for use with ACTION_VIEW intents
            currentPhotoPath = absolutePath
        }
    }


}