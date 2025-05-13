package presentation.utils.formatter

import org.example.presentation.utils.formatter.CliFormatter
import kotlin.test.Test

class CliFormatterTest {

    @Test
    fun testRectangle() {
        val cliFormatter = CliFormatter()
        println(
            cliFormatter.rectangleLayout(
                "Testvfsvsfvfssfvdsend\nl;vsfsfvl;d,\nTTTT",
                width = 26,
                height = 2,
            )
        )
        assert(true)
    }


    @Test
    fun testVertical() {
        val cliFormatter = CliFormatter()
        println(
            cliFormatter.verticalLayout(
                listOf("sdssssd", "sdsddsdsdsccc", "DSds"),
            )
        )
        assert(true)
    }

    @Test
    fun testHorizontal() {
        val cliFormatter = CliFormatter()
        println(
            cliFormatter.horizontalLayout(
                listOf("sdssssd\nss", "sdsddsdsdsccc", "DSds"),
            )
        )
        assert(true)
    }

    @Test
    fun testTable() {
        val cliFormatter = CliFormatter()
        println(
            cliFormatter.tableLayout(
                listOf(
                    listOf("header", "header2", "header3"),
                    listOf("header\ndd\nedd", "header2", "header3"),
                    listOf("header", "header2", "header3"),
                    listOf("header", "header2", "header3")
                )
            )
        )
        assert(true)
    }


}