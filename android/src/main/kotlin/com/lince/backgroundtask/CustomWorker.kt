package com.lince.backgroundtask

import android.content.Context
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.work.Worker
import androidx.work.WorkerParameters
import io.flutter.embedding.engine.FlutterEngine
import io.flutter.embedding.engine.FlutterJNI
import io.flutter.embedding.engine.dart.DartExecutor
import io.flutter.embedding.engine.loader.FlutterLoader
import io.flutter.view.FlutterCallbackInformation
import io.flutter.view.FlutterMain
import kotlin.coroutines.coroutineContext

class CustomWorker(
        context: Context,
        workerParameters: WorkerParameters
) : Worker(context, workerParameters) {

    override fun doWork(): Result {
        Handler(Looper.getMainLooper()).post {
            Log.d("periodic", "background service start")
//
            val handle = inputData.getLong("handle", 0)
            startDartTask(applicationContext, handle)
            Log.d("periodic", "background service end")
        }
        
        return Result.success()
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
}