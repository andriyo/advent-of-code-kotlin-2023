package day03

import println
import readInput
import kotlin.math.max

fun hasConnection(m: List<String>, i: Int, j: Int) =
    // Part 2 is using better implementation
    listOfNotNull(
        m.getOrNull(i - 1)?.getOrNull(j),
        m.getOrNull(i - 1)?.getOrNull(j - 1),
        m.getOrNull(i - 1)?.getOrNull(j + 1),
        m.getOrNull(i)?.getOrNull(j),
        m.getOrNull(i)?.getOrNull(j - 1),
        m.getOrNull(i)?.getOrNull(j + 1),
        m.getOrNull(i + 1)?.getOrNull(j),
        m.getOrNull(i + 1)?.getOrNull(j - 1),
        m.getOrNull(i + 1)?.getOrNull(j + 1),
    ).any { !it.isDigit() && it != '.' }
typealias GearPos = Pair<Int, Int>

val directions = listOf(
    -1 to -1,
    -1 to 0,
    -1 to 1,
    0 to -1,
    0 to 1,
    1 to -1,
    1 to 0,
    1 to 1,
)

fun getGears(m: List<String>, i: Int, j: Int): List<GearPos> = directions.mapNotNull {
    val (gi, gj) = (it.first + i) to (it.second + j)
    if ('*' == m.getOrNull(gi)?.getOrNull(gj)) GearPos(gi, gj) else null
}


fun main() {
    fun part1(m: List<String>) =
        m.mapIndexed { i, mm ->
            mm.foldIndexed(emptyList<List<Pair<Char, Boolean>>>()) { j, acc, c ->
                if (c.isDigit()) {
                    val head = acc.take(max(0, acc.size - 1))
                    val tail = (acc.lastOrNull() ?: emptyList()) + (c to hasConnection(m, i, j))
                    head + listOf(tail)
                } else {
                    acc + listOf(emptyList())
                }
            }.asSequence().filter { it.isNotEmpty() }.map {
                it.fold(Pair("", false)) { acc, pair ->
                    (acc.first + pair.first.toString()) to (acc.second || pair.second)
                }
            }.filter { it.second }.map { it.first.toInt() }.sum()
        }.sum()


    fun part2(m: List<String>): Int =
        m.flatMapIndexed { i, mm ->
            mm.foldIndexed(emptyList<List<Pair<Char, List<GearPos>>>>()) { j, acc, c ->
                if (c.isDigit()) {
                    val head = acc.take(max(0, acc.size - 1))
                    val tail = (acc.lastOrNull() ?: emptyList()) + (c to getGears(m, i, j))
                    head + listOf(tail)
                } else {
                    acc + listOf(emptyList())
                }
            }.asSequence()
                .filter {
                    it.isNotEmpty()
                }
                .map {
                    it.fold(Pair("", emptyList<GearPos>())) { acc, pair ->
                        (acc.first + pair.first.toString()) to (acc.second.union(pair.second).toList())
                    }
                }.filter {
                    it.second.isNotEmpty()
                }
                .map {
                    it.first.toInt() to it.second
                }
                .flatMap { numberToGearList ->
                    numberToGearList.second.map { it to numberToGearList.first }
                }
        }.fold(emptyMap<GearPos, List<Int>>()) { acc, pair ->
            acc + (pair.first to ((acc[pair.first] ?: emptyList()) + listOf(pair.second)))
        }.filter { it.value.size == 2 }
            .mapValues {
                it.value.reduce { a, b -> a * b }
            }.values.sum()



    part1(readInput("Day03_test")).println()
    part2(readInput("Day03_test")).println()
    part2(readInput("Day03")).println()

}

// alternative implementation that supports overflowing numbers

//    fun part1(m: List<String>) =
//        m.flatMapIndexed { i: Int, s: String ->
//            s.mapIndexed { j, c -> Pair(Pair(i, j), c) }
//        }.fold(emptyList<List<Pair<Char, Boolean>>>()) { acc, cell ->
//            val (pos, c) = cell
//            val (i, j) = pos
//            if (c.isDigit()) {
//                val head = acc.take(max(0, acc.size - 1))
//                val tail = (acc.lastOrNull() ?: emptyList()) + (c to hasConnection(m, i, j))
//                head + listOf(tail)
//            } else {
//                acc + listOf(emptyList())
//            }
//        }.asSequence().filter { it.isNotEmpty() }.map {
//            it.fold(Pair("", false)) { acc, pair ->
//                (acc.first + pair.first.toString()) to (acc.second || pair.second)
//            }
//        }.filter { it.second }.map { it.first.toInt().also { it.println() } }.sum()
