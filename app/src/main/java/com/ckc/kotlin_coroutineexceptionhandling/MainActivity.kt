package com.ckc.kotlin_coroutineexceptionhandling

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        //Hatayı onlemek için tray catch mantıklı bir yöntem değildir.Bir sürü lunch olduğunda patlar

        lifecycleScope.launch {

            try {
                launch {
                    throw Exception("error")
                }
            } catch (e: Exception) {
                print(e.stackTrace)
            }
        }
        lifecycleScope.launch {

            launch {
                try {
                    throw Exception("error")
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }


        //CoroutineExceptionHandler ise hata olsa bile hata olmayan kısmı çalıştırır hata olanı çalıştırmaz
        val handler = CoroutineExceptionHandler {context, throwable ->
            println("exception: " + throwable)
        }

        lifecycleScope.launch(handler) {

            launch {
                throw Exception("error")
            }
            launch {
                delay(500L)
                println("this is executed")
            }
        }

        lifecycleScope.launch(handler) {


            supervisorScope {
                launch {
                    throw Exception("error")
                }
                launch {
                    delay(500L)
                    println("this is executed")
                }
            }
        }



        CoroutineScope(Dispatchers.Main + handler).launch {
            launch {
                throw Exception("error in a coroutine scope")
            }
        }

    }
}