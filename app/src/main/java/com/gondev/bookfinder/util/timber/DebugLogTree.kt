package com.gondev.bookfinder.util.timber

import timber.log.Timber

class DebugLogTree : Timber.DebugTree() {
    override fun createStackElementTag(element: StackTraceElement) =
        "Timber: (${element.fileName}:${element.lineNumber})"
}