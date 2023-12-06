package day01

import println
import readInput

val digitNames = listOf(
    "one",
    "two",
    "three",
    "four",
    "five",
    "six",
    "seven",
    "eight",
    "nine",
)

fun findFirst(s: String): String {
    return listOfNotNull(
        s.indexOfFirst {
            it.isDigit()
        }.takeIf { it > -1 }?.let { it to s[it].toString() },
        digitNames.mapIndexed { digitIndex, digitName ->
            val matchIndex = s.indexOf(digitName)
            matchIndex to (digitIndex + 1).toString()
        }.filterNot { it.first == -1 }.minByOrNull { it.first }).minBy { it.first }.second
}

fun findLast(s: String): String {
    return listOfNotNull(
        s.indexOfLast {
            it.isDigit()
        }.takeIf { it > -1 }?.let { it to s[it].toString() },
        digitNames.mapIndexed { digitIndex, digitName ->
            val matchIndex = s.lastIndexOf(digitName)
            matchIndex to (digitIndex + 1).toString()
        }.filterNot { it.first == -1 }.maxByOrNull { it.first }).maxBy { it.first }.second
}

fun main() {

    fun part1(input: List<String>): Int {
        return input.sumOf {
            Integer.parseInt(
                it.find { it.isDigit() }.toString() + it.findLast { it.isDigit() }
            )
        }
    }

    fun part2(input: List<String>): Int {
        return input.sumOf { s ->
            Integer.parseInt(findFirst(s) + findLast(s))
        }
    }


    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day01_test")
//    check(part1(testInput) == 142)
    part2(testInput).println()

    val input = readInput("Day01")
    part1(input).println()
    part2(input).println()
}
