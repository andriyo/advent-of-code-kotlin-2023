package day16

import println
import readInput

typealias Vector = Pair<Int, Int>
typealias Point = Pair<Int, Int>
data class Beam(val origin: Point, val dir: Vector)

fun Beam.rotR() = copy(dir = Vector(dir.second, -dir.first))
fun Beam.rotL() = copy(dir = Vector(-dir.second, dir.first))
fun Point.add(other: Vector) = Point(first + other.first, second + other.second)

fun main() {
    fun part1(
        lines: List<String>, startBeam: Beam = Beam(
            Point(0, -1),
            Vector(0, 1),
        )
    ): Long {
        return generateSequence(
            listOf(
                startBeam
            ) to emptySet<Beam>()
        ) { beamsAndVisited ->
            val (beams, visited) = beamsAndVisited
            val newBeams = beams.map { beam ->
                val (lastPoint, vector) = beam
                val newPoint = lastPoint.add(vector)
                val cell = lines.getOrNull(newPoint.first)?.getOrNull(newPoint.second)
                val newBeam = Beam(newPoint, vector)
                if (cell != null) {
                    when (cell) {
                        '|' -> {
                            if (vector.second != 0) listOf(
                                newBeam.copy(dir = Vector(1, 0)),
                                newBeam.copy(dir = Vector(-1, 0))
                            ) else listOf(newBeam)
                        }

                        '-' -> {
                            if (vector.first != 0) listOf(
                                newBeam.copy(dir = Vector(0, 1)),
                                newBeam.copy(dir = Vector(0, -1))
                            ) else listOf(newBeam)
                        }

                        '/' -> {
                            if (vector.first != 0) listOf(newBeam.rotR()) else listOf(newBeam.rotL())
                        }

                        '\\' -> {
                            if (vector.first != 0) listOf(newBeam.rotL()) else listOf(newBeam.rotR())
                        }

                        else -> {
                            listOf(newBeam)
                        }
                    }
                } else {
                    listOf()
                }.filterNot { visited.contains(it) }
            }.flatten()
            val newVisited = visited + newBeams
            if (visited.size == newVisited.size)
                null
            else
                newBeams to newVisited
        }.last().second.map { it.origin }.toSet().size.toLong()

    }

    fun part2(lines: List<String>): Long {
        val down = (1..lines[0].length).map { Beam(Point(-1, it), Vector(1, 0)) }
        val up = (1..lines[0].length).map { Beam(Point(lines.size, it), Vector(-1, 0)) }
        val left = (1..lines.size).map { Beam(Point(it, lines[0].length), Vector(0, -1)) }
        val right = (1..lines.size).map { Beam(Point(it, 0), Vector(0, 1)) }
        return (down + up + left + right).map { part1(lines, it) }.max()
    }


    part1(readInput("Day16_test")).println()
    part1(readInput("Day16")).println()
    part2(readInput("Day16_test")).println()
    part2(readInput("Day16")).println()

}
