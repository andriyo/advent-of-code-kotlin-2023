package day04

import println
import readInput


fun main() {
    fun part1(m: List<String>): Int {
        return m.sumOf {
            val (winningCards, playerCards) = it.split(": ")[1].split(" | ")
                .map {
                    it.split("\\s+".toRegex())
                        .filter { it.isNotEmpty() }
                        .map { it.toInt() }
                        .toSet()
                }
            val matches = winningCards.intersect(playerCards).size
            (if (matches > 0) 1.shl(matches - 1) else 0)
        }
    }

    fun part2(m: List<String>): Int {
        return m.map {
            val (winningCards, playerCards) = it.split(": ")[1].split(" | ")
                .map { it.split("\\s+".toRegex()).filter { it.isNotEmpty() }.map { it.toInt() }.toSet() }
            winningCards.intersect(playerCards).size
        }.reversed().fold(emptyList<Int>()) { acc, extra ->
            acc + listOf(1 + acc.takeLast(extra).sum())
        }.sum()
    }


    part1(readInput("Day04_test")).println()
    part1(readInput("Day04")).println()
    part2(readInput("Day04_test")).println()
    part2(readInput("Day04")).println()

}
