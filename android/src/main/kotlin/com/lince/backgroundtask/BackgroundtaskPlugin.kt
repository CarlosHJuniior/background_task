package com.lince.backgroundtask

import android.content.Context
import android.os.Build
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi
import androidx.work.Constraints
import androidx.work.NetworkType
import androidx.work.PeriodicWorkRequest
import androidx.work.WorkManager
import io.flutter.embedding.engine.plugins.FlutterPlugin
import io.flutter.plugin.common.MethodChannel
import java.time.Duration
import java.util.concurrent.TimeUnit

/** BackgroundtaskPlugin */
open class BackgroundtaskPlugin : FlutterPlugin {
    private val channel = "lince.com/backgroundtask"
    private val method = "periodic"

    override fun onAttachedToEngine(
            @NonNull flutterPluginBinding: FlutterPlugin.FlutterPluginBinding) {
        val channel = MethodChannel(flutterPluginBinding.binaryMessenger, channel)
        channel.setMethodCallHandler { call, result ->
            if (call.method == method) {
                startService(flutterPluginBinding.applicationContext)
                result.success(null)
            }
        }
    }
  
    private fun startService(context: Context) {
        println("startService")
        try{
            val constrains = Constraints.Builder()
                    .setRequiredNetworkType(NetworkType.CONNECTED)
                    .build()
    
            val pWorkRequest = PeriodicWorkRequest
                    .Builder(CustomWorker::class.java, 15, TimeUnit.MINUTES)
                    .setConstraints(constrains)
                    .build()
    
            WorkManager.getInstance(context).enqueue(pWorkRequest)
        } catch (e: Exception) {
            println(e)
        }
    }

    override fun onDetachedFromEngine(@NonNull binding: FlutterPlugin.FlutterPluginBinding) {
    }
}
