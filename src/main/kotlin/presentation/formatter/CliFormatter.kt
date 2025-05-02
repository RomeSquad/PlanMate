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
        omitRightSideBorder: Boolean = false,
        omitLeftSideBorder: Boolean = false,
        omitTopBorder: Boolean = false,
        omitBottomBorder: Boolean = false
    ): String {
        val output = StringBuilder()

        if (!omitTopBorder) output.append(topBorder(width, sidePaddingSize, topBottomPaddingSize))

        val messageLines = message.split("\n")
        for (index in 0 until height) {
            if (!omitLeftSideBorder) output.append(SIDE_BORDER)
            output.append(messageLines.get(index).take(width))
            if (!omitRightSideBorder) output.appendLine(SIDE_BORDER)
        }

        if (!omitBottomBorder) output.append(bottomBorder(width, sidePaddingSize, topBottomPaddingSize))

        return output.toString()
    }

    private fun topBorder(width: Int, sidePaddingSize: Int, topBottomPaddingSize: Int): String {
        val topBottomBorder = StringBuilder()
        topBottomBorder.append(TOP_BOTTOM_BORDER.repeat(width + sidePaddingSize * 2))
        for (topPaddingIndex in 0 until topBottomPaddingSize) {
            topBottomBorder.appendLine()
        }
        return topBottomBorder.toString()
    }

    private fun bottomBorder(width: Int, sidePaddingSize: Int, topBottomPaddingSize: Int): String {
        val topBottomBorder = StringBuilder()
        for (botPaddingIndex in 0 until topBottomPaddingSize) {
            topBottomBorder.appendLine()
        }
        topBottomBorder.append(TOP_BOTTOM_BORDER.repeat(width + sidePaddingSize * 2))
        return topBottomBorder.toString()
    }

    fun verticalLayout(messages: List<String>, width: Int = 20, height: Int = 1): String {
        val output = StringBuilder()
        messages.map { output.appendLine(rectangleLayout(it)) }
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