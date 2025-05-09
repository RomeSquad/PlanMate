package org.example.presentation.utils.io

import presentation.io.InputReader

class ConsoleInputReader : InputReader {
    override fun readString(string: String): String = readlnOrNull()?:throw IllegalArgumentException("Input cannot be null")

    override fun readIntOrNull(string: String, ints: IntRange): Int? = readlnOrNull()?.toIntOrNull()

    override fun readDoubleOrNull(): Double? = readlnOrNull()?.toDoubleOrNull()
}