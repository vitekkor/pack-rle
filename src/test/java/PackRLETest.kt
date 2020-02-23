import org.junit.Test
import java.io.File
import kotlin.test.assertEquals


class PackRLETest {

    private fun assertFileContent(expectedFile: String, actualFile: String): Boolean {
        val expected = File(expectedFile).readText()
        val actual = File(actualFile).readText()
        return expected == actual
    }

    @Test
    fun packRLE() {
        main("pack-rle -z -out xxx.txt input/1.txt".split(" ").toTypedArray())
    }

    @Test
    fun decode() {
        assertEquals("AAAAAOOOOOOOYYYYMMMMMMMMMMPPPPPPPPP", decode("5A7O4Y10M9P"))
        assertEquals("CCCCCCCCDDDDEEE", decode("8C4D3E"))
        assertEquals("TTESSST", decode("2TE3ST"))
        assertEquals("DBAEFDBAFDABFDBAEDFABEDFAEBDFEABDFABEDFFADBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBAEBDDBAEBDF",
                decode("DBAEFDBAFDABFDBAEDFABEDFAEBDFEABDFABED2FAD31BAEB2DBAEBDF"))
        assertEquals("wwwwwiiiikkkkkkkiiippppppeeeeeddddiia",
                decode("5w4i7k3i6p5e4d2i1a"))
    }

    @Test
    fun encode() {
        assertEquals("5A7O4Y10M9P", encode("AAAAAOOOOOOOYYYYMMMMMMMMMMPPPPPPPPP"))
        assertEquals("8C4D3E", encode("CCCCCCCCDDDDEEE"))
        assertEquals("", encode(""))
        assertEquals("12WB12W3B24WB14W", encode("WWWWWWWWWWWWBWWWWWWWWWWWWBBBWWWWWWWWWWWWWWWWWWWWWWWWBWWWWWWWWWWWWWW"))
        assertEquals("2TE3ST", encode("TTESSST"))
        assertEquals("WWWWWWWWWWWWBWWWWWWWWWWWWBBBWWWWWWWWWWWWWWWWWWWWWWWWBWWWWWWWWWWWWWW"
                , decode(encode("WWWWWWWWWWWWBWWWWWWWWWWWWBBBWWWWWWWWWWWWWWWWWWWWWWWWBWWWWWWWWWWWWWW")))
        assertEquals("12WB12W3B24WB14W", encode(decode("12WB12W3B24WB14W")))
        assertEquals("DBAEFDBAFDABFDBAEDFABEDFAEBDFEABDFABED2FAD31BAEB2DBAEBDF",
                encode("DBAEFDBAFDABFDBAEDFABEDFAEBDFEABDFABEDFFADBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBAEBDDBAEBDF"))
        assertEquals("5w4i7k3i6p5e4d2ia",
                encode("wwwwwiiiikkkkkkkiiippppppeeeeeddddiia"))
    }
}