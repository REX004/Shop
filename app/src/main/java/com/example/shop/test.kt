package com.example.shop

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.os.StrictMode
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.shop.databinding.ActivityEditProfileBinding
import com.google.gson.Gson
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody
import org.json.JSONArray
import org.json.JSONObject
import java.io.IOException
import java.util.concurrent.TimeUnit
import kotlin.io.encoding.Base64
import kotlin.io.encoding.ExperimentalEncodingApi

    @SuppressLint("StaticFieldLeak")
    private lateinit var binding: ActivityEditProfileBinding
class test : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val policy = StrictMode.ThreadPolicy.Builder().permitAll().build()
        StrictMode.setThreadPolicy(policy)
        val text2ImageAPI = Text2ImageAPI(
            "https://api-key.fusionbrain.ai/",
            "D2819CEFB02E58AB63B1E773B634CA94",
            "9AD8A3BC1814DD3B70A09BB94672CB0F "
        )
        val model = text2ImageAPI.getModel()
        val generated = text2ImageAPI.generate("негр", model!!)
        var check  = text2ImageAPI.checkGeneration(generated!!, context = this)
        println(check)
//        println(generated);
//        while (check!!.status != "DONE") {
//            println("while")
//            check = text2ImageAPI.checkGeneration(generated!!)
//        }
//        println("DONE:  $check")

//        var glideUrl  = GlideUrl("https://fusionbrain.ai/${check!![0]}", LazyHeaders.Builder()
//            .addHeader("X-Key", "Key D2819CEFB02E58AB63B1E773B634CA94")
//            .addHeader("X-Secret", "Secret 9AD8A3BC1814DD3B70A09BB94672CB0F").build()
//        )
//
//        Glide.with(this).load(glideUrl).into(binding.imageView4)
    }

    class Text2ImageAPI(private val url: String, apiKey: String, secretKey: String) {

        private val authHeaders = mapOf(
            "X-Key" to "Key $apiKey",
            "X-Secret" to "Secret $secretKey"
        )

        private val client = OkHttpClient()

        fun getModel(): String? {
            val request = Request.Builder()
                .url("${url}key/api/v1/models")
                .apply {
                    authHeaders.forEach { (key, value) ->
                        addHeader(key, value)
                    }
                }
                .build()

            client.newCall(request).execute().use { response ->
                if (!response.isSuccessful) throw IOException("Unexpected code $response")
                val data = JSONArray(response.body?.string() ?: "")
                return data.getJSONObject(0).getString("id")
            }
        }

        fun generate(
            query: String,
            model: String,
            images: Int = 1,
            width: Int = 1024,
            height: Int = 1024
        ): String {
            val params = JSONObject().apply {
                put("type", "GENERATE")
                put("numImages", images)
                put("width", width)
                put("height", height)
                put("generateParams", JSONObject().put("query", query))
            }

            val body = MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("model_id", model)
                .addFormDataPart(
                    "params",
                    params.toString(),
                    RequestBody.create(
                        "application/json; charset=utf-8".toMediaType(),
                        params.toString()
                    )
                )
                .build()

            val request = Request.Builder()
                .url("${url}key/api/v1/text2image/run")
                .apply {
                    authHeaders.forEach { (key, value) ->
                        addHeader(key, value)
                    }
                }
                .post(body)
                .build()

            client.newCall(request).execute().use { response ->
                if (!response.isSuccessful) throw IOException("Unexpected code $response")
                var data = response.body!!.string()
                var model = Gson().fromJson(data, Model::class.java)
                println(model)
                println(data)
//                var dataBytes = data.bytes()
//                println(dataBytes)
//                var bitmap = BitmapFactory.decodeByteArray(data.bytes(), 0, data.bytes().size)
//                println(bitmap)
                return model.uuid
            }
        }

        @OptIn(ExperimentalEncodingApi::class)
        fun checkGeneration(
            requestId: String,
            attempts: Int = 20,
            delay: Long = 10,
            context: Context
        ): Image? {
            try {

                repeat(attempts) {
                    val request = Request.Builder()
                        .url("${url}key/api/v1/text2image/status/$requestId")
                        .apply {
                            authHeaders.forEach { (key, value) ->
                                addHeader(key, value)
                            }
                        }
                        .build()
                    client.newCall(request).execute().use { response ->
                        if (!response.isSuccessful) throw IOException("Unexpected code $response")
                        val image = Gson().fromJson(response.body!!.string(), Image::class.java)
                        println(image.status)
                        if (image.status == "DONE") {
                            println(image.images)
                            var imageByteArray = Base64.decode(image.images!!.first())
                            Glide.with(context).load(imageByteArray).into(binding.imageView4)
                        }
                        image
                    }

                    TimeUnit.SECONDS.sleep(delay)
                }
                return null
            } catch (e: Exception) {
                println("laskjflsdaj")
                return  null
            }
        }
    }
}

data class Model(val status: String, val uuid: String)

data class Image(val uuid: String, val status: String, val censored: Boolean, val images: List<String>? = null)