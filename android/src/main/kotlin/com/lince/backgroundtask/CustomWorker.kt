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
        val url = URL("http://10.0.11.68:3000/moeda/get")
        with(url.openConnection() as HttpURLConnection) {
            requestMethod = "GET"
            StrictMode.setThreadPolicy(
                    StrictMode.ThreadPolicy
                            .Builder()
                            .permitAll()
                            .build()
            )
            
            println(responseCode)
        }
        
        return Result.success()
    }
}