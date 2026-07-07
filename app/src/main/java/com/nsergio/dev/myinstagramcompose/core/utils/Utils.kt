package com.nsergio.dev.myinstagramcompose.core.utils


object Utils {

    fun generateRandomString(): String {
        return generateRandomFloat().toString()
    }

    fun generateRandomFloat(): Long {
        return System.currentTimeMillis()
    }

}
