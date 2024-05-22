package com.example.shop

import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Base64
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.shop.databinding.ActivityPhotoGeneratorBinding
import okhttp3.Call
import okhttp3.Callback
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody
import okhttp3.Response
import org.json.JSONObject
import java.io.IOException

class PhotoGeneratorActivity : AppCompatActivity() {
    private lateinit var binding: ActivityPhotoGeneratorBinding
    private val apiKey = "9E8B760E2009BFDE94374F89E7DE9D4F"
    private val secretKey = "8098060EE5DD3AC0E9C9C8DFABBDAB66"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPhotoGeneratorBinding.inflate(layoutInflater)
        setContentView(binding.root)


        binding.generateButton.setOnClickListener {
            val firstName = binding.firstNameEditText.text.toString()
            val lastName = binding.firstNameEditText.text.toString()
            generateImage(firstName, lastName)
        }
    }

    private fun generateImage(firstName: String, lastName: String) {
        val client = OkHttpClient()

        // Construct the request body
        val requestBody = JSONObject()
        requestBody.put("type", "GENERATE")
        requestBody.put("numImages", 1)
        requestBody.put("width", 1024)
        requestBody.put("height", 1024)
        requestBody.put("generateParams", JSONObject().put("query", "$firstName $lastName"))

        // Create the request
        val request = Request.Builder()
            .url("https://8098060EE5DD3AC0E9C9C8DFABBDAB66.fusionbrain.ai/key/api/v1/text2image/run")
            .post(RequestBody.create("application/json".toMediaTypeOrNull(), requestBody.toString()))
            .addHeader("X-Key", "Key $apiKey")
            .addHeader("X-Secret", "Secret $secretKey")
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                Log.e("PhotoGeneratorActivity", "Error: ${e.message}")
            }

            override fun onResponse(call: Call, response: Response) {
                response.use {
                    if (!it.isSuccessful) {
                        throw IOException("Unexpected code $response")
                    }

                    val responseBody = it.body?.string()
                    val responseJson = JSONObject(responseBody!!)
                    val taskId = responseJson.getString("uuid")

                    val imageRequest = Request.Builder()
                        .url("https://8098060EE5DD3AC0E9C9C8DFABBDAB66.fusionbrain.ai/key/api/v1/text2image/status/$taskId")
                        .build()

                    client.newCall(imageRequest).enqueue(object : Callback {
                        override fun onFailure(call: Call, e: IOException) {
                            Log.e("PhotoGeneratorActivity", "Error getting image: ${e.message}")
                        }

                        override fun onResponse(call: Call, response: Response) {
                            response.use {
                                if (it.isSuccessful) {
                                    val imageResponse = JSONObject(it.body!!.string())
                                    val imageBase64 = imageResponse.getJSONArray("images").getString(0)
                                    val imageBytes = Base64.decode(imageBase64, Base64.DEFAULT)
                                    val bitmap = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.size)
                                    runOnUiThread {
                                        binding.imageView.setImageBitmap(bitmap)
                                    }
                                }
                            }
                        }
                    })
                }
            }
        })
    }
}
