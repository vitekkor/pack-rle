import java.io.File


fun main(args: Array<String>) {
    Parser.main(args)
}

fun decode(str: String): String {
    val result = StringBuilder()
    var i = 0
    while (i in str.indices) {
        val times = str.substring(i).takeWhile { it.isDigit() }
        val index = dictionary.indexOf(str[i + times.count()])
        val char = if (index != -1) '0' + index else str[i + times.count()]
        repeat(times.toIntOrNull() ?: 1) { result.append(char) }
        i += 1 + times.count()
    }
    return result.toString()
}

private val dictionary = mutableListOf('⌂', 'À', 'Á', 'Â', 'Ã', 'Ä', 'Å', 'Æ', 'È','É')

fun encode(string: String): String {
    if (string == "") return ""
    val result = StringBuilder()
    var count = 0
    var prev = string[0]
    for (char in string) {
        if (char != prev) {
            if (count > 1) result.append(count)
            if (prev.isDigit()) result.append(dictionary.elementAt(prev - '0')) else result.append(prev)
            count = 0
            prev = char
        }
        count++
    }
    if (count > 1) result.append(count)
    if (prev.isDigit()) result.append(dictionary.elementAt(prev - '0')) else result.append(prev)
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
    println("Pack-rle: "+ if (pack) "pack" else {"unpack"}+" successful")
}

