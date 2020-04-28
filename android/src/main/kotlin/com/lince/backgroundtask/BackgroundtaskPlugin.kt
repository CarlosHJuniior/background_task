package com.lince.backgroundtask

import android.content.Context
import android.util.Log
import androidx.annotation.NonNull;
import androidx.work.*
import io.flutter.embedding.engine.FlutterEngine
import io.flutter.embedding.engine.dart.DartExecutor
import io.flutter.embedding.engine.plugins.FlutterPlugin
import io.flutter.plugin.common.MethodCall
import io.flutter.plugin.common.MethodChannel
import io.flutter.view.FlutterCallbackInformation
import io.flutter.view.FlutterMain
import org.json.JSONArray
import org.json.JSONObject
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

                result.success(startService(JSONArray(array)))
            } catch (e: Exception) {
                Log.d("periodic", "erro ao executar o serviço >> $e")
                result.success(false)
            }
        }
    }

    private fun startService(param: JSONArray): Boolean {
        return try {
            val data = Data.Builder()
            data.putLong("handle", param.getLong(0))
            
            val constrains = Constraints.Builder()
                    .setRequiredNetworkType(NetworkType.CONNECTED)
                    .build()

            val pWorkRequest = PeriodicWorkRequest
                    .Builder(CustomWorker::class.java, 15, TimeUnit.MINUTES)
                    .setInputData(data.build())
                    .setConstraints(constrains)
                    .build()

            WorkManager.getInstance(context).enqueue(pWorkRequest)
            
            true
        } catch (e: Exception) {
            println(e)
            false
        }
    }

    private fun startDartTask(context: Context, handle: Long) {
        try {
            val flutterEngine = FlutterEngine(context)
            val flutterCallback = FlutterCallbackInformation.lookupCallbackInformation(handle)
            if (flutterCallback.callbackName == null) {
                Log.e("periodic", "callback not found")
                return
            }

            val assets = context.assets
            Log.d("periodic", "assets")

            val bundle = FlutterMain.findAppBundlePath()
            Log.d("periodic", "bundle")

            val executor = flutterEngine.dartExecutor
            Log.d("periodic", "exec")

            val dartCallback = DartExecutor.DartCallback(assets, bundle, flutterCallback)
            Log.d("periodic", "callback")

            executor.executeDartCallback(dartCallback)
            Log.d("periodic", "fim")

        } catch (e: Exception) {
            Log.e("periodic", "erro desconhecido >> $e")
        }
    }

    override fun onDetachedFromEngine(@NonNull binding: FlutterPlugin.FlutterPluginBinding) {
    }
}
