package day06

import println
import readInput
import kotlin.math.ceil
import kotlin.math.floor
import kotlin.math.sqrt

fun main() {

    // quadratic equation

    fun lowerDer(d: Long, t: Long) =
        floor(0.5 * (t - sqrt((t * t - 4 * d).toDouble()))).toLong()

    fun upperDer(d: Long, t: Long) =
        ceil(0.5 * (sqrt((t * t - 4 * d).toDouble()) + t)).toLong()


    fun part1(m: List<String>): Long {
        val times =
            m[0].removePrefix("Time: ").split("\\s+".toRegex()).map { it.trim() }.filter { it.isNotEmpty() }
                .map { it.toLong() }
        val distances =
            m[1].removePrefix("Distance: ").split("\\s+".toRegex()).map { it.trim() }.filter { it.isNotEmpty() }
                .map { it.toLong() }

        return times.mapIndexed { index, time ->
            val distance = distances[index]
            (upperDer(distance, time) - lowerDer(distance, time) - 1)
        }.reduce { a, b -> a * b }
    }


    fun part2(m: List<String>): Long {
        val time =
            m[0].removePrefix("Time: ").replace(" ", "").trim().toLong()
        val distance =
            m[1].removePrefix("Distance: ").replace(" ","").trim().toLong()

        return upperDer(distance, time) - lowerDer(distance, time) - 1
    }

    part1(readInput("Day06_test")).println()
    part1(readInput("Day06")).println()
    part2(readInput("Day06_test")).println()
    part2(readInput("Day06")).println()

}
