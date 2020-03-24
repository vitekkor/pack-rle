import org.junit.Assert.assertThrows
import org.junit.Test
import org.kohsuke.args4j.CmdLineException
import java.io.File
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue


class PackRLETest {

    private fun assertFileContent(expectedFile: String, actualFile: String): Boolean {
        val expected = File(expectedFile).readLines()
        val actual = File(actualFile).readLines()
        for (i in actual.indices) {
            if (expected[i] != actual[i] || expected[i].length < actual[i].length) return false
        }
        return expected == actual
    }

    @Test
    fun packRLE() {
        main("pack-rle -z -out output/1.txt input/1.txt".split(" ").toTypedArray())
        assertThrows(IllegalArgumentException::class.java) {
            main("pack-rle -z -out xxx.txt input/1.txt hahah".split(" ").toTypedArray())
        }
        main("pack-rle -z input/3.txt".split(" ").toTypedArray())
        assertTrue {
            assertFileContent("output/3.txt", "input/3.txt")
        }
        main("pack-rle -u input/3.txt".split(" ").toTypedArray())
        assertFalse { assertFileContent("output/3.txt", "input/3.txt") }
        assertTrue { assertFileContent("input/3_1.txt", "input/3.txt") }
        main("pack-rle -u output/1.txt".split(" ").toTypedArray())
        assertTrue { assertFileContent("input/1.txt", "output/1.txt") }
        assertThrows(IllegalArgumentException::class.java) {
            main("pack-rle -z -u -out xxx.txt".split(" ").toTypedArray())
        }
        assertThrows(IllegalArgumentException::class.java) {
            main("pаck-rle -u xxx.txt".split(" ").toTypedArray()) //буква 'a' - русская
        }
        assertThrows(IllegalArgumentException::class.java) {
            main("pack -z xxx.txt".split(" ").toTypedArray())
        }
        assertThrows(IllegalArgumentException::class.java) {
            main("pack-rle -z".split(" ").toTypedArray())
        }
        main("pack-rle -z -out output/2.txt input/2.txt".split(" ").toTypedArray())
        main("pack-rle -z output/2.txt".split(" ").toTypedArray())
        main("pack-rle -z -out output/tmp.txt input/2.txt".split(" ").toTypedArray())
        main("pack-rle -u output/2.txt".split(" ").toTypedArray())
        assertTrue { assertFileContent("output/tmp.txt", "output/2.txt") }
        main("pack-rle -u output/2.txt".split(" ").toTypedArray())
        assertTrue { assertFileContent("output/2.txt", "input/2.txt") }
        File("output/tmp.txt").delete()
    }

    @Test
    fun decode() {
        assertEquals(decode("KKKKKKKOOOOOOOTTTTTLLLLIIIIIIIIIINNNNNN"), decode("7K7O5T4L10I6N"))
        assertEquals("AAAAAOOOOOOOYYYYMMMMMMMMMMPPPPPPPPP", decode("5A7O4Y10M9P"))
        assertEquals("CCCCCCCCDDDDEEE", decode("8C4D3E"))
        assertEquals("TTESSST", decode("2TE3ST"))
        assertEquals("DBAEFDBAFDABFDBAEDFABEDFAEBDFEABDFABEDFFADBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBAEBDDBAEBDF",
                decode("DBAEFDBAFDABFDBAEDFABEDFAEBDFEABDFABED2FAD31BAEB2DBAEBDF"))
        assertEquals("wwwwwiiiikkkkkkkiiippppppeeeeeddddiia",
                decode("5w4i7k3i6p5e4d2i1a"))
        assertEquals("0000", decode("4⌂"))
        assertEquals("02000004000001BABB22FFFFFEFEFCFC4812EFF0F0F0F0F0D0D0D0D0D0B0B1A0A0A1",
                decode("⌂Á5⌂Ã5⌂ÀBA2B2Á5FEFEFCFCÃÈÀÁE2F⌂F⌂F⌂F⌂F⌂D⌂D⌂D⌂D⌂D⌂B⌂BÀA⌂A⌂AÀ"))
        assertEquals("BABABABABABBABABABABABBABABABB687064701890434836048016401AD6C4ADC6146A4DC63A4C6486",
                decode("BABABABABA2BABABABABA2BABABA2BÅÈÆ⌂ÅÃÆ⌂ÀÈÉ⌂ÃÂÃÈÂÅ⌂ÃÈ⌂ÀÅÃ⌂ÀADÅCÃADCÅÀÃÅAÃDCÅÂAÃCÅÃÈÅ"))
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
        assertEquals("⌂Á5⌂Ã5⌂ÀBA2B2Á5FEFEFCFCÃÈÀÁE2F⌂F⌂F⌂F⌂F⌂D⌂D⌂D⌂D⌂D⌂B⌂BÀA⌂A⌂AÀ",
                encode("02000004000001BABB22FFFFFEFEFCFC4812EFF0F0F0F0F0D0D0D0D0D0B0B1A0A0A1"))
        assertEquals("02000004000001BABB22FFFFFEFEFCFC4812EFF0F0F0F0F0D0D0D0D0D0B0B1A0A0A1",
                decode(encode("02000004000001BABB22FFFFFEFEFCFC4812EFF0F0F0F0F0D0D0D0D0D0B0B1A0A0A1")))
        assertEquals("BABABABABA2BABABABABA2BABABA2BÅÈÆ⌂ÅÃÆ⌂ÀÈÉ⌂ÃÂÃÈÂÅ⌂ÃÈ⌂ÀÅÃ⌂ÀADÅCÃADCÅÀÃÅAÃDCÅÂAÃCÅÃÈÅ",
                encode("BABABABABABBABABABABABBABABABB687064701890434836048016401AD6C4ADC6146A4DC63A4C6486"))
    }
}