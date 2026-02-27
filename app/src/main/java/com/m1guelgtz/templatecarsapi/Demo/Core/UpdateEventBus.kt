package com.m1guelgtz.templatecarsapi.Demo.Core

import android.util.Log
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UpdateEventBus @Inject constructor() {
    // replay = 1 ensures that the latest update signal is preserved
    private val _events = MutableSharedFlow<Unit>(replay = 1)
    val events = _events.asSharedFlow()

    fun emitUpdate() {
        Log.i("WS_DEBUG", "UpdateEventBus: Emitting update signal to all UI listeners")
        _events.tryEmit(Unit)
    }
}
