package org.example.presentation.formatter

import org.koin.core.logger.MESSAGE
import javax.swing.border.Border


class CliFormatter : Formatter {

    /**
     * If the message is longer than the width or the height, it will be truncated.
     */
    fun rectangleLayout(
        message: String,
        width: Int = 50,
        height: Int = 5,
        sidePaddingSize: Int = 1,
        topBottomPaddingSize: Int = 1,
    ): String {
        val messageLines = message.split("\n")
        val output = StringBuilder()
        output.append(topBottomBorder(width + sidePaddingSize * 2))
        for (index in 0 until height) {
            output.append(SIDE_BORDER)
            output.append(messageLines.get(index).take(width))
            output.appendLine(SIDE_BORDER)
        }

        output.append(topBottomBorder(width + sidePaddingSize * 2))
        return output.toString()
    }

    private fun topBottomBorder(width: Int): String {
        return " " + TOP_BOTTOM_BORDER.repeat(width)
    }

    fun verticalLayout(messages: List<String>, width: Int = 20, height: Int = 1): String {
        val output = StringBuilder()
        messages.map { output.append(rectangleLayout(it)) }
        return output.toString()
    }

    fun horizontalLayout(messages: List<String>, width: Int = 20, height: Int = 1): String {
        val output = StringBuilder()
        messages.map { output.appendLine(rectangleLayout(it)) }
        return output.toString()
    }


    companion object {
        private val TOP_BOTTOM_BORDER = "-"
        private val SIDE_BORDER = "|"
    }
}