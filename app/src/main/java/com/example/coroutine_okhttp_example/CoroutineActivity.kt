package com.example.coroutine_okhttp_example

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import okhttp3.*
import java.io.IOException

class CoroutineActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        coroutine()

    }

    fun coroutine(){

        //비동기 : async , 동기 : launch
        CoroutineScope(Dispatchers.Main).launch {

            val html = CoroutineScope(Dispatchers.Default).async {
                //Default : network를 위한 쓰레드
                //network 작업들
                getHtml()
            }.await()

            val mTextView = findViewById<TextView>(R.id.mTextMain)
            mTextView.text = html
            //메인 쓰레드영역
        }
    }
    fun getHtml(): String {
        // okhttp의 큰 흐름
        //1. 클라이언트 만들기
        val client = OkHttpClient.Builder().build()
        //2. 요청
        val req = Request.Builder().url("https://www.google.com" ).build()
        //3. 응답 (excute():동기, enqueue():비동기)
        client.newCall(req).execute().use {
            response -> return if(response.body != null){
                response.body!!.toString()
           }
            else {
                "body null"
            }
        }
    }


}
