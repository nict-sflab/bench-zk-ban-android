package com.akakou.zkbanandroid.bench

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import crypto.Crypto.benchmark
import kotlinx.coroutines.*

import kotlin.concurrent.thread

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        if (!Environment.isExternalStorageManager()) {
            intent = Intent(
                ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION,
                Uri.parse("package:${this.packageName}")
            )
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
            this.startActivity(intent)
        }

        val basePath = Environment.getExternalStorageDirectory()
        val path = basePath.path + "/zk-ban/"

        Log.d("zk-ban-bench-path", path)
//        Toast.makeText(this, path, Toast.LENGTH_LONG).show()

        val sharedPref = this.getSharedPreferences("DEFAULT", Context.MODE_PRIVATE)
        val previous = sharedPref.getString("result", "")

        Log.d("zk-ban-bench", "$previous")

        thread {
            val result = benchmark(path)!!

            GlobalScope.launch(Dispatchers.Main) {
                val edit = sharedPref.edit()
                edit.putString("result", result).apply()
            }
        }
        setContent {
            Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                Greeting(
                    name = "Android",
                    modifier = Modifier.padding(innerPadding)
                )
            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
}