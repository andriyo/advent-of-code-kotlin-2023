package day07

import println
import readInput
import java.util.TreeMap
import kotlin.math.abs


fun List<Int>.expansion(scale: Int) = this.mapIndexed { index, i ->
    i.toLong() to ((scale - 1) * (index + 1)).toLong()
}.toMap(TreeMap())

fun main() {
    fun part1(lines: List<String>, scale: Int = 2): Long {
        val yTrans = lines.mapIndexedNotNull { index, s ->
            if (s.indexOf("#") == -1) index else null
        }.expansion(scale)


        val xTrans = lines[0].indices.mapNotNull { xIndex ->
            if (lines.map { it[xIndex] }.all { it == '.' }) xIndex else null
        }.expansion(scale)

        fun Long.ey() = (yTrans.floorEntry(this)?.value ?: 0) + this

        fun Long.ex() = (xTrans.floorEntry(this)?.value ?: 0) + this

        val galaxies = lines.mapIndexed { yIndex, s ->
            s.mapIndexedNotNull { xIndex, c ->
                if (c == '#') xIndex.toLong() to yIndex.toLong() else null
            }
        }.flatten().map { it.first.ex() to it.second.ey() }

        return galaxies.mapIndexed { index, pair ->
            ((index + 1)..<galaxies.size).map { i ->
                val other = galaxies[i]
                (abs(other.first - pair.first) + abs(other.second - pair.second))
            }
        }.flatten().sum()
    }

    fun part2(lines: List<String>): Long {
        return part1(lines, 1_000_000)
    }

    part1(readInput("Day11_test")).println()
    part1(readInput("Day11")).println()
    part2(readInput("Day11_test")).println()
    part2(readInput("Day11")).println()

}
