package com.example.apitest
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.apitest.databinding.ActivityMainBinding
import okhttp3.*
import org.json.JSONObject
import java.io.IOException
import java.io.InputStreamReader

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private var client: OkHttpClient? = null
    private val getURL : String = "https://www.boredapi.com/api/activity/"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        client = OkHttpClient()

        binding.button.setOnClickListener {
            getData()
        }
    }
        private fun getData() {
            val request = Request.Builder()
                .url(getURL)
                .build()

            client!!.newCall(request).enqueue(object : Callback {
                override fun onFailure(call: Call, e: IOException) {
                    e.printStackTrace()
                }

                override fun onResponse(call: Call, response: Response) {
                    response.use {
                        if (!response.isSuccessful) throw IOException("unexpected code $response")
                        val jsonData: ResponseBody? = response.body

                        val result = JSONObject(jsonData?.string())
                        val res = result.toString()

                        val jsonObject = JSONObject(res)
                        val activity = jsonObject.optString("activity")
                        val type = jsonObject.optString("type")
                        val participants = jsonObject.optInt("participants")
                        val price = jsonObject.optDouble("price")
                        val key = jsonObject.optInt("key")

                        //setting data to the ui thread
                        runOnUiThread {
                            binding.textActivity.text = activity
                            binding.textType.text = type
                            binding.textParticipants.text = participants.toString()
                            binding.textPrice.text = price.toString()
                            binding.textKey.text = key.toString()
                        }
                    }
                }
            })
        }
}