package org.example.presentation.utils.io

interface InputReader {
    fun readString(string: String): String
    fun readIntOrNull(string: String, ints: IntRange): Int?
    fun readDoubleOrNull(): Double?
}