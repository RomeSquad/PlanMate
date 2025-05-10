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
                width = 20,
                height = 2,
                omitRightSideBorder = true
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


}