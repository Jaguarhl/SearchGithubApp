package com.kartsev.dmitry.searchgithubrepos.util

import kotlinx.coroutines.CoroutineDispatcher
import java.util.concurrent.Executor
import kotlin.coroutines.EmptyCoroutineContext

class DispatcherExecutor(val dispatcher : CoroutineDispatcher) : Executor {
    override fun execute(command: java.lang.Runnable) {
        dispatcher.dispatch(EmptyCoroutineContext, command)
    }
}