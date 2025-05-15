package org.example.presentation.utils.formatter


class CliFormatter {

    /**
     * If the message is longer than the width or the height, it will be truncated.
     */
    fun rectangleLayout(
        message: String,
        width: Int = 30,
        height: Int = 3,
        sidePaddingSize: Int = 1,
        topBottomPaddingSize: Int = 1,
        omitRightSideBorder: Boolean = false,
        omitLeftSideBorder: Boolean = false,
        omitTopBorder: Boolean = false,
        omitBottomBorder: Boolean = false
    ): String {
        val output = StringBuilder()
        if (width < 0) throw IllegalArgumentException("Width must be greater than 0")
        if (height < 0) throw IllegalArgumentException("Height must be greater than 0")
        if (!omitTopBorder) output.append(topBorder(width, sidePaddingSize, topBottomPaddingSize))

        val messageLines = message.split("\n")
        for (index in 0 until height) {
            if (index >= messageLines.size) {
                if (!omitLeftSideBorder) output.append(SIDE_BORDER)
                output.append(" ".repeat(width))
                if (!omitRightSideBorder) output.append(SIDE_BORDER)
                output.appendLine()
            } else {
                if (!omitLeftSideBorder) output.append(SIDE_BORDER)

                if (messageLines[index].length + sidePaddingSize * 2 > width) {
                    output.append(messageLines[index].take(width))
                } else {

                    val empty = ((width - messageLines[index].length - sidePaddingSize * 2) / 2) + 1

                    var emptySpace = " ".repeat(empty)

                    output.append(emptySpace)
                    output.append(messageLines[index])
                    if (Math.floorMod(messageLines[index].length, 2) != 0) output.append(" ")
                    output.append(emptySpace)
                }
                if (!omitRightSideBorder) output.append(SIDE_BORDER)
                output.appendLine()
            }
        }

        if (!omitBottomBorder) {
            output.append(
                bottomBorder(
                    width, sidePaddingSize, topBottomPaddingSize,
                    omitRightSideBorder, omitLeftSideBorder
                )
            )
        }

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

    private fun bottomBorder(
        width: Int, sidePaddingSize: Int, topBottomPaddingSize: Int, omitRightSideBorder: Boolean = false,
        omitLeftSideBorder: Boolean = false,
    ): String {
        val topBottomBorder = StringBuilder()
        for (botPaddingIndex in 0 until topBottomPaddingSize) {
            if (!omitLeftSideBorder) topBottomBorder.append(SIDE_BORDER)
            topBottomBorder.append(" ".repeat(width))
            if (!omitRightSideBorder) topBottomBorder.append(SIDE_BORDER)
            topBottomBorder.appendLine()
        }
        topBottomBorder.append(TOP_BOTTOM_BORDER.repeat(width + sidePaddingSize * 2))
        return topBottomBorder.toString()
    }

    fun verticalLayout(messages: List<String>, width: Int = 30, height: Int = 3): String {
        val output = StringBuilder()
        messages.map {
            output.appendLine(
                rectangleLayout(
                    message = it,
                    width = width,
                    height = height,
                    omitBottomBorder = true
                )
            )
        }
        return output.toString()
    }

    fun horizontalLayout(messages: List<String>, width: Int = 30, height: Int = 3): String {
        val output = StringBuilder()
        // nested for loop to create a horizontal layout using the rectangleLayout method
        val recMessages = messages.map {
            rectangleLayout(
                message = it,
                width = width,
                height = height,
                omitBottomBorder = true,
                omitRightSideBorder = true
            )
        }
        for (index in 0 until height) {
            for (message in recMessages) {
                val messageLines = message.split("\n")
                if (index >= messageLines.size) {
                    output.append(" ".repeat(width))
                } else {
                    if (messageLines[index].length > width) {
                        output.append(messageLines[index].take(width))
                    } else {
                        val empty = ((width - messageLines[index].length) / 2) + 1
                        var emptySpace = " ".repeat(empty)
                        output.append(emptySpace)
                        output.append(messageLines[index])
                        if (Math.floorMod(messageLines[index].length, 2) != 0) output.append(" ")
                        output.append(emptySpace)
                    }
                }
            }
            output.appendLine(SIDE_BORDER)
        }
        return output.toString()
    }

    fun tableLayout(messages: List<List<String>>): String {
        val output = StringBuilder()
        messages.map {
            output.append(horizontalLayout(it))
        }
        return output.toString()
    }


    companion object {
        private val TOP_BOTTOM_BORDER = "═"
        private val SIDE_BORDER = "║"
    }
}