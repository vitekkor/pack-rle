import java.io.File


fun main(args: Array<String>) {
    Parser.main(args)
}

fun decode(str: String): String {
    val result = StringBuilder()
    var i = 0
    while (i in str.indices) {
        val times = str.substring(i).takeWhile { it.isDigit() }
        if (times != "") {
            repeat(times.toInt()) { result.append(str[i + times.count()]) }
        } else result.append(str[i])
        i += 1 + times.count()
    }
    return result.toString()
}

fun encode(string: String): String {
    if (string == "") return ""
    val result = StringBuilder()
    var count = 0
    var prev = string[0]
    for (char in string) {
        if (char != prev) {
            if (count > 1) result.append(count)
            result.append(prev)
            count = 0
            prev = char
        }
        count++
    }
    if (count > 1) result.append(count)
    result.append(prev)
    return result.toString()
}

fun packRLE(pack: Boolean, inputFile: String, outputFile: String?) {
    val inputStrings = File(inputFile).readLines()
    val outputStream = File(outputFile ?: inputFile).bufferedWriter()
    outputStream.use {
        for (string in inputStrings) {
            val newString = if (pack) encode(string) else decode(string)
            it.write(newString)
            it.newLine()
        }
    }
}

