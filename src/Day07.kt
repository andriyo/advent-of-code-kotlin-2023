package day07

import println
import readInput

val handTypes = listOf(
    listOf(1, 1, 1, 1, 1),
    listOf(2, 1, 1, 1),
    listOf(2, 2, 1),
    listOf(3, 1, 1),
    listOf(3, 2),
    listOf(4, 1),
    listOf(5),
)

val original_marks = "AKQJT"
val ordered_marks = "EDCBA"
fun addType(hand: String) = (handTypes.indexOf(powers(hand)).toString() + hand)
fun powers(hand: String): List<Int> = hand.fold(emptyMap<Char, Int>()) { acc, c ->
    acc + (c to (acc.getOrDefault(c, 0) + 1))
}.values.sorted().reversed()

val original_marks2 = "AKQTJ"
val ordered_marks2 = "DCBA1"
fun addType2(hand: String) = (handTypes.indexOf(powers2(hand)).toString() + hand)
fun powers2(hand: String): List<Int> = hand.toList().sorted().reversed().fold(emptyMap<Char, Int>()) { acc, c ->
    if (c == '1') {
        val highestKeyPair = acc.maxByOrNull { it.value }
        if (highestKeyPair == null) {
            acc + (c to (acc.getOrDefault(c, 0) + 1))
        } else {
            acc + (highestKeyPair.key to highestKeyPair.value + 1)
        }

    } else
        acc + (c to (acc.getOrDefault(c, 0) + 1))
}.values.sorted().reversed()


fun main() {
    fun part1(m: List<String>): Long =
        m.map {
            val (hand, bid) = it.split(" ")
            addType(hand.map {
                if (!it.isDigit()) ordered_marks[original_marks.indexOf(it)] else it
            }.joinToString("")) to bid.toLong()
        }.sortedWith { a, b ->
            a.first.compareTo(b.first)
        }.mapIndexed { index, handToBid ->
            handToBid.second * (index + 1)
        }.sum()

    fun part2(m: List<String>) =
        m.map {
            val (hand, bid) = it.split(" ")
            addType2(hand.map {
                if (!it.isDigit()) ordered_marks2[original_marks2.indexOf(it)] else it
            }.joinToString("")) to bid.toLong()
        }.sortedWith { a, b ->
            a.first.compareTo(b.first)
        }.mapIndexed { index, handToBid ->
            handToBid.second * (index + 1)
        }.sum()

    part1(readInput("Day07_test")).println()
    part1(readInput("Day07")).println()
    part2(readInput("Day07_test")).println()
    part2(readInput("Day07")).println()

}
