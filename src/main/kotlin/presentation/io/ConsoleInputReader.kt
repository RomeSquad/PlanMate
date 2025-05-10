package presentation.io

import org.example.presentation.io.InputReader

class ConsoleInputReader : InputReader {

    override fun readString(string: String): String =
        readlnOrNull() ?: throw IllegalArgumentException("Input cannot be null")

    override fun readIntOrNull(string: String, ints: IntRange): Int? =
        readlnOrNull()?.toIntOrNull()?.takeIf { it in ints }

    override fun readDoubleOrNull(): Double? = readlnOrNull()?.toDoubleOrNull()
}