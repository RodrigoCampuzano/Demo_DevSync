package com.m1guelgtz.templatecarsapi.Demo.Core.Network

import android.util.Log
import com.google.gson.Gson
import com.m1guelgtz.templatecarsapi.Demo.Core.Data.Local.TaskDao
import com.m1guelgtz.templatecarsapi.Demo.Core.UpdateEventBus
import com.m1guelgtz.templatecarsapi.Demo.Core.Di.BasicClient
import kotlinx.coroutines.*
import okhttp3.*
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class WebSocketManager @Inject constructor(
    @BasicClient private val client: OkHttpClient,
    private val taskDao: TaskDao,
    private val gson: Gson,
    private val updateEventBus: UpdateEventBus
) {
    private var webSocket: WebSocket? = null
    private val scope = CoroutineScope(Dispatchers.IO + SupervisorJob())
    private var currentUrl: String? = null
    private var isConnecting = false

    fun connect(url: String) {
        if (webSocket != null || isConnecting) return
        isConnecting = true
        currentUrl = url
        val request = Request.Builder().url(url).build()
        webSocket = client.newWebSocket(request, object : WebSocketListener() {
            override fun onOpen(webSocket: WebSocket, response: Response) {
                isConnecting = false
                Log.i("WS_DEBUG", "CONNECTION OPENED SUCCESSFULLY")
            }
            override fun onMessage(webSocket: WebSocket, text: String) {
                Log.i("WS_DEBUG", "RECEIVED MESSAGE: $text")
                scope.launch { handleSocketMessage(text) }
            }
            override fun onFailure(webSocket: WebSocket, t: Throwable, response: Response?) {
                isConnecting = false
                this@WebSocketManager.webSocket = null
                scope.launch {
                    delay(2000)
                    currentUrl?.let { connect(it) }
                }
            }
            override fun onClosed(webSocket: WebSocket, code: Int, reason: String) {
                isConnecting = false
                this@WebSocketManager.webSocket = null
            }
        })
    }

    fun sendUpdate(taskId: String, status: String, updatedBy: String) {
        val message = SocketUpdateMessage(
            type = "task_update",
            payload = TaskUpdatePayload(taskId, status, updatedBy)
        )
        val json = gson.toJson(message)
        Log.i("WS_DEBUG", "SENDING TO SOCKET: $json")
        webSocket?.send(json)
    }

    private suspend fun handleSocketMessage(text: String) {
        try {
            val message = gson.fromJson(text, SocketUpdateMessage::class.java)
            if (message.type == "task_update" && message.payload != null) {
                val payload = message.payload
                taskDao.updateTaskStatusByAnyId(payload.taskId, payload.status)
            }
            updateEventBus.emitUpdate()
        } catch (e: Exception) {
            updateEventBus.emitUpdate()
        }
    }

    fun disconnect() {
        currentUrl = null
        webSocket?.close(1000, "User logout")
        webSocket = null
    }
}

data class SocketUpdateMessage(
    val type: String?,
    val payload: TaskUpdatePayload?
)

data class TaskUpdatePayload(
    @com.google.gson.annotations.SerializedName("task_id") val taskId: String,
    val status: String,
    @com.google.gson.annotations.SerializedName("updated_by") val updatedBy: String
)
