package com.example.shop

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.provider.OpenableColumns
import android.util.Log
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.example.shop.databinding.ActivityEditProfileBinding
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream

class ClassForTesting : AppCompatActivity() {
    private lateinit var binding: ActivityEditProfileBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.addPhoto.setOnClickListener{
            val pickImg = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI)
            val takePicture = Intent(MediaStore.ACTION_IMAGE_CAPTURE)

            val chooser = Intent.createChooser(pickImg, "Select Image")
            chooser.putExtra(Intent.EXTRA_ALTERNATE_INTENTS, arrayOf(takePicture))

            imagePicker.launch(chooser)
        }
    }

    private val imagePicker =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK){
                val data = result.data
                val imageUri = data?.data
                if (imageUri != null) {
                    val file = getFile(this, imageUri)
                    binding.imageView4.setImageURI(imageUri)
                } else{
                    data?.extras?.get("data") as Bitmap
                }
            }
        }

    private fun getFile(context: Context, uri: Uri): File {
        val destinationFileName =
            File(context.filesDir.path + File.separatorChar + queryName(context, uri))
        try {
            context.contentResolver.openInputStream(uri).use { ins ->
                createFile
            }
        }
    }

    private fun createFileFromStream(ins: InputStream, destination: File?){
        try {
            FileOutputStream(destination).use { os ->
                val buffer = ByteArray(4096)
                var length: Int
                while (ins.read(buffer).also { length = it } > 0){
                    os.write(buffer, 0, length)
                }
                os.flush()
            }
        } catch (ex: Exception){
            Log.e("Save File", e.)
        }
    }

    private fun queryName(context: Context, uri : Uri): String{
        val returnCursor = context.contentResolver.query(uri, null, null, null)!!
        val nameIndex = returnCursor.getColumnIndex(OpenableColumns.DISPLAY_NAME)
        returnCursor.moveToFirst()
        val name = returnCursor.getString(nameIndex)
        returnCursor.close()
        return name
    }
}