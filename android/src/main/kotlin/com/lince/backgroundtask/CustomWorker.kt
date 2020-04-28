package com.lince.backgroundtask

import android.content.Context
import android.util.Log
import androidx.work.Worker
import androidx.work.WorkerParameters
import io.flutter.embedding.engine.FlutterEngine
import io.flutter.embedding.engine.FlutterJNI
import io.flutter.embedding.engine.dart.DartExecutor
import io.flutter.embedding.engine.loader.FlutterLoader
import io.flutter.view.FlutterCallbackInformation
import io.flutter.view.FlutterMain

class CustomWorker(
        context: Context,
        workerParameters: WorkerParameters
) : Worker(context, workerParameters) {

    override fun doWork(): Result {
        Log.d("periodic", "background service start")

        val handle = inputData.getLong("handle", 0)
        startDartTask(applicationContext, handle)

        Log.d("periodic", "background service end")
        return Result.success()
    }

    private fun startDartTask(context: Context, handle: Long) {
        try {
            val flutterEngine = FlutterEngine(context)
            Log.d("periodic", "flutter engine")
            val flutterCallback = FlutterCallbackInformation.lookupCallbackInformation(handle)
            Log.d("periodic", "flutter callback")
            if (flutterCallback.callbackName == null) {
                Log.e("periodic", "callback not found")
                return
            }

            val executor = flutterEngine.dartExecutor
            Log.d("periodic", "dart executor")
            val dartCallback = DartExecutor.DartCallback(context.assets, FlutterMain.findAppBundlePath(), flutterCallback)
            Log.d("periodic", "dart callback")
            executor.executeDartCallback(dartCallback)
            Log.d("periodic", "dart execute")
        } catch (e: Exception) {
            Log.e("periodic", "erro desconhecido >> $e")
        }
    }
}