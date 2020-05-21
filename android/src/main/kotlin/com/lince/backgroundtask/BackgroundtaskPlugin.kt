package com.lince.backgroundtask

import android.content.Context
import android.util.Log
import androidx.annotation.NonNull
import androidx.work.*
import io.flutter.embedding.engine.plugins.FlutterPlugin
import io.flutter.plugin.common.MethodCall
import io.flutter.plugin.common.MethodChannel
import java.util.concurrent.Executor
import java.util.concurrent.TimeUnit

/** BackgroundtaskPlugin */
open class BackgroundtaskPlugin : FlutterPlugin, MethodChannel.MethodCallHandler {
    private val channel = "lince.com/backgroundtask"
    private val method = "periodic"
    private lateinit var context: Context
    
    override fun onAttachedToEngine(@NonNull flutterPluginBinding: FlutterPlugin.FlutterPluginBinding) {
        val channel = MethodChannel(flutterPluginBinding.binaryMessenger, channel)
        context = flutterPluginBinding.applicationContext
        channel.setMethodCallHandler(this)
    }
    
    override fun onMethodCall(call: MethodCall, result: MethodChannel.Result) {
        if (call.method == method) {
            try {
                val array = ArrayList<Any?>()
                array.addAll(call.arguments())
                
                val config = WorkerConfiguration(array[1] as Map<String, Any>)
                
                result.success(startService(array[0] as Long, config))
            } catch (e: Exception) {
                Log.d("periodic", "erro ao executar o serviço >> $e")
                result.success(false)
            }
        }
    }
    
    private fun startService(handle: Long, config: WorkerConfiguration): Boolean {
        return try {
            val data = Data.Builder()
            data.putLong("handle", handle)
            
            val pWorkRequest = PeriodicWorkRequest
                .Builder(CustomWorker::class.java, config.interval.toLong(), TimeUnit.SECONDS)
                .setInitialDelay(10, TimeUnit.SECONDS)
                .setInputData(data.build())
                .setConstraints(config.buildConstraints())
                .build()
            
            WorkManager.getInstance(context).enqueueUniquePeriodicWork(
                "background_tast",
                ExistingPeriodicWorkPolicy.REPLACE,
                pWorkRequest
            )
            
            true
        } catch (e: Exception) {
            println(e)
            false
        }
    }
    
    override fun onDetachedFromEngine(@NonNull binding: FlutterPlugin.FlutterPluginBinding) {
    }
}

data class WorkerConfiguration(val map: Map<String, Any>) {
    val interval: Number = map["interval"] as Number
    private var constraints: Set<Int>? = null
    private var networkType: String? = null
    
    init {
        val list = map["constraints"]
        if (list != null) {
            constraints = (list as List<Int>).toHashSet()
        }
        
        val network = map["networkType"]
        if (network != null) {
            networkType = (network as String).replace("NetworkType.", "")
        }
    }
    
    fun buildConstraints(): Constraints {
        val builder = Constraints.Builder()
        
        if (constraints != null) {
            builder.setRequiresBatteryNotLow(constraints!!.contains(0))
                .setRequiresCharging(constraints!!.contains(1))
                .setRequiresDeviceIdle(constraints!!.contains(2))
                .setRequiresStorageNotLow(constraints!!.contains(3))
        }
        
        if (networkType != null) {
            builder.setRequiredNetworkType(NetworkType.valueOf(networkType!!))
        }
        
        return builder.build()
    }
}