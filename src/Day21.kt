package day21

import println
import readInput

typealias Point = Pair<Int, Int>

fun main() {
    fun part1(lines: List<String>): Long {
        val sPoint = lines.mapIndexed() { index, s ->
            s.mapIndexedNotNull() { index2, c ->
                if (c == 'S') {
                    index to index2
                } else {
                    null
                }
            }
        }.flatten().first()
        val n = lines.size
        val m = lines[0].length

        val plots = setOf(sPoint).toMutableSet()
        repeat(500) {
            val newPlots = setOf<Point>().toMutableSet()
            for (plot in plots) {
                val (x, y) = plot
                newPlots.addAll(setOf(
                    x + 1 to y,
                    x - 1 to y,
                    x to y + 1,
                    x to y - 1
                ).filter {
                    val projectedX = if (it.first >= 0 ) it.first % n else (it.first % n + n) % n
                    val projectedY = if (it.second >= 0 ) it.second % m else (it.second % m + m) % m
                    val c = lines[projectedX][projectedY]
                    c == '.' || c == 'S'
                })
            }
//            newPlots.size.println()
            plots.clear()
            plots.addAll(newPlots)
        }
        return plots.size.toLong()
    }

    fun part2(lines: List<String>): Long {
        return 1L
    }


    part1(readInput("Day21_test")).println()
//    part1(readInput("Day21")).println()
//    part2(readInput("Day21_test")).println()
//    part2(readInput("Day21")).println()

}
