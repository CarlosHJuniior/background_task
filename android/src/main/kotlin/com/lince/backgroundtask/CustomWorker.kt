package com.lince.backgroundtask

import android.content.Context
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.work.WorkInfo
import androidx.work.WorkManager
import androidx.work.Worker
import androidx.work.WorkerParameters
import io.flutter.embedding.engine.FlutterEngine
import io.flutter.embedding.engine.dart.DartExecutor
import io.flutter.view.FlutterCallbackInformation
import io.flutter.view.FlutterMain
import kotlin.math.log

class CustomWorker(
        context: Context,
        workerParameters: WorkerParameters
) : Worker(context, workerParameters) {

    override fun doWork(): Result {
        val workManager =  WorkManager.getInstance(this.applicationContext)
        val workInfoList = workManager.getWorkInfoById(this.id).get()
        
        if (workInfoList.state == WorkInfo.State.CANCELLED) {
            Log.d("periodic", "cancelado")
            return Result.success()
        }
        
        Handler(Looper.getMainLooper()).post {
            Log.d("periodic", "background service start")

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
            val bundle = FlutterMain.findAppBundlePath()
            val executor = flutterEngine.dartExecutor
            val dartCallback = DartExecutor.DartCallback(assets, bundle, flutterCallback)
            
            executor.executeDartCallback(dartCallback)

        } catch (e: Exception) {
            Log.e("periodic", "erro desconhecido >> $e")
        }
    }
}