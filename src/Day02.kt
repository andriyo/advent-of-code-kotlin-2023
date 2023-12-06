package day02

import println
import readInput
import kotlin.math.max

enum class Color {
    red, blue, green
}

fun main() {
    fun makeBag(s: String): Map<Color, Int> = s.split(",").associate {
        it.trim().split(" ").let { Color.valueOf(it[1]) to it[0].toInt() }
    }

    fun possibleBag(s: String): Boolean {
        val bag = makeBag(s)
        return (bag[Color.red] ?: 0) <= 12 && (bag[Color.green] ?: 0) <= 13 && (bag[Color.blue] ?: 0) <= 14
    }

    fun part1(list: List<String>): Int {
        val gameIdStartPosition = "Game ".length
        return list.sumOf {
            val (gameIdStr, allBagsStr) = it.split(":")
            val gameId = gameIdStr.substring(gameIdStartPosition).toInt()
            if (allBagsStr.split(";").all { possibleBag(it) }) gameId else 0
        }
    }


    fun part2(list: List<String>): Int {
        return list.map {
            val (_, allBagsStr) = it.split(":")
            allBagsStr.split(";")
                .map { makeBag(it) }
                .fold(mapOf(Color.red to 0, Color.blue to 0, Color.green to 0))
                { acc, other ->
                    acc.mapValues { max(it.value, other[it.key] ?: 0) }
                }.values.fold(1) { acc, i -> acc * i }
        }.sum()
    }

    val input = readInput("Day02")
    part1(input).println()
    part2(input).println()
}
