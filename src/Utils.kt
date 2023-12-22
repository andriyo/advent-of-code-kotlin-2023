import java.math.BigInteger
import java.security.MessageDigest
import kotlin.io.path.Path
import kotlin.io.path.readLines
import kotlin.io.path.readText
import kotlin.io.path.writeText

val inputFolder = "${System.getProperty("user.home")}/advent_input/"

/**
 * Reads lines from the given input txt file.
 */
fun readInput(name: String): List<String> {
    return Path("$inputFolder$name.txt").readLines()
}

/**
 * Converts string to md5 hash.
 */
fun String.md5() = BigInteger(1, MessageDigest.getInstance("MD5").digest(toByteArray()))
    .toString(16)
    .padStart(32, '0')

/**
 * The cleaner shorthand for printing output.
 */
fun Any?.println() = println(this)

fun main() {
    for (day in 26..26) {
        Path("$inputFolder/Day${day}.txt").writeText("")
        Path("$inputFolder/Day${day}_test.txt").writeText("")
        Path("src/Day${day}.kt").writeText(
            Path("src/DayXX.kt.template").readText().replace("XX", day.toString())
        )
    }
}
