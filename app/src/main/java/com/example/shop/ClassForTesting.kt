package com.example.shop

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.provider.OpenableColumns
import android.util.Log
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.shop.databinding.ActivityEditProfileBinding
import com.google.zxing.BarcodeFormat
import com.google.zxing.oned.Code128Writer
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream

class ClassForTesting : AppCompatActivity() {
    private lateinit var binding: ActivityEditProfileBinding
    private val REQUEST_IMAGE_CAPTURE = 1
    private val REQUEST_IMAGE_PICK = 2
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.imageView4.setOnClickListener { showImagePickerDialog() }


        lifecycleScope.launch {
            val text = "googleoo"
            val bitMatrix = Code128Writer().encode(
                text,
                BarcodeFormat.CODE_128,
                binding.imageView4.width,
                binding.imageView4.height
            )
            val pixels = IntArray(binding.imageView4.width * binding.barcodeImage.height)
            for (y in 0 until bitMatrix.height) {
                val offset = y * bitMatrix.width
                for (x in 0 until bitMatrix.width) {
                    pixels[offset + x] = if (bitMatrix.get(x, y)) Color.BLACK else Color.WHITE
                }
            }
            val bitmap = Bitmap.createBitmap(
                binding.imageView4.width,
                binding.imageView4.height,
                Bitmap.Config.ARGB_8888
            )
            bitmap.setPixels(
                pixels, 0,
                binding.imageView4.width,
                0, 0,
                binding.imageView4.width,
                binding.imageView4.height
            )
            binding.imageView4.setImageBitmap(bitmap)
        }

        lifecycleScope.launch {
            val text = "qwerty"
            val bitMatrix = Code128Writer().encode(text,
                BarcodeFormat.CODE_128, binding.imageView4.width,
                binding.imageView4.height)
            val pixels = IntArray(binding.imageView4.width * binding.imageView4.height)
            for (y in 0 until binding.imageView4.width){
                val offset = y * bitMatrix.width
                for (x in 0 until bitMatrix.width){
                    pixels[offset + x] = if (bitMatrix.get(x, y)) Color.BLACK else Color.WHITE
                }
            }
            val bitmap = Bitmap.createBitmap(
                binding.imageView4.width,
                binding.imageView4.height,
                Bitmap.Config.ARGB_8888
            )
            bitmap.setPixels(
                pixels, 0,
                binding.imageView4.width,
                0, 0,
                binding.imageView4.width,
                binding.imageView4.height
            )

        }

        binding.addPhoto.setOnClickListener{
            val pickImg = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI)
            val takePicture = Intent(MediaStore.ACTION_IMAGE_CAPTURE)

            val chooser = Intent.createChooser(pickImg, "Select Image")
            chooser.putExtra(Intent.EXTRA_ALTERNATE_INTENTS, arrayOf(takePicture))

            imagePicker.launch(chooser)
        }
    }

    private fun showImagePickerDialog() {
        AlertDialog.Builder(this).setTitle("Выберите опцию")
            .setItems(arrayOf("Сделать фото", "Выбрать из галереи", "Отмена")) { _, which ->
                when (which) {
                    0 -> startActivityForResult(Intent(MediaStore.ACTION_IMAGE_CAPTURE), REQUEST_IMAGE_CAPTURE)
                    1 -> startActivityForResult(Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI).apply { type = "image/*" }, REQUEST_IMAGE_PICK)
                }
            }.show()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                REQUEST_IMAGE_CAPTURE -> binding.imageView4.setImageBitmap(data?.extras?.get("data") as Bitmap)
                REQUEST_IMAGE_PICK -> binding.imageView4.setImageURI(data?.data)
            }
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
                createFileFromStream(
                    ins!!,
                    destinationFileName
                )
            }
        } catch (e: Exception){
            Log.e("Save File", e.message!!)
            e.printStackTrace()
        }
        return destinationFileName
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
            Log.e("Save File", ex.message.toString())
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