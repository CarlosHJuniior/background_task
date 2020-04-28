package com.lince.backgroundtask

import android.content.Context
import android.os.StrictMode
import androidx.work.Worker
import androidx.work.WorkerParameters
import java.net.HttpURLConnection
import java.net.URL

class CustomWorker(
        context: Context,
        workerParameters: WorkerParameters
) : Worker(context, workerParameters) {
    
    override fun doWork(): Result {
        val url = URL("http://192.168.1.4:3000/")
        with(url.openConnection() as HttpURLConnection) {
            requestMethod = "GET"
            StrictMode.setThreadPolicy(
                    StrictMode.ThreadPolicy
                            .Builder()
                            .permitAll()
                            .build()
            )
            
            println(responseCode)
            println(responseMessage)
        }
        
        return Result.success()
    }
}