package com.twisthenry8gmail.projectbarry.core

open class Event<T>(private val content: T) {

    private var consumed = false

    fun get() = content

    fun getIfNotHandled(): T? {

        return if (consumed) null else {

            consumed = true
            content
        }
    }

    class Observer<T>(val onChanged: (T) -> Unit) : androidx.lifecycle.Observer<Event<T>> {

        override fun onChanged(t: Event<T>?) {

            t?.getIfNotHandled()?.let { onChanged(it) }
        }
    }
}

class Trigger : Event<Unit>(Unit)