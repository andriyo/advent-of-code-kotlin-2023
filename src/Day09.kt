package day07

import println
import readInput


fun main() {
    fun part1(lines: List<String>): Int {
        return lines.sumOf {
            val nums = it.split(" ").map { it.toInt() }
            generateSequence(nums) { curNums ->
                if (curNums.any { aNum -> aNum != 0 })
                    curNums.windowed(2).map { (a, b) -> b - a }
                else
                    null
            }.map { it.last() }.sum()
        }
    }

    fun part2(lines: List<String>): Int {
        return lines.map {
            val nums = it.split(" ").map { it.toInt() }
            generateSequence(nums) { curNums ->
                if (curNums.any { aNum -> aNum != 0 })
                    curNums.windowed(2).map { (a, b) -> b - a }
                else
                    null
            }.map { it.first() }.toList().reversed().fold(0) { acc, aNum -> aNum - acc }
        }.sum()
    }


    part1(readInput("Day09_test")).println()
    part1(readInput("Day09")).println()
    part2(readInput("Day09_test")).println()
    part2(readInput("Day09")).println()

}
