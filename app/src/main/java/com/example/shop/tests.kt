package com.example.shop

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.shop.databinding.ActivityEditProfileBinding
import com.google.zxing.BarcodeFormat
import com.google.zxing.oned.Code128Writer
import kotlinx.coroutines.launch
import java.io.File

class tests: AppCompatActivity() {

    private lateinit var binding: ActivityEditProfileBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        lifecycleScope.launch {
            val text = "qwertyuio"
            val bitMatrix = Code128Writer().encode(
                text, BarcodeFormat.CODE_128,
                binding.imageView4.width,
                binding.imageView4.height
            )
            val pixels = IntArray(binding.imageView4.width * binding.imageView4.height)
            for (y in 0 until bitMatrix.height){
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
            binding.imageView4.setImageBitmap(bitmap)
        }



        binding.imageView4.setOnClickListener {
            val pickImg = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI)
            val takePicture = Intent(MediaStore.ACTION_IMAGE_CAPTURE)

            val choser = Intent.createChooser(pickImg, "Chose Image")
            choser.putExtra(Intent.EXTRA_ALTERNATE_INTENTS, arrayOf(takePicture)

            )
        }
    }

    private fun showImagePickerDialog(){
        AlertDialog.Builder(this).setTitle("Выберите опцию")
            .setItems(arrayOf("Сделать фото", "Сделать фото", "Сделать фото")) { which , _ ->
                when(which){
                    0 -> startActivityForResult(Intent(MediaStore.ACTION_IMAGE_CAPTURE), REQUEST_IMAGE_CAPTURE)
                    1 -> startActivityForResult(Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI).apply { type = "image/*" }, REQUEST_IMAGE_PICK)

                }
            }.show()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK){
            when(requestCode){
                REQUS -> binding.imageView4.setImageBitmap(data?.extras?.get("data") as Bitmap)
                dfsdfsdd -> binding.imageView4.setImageURI(data?.data)
            }
        }
    }

    val imagePicker =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK){
                val data = result.data
                val imageUri = data?.data
                if (imageUri != null){

                }
            }
        }

    private fun getFile(context: Context, uri: Uri){
        val destinationFileName =
            File(context.filesDir.path + File.separatorChar + )
    }
}