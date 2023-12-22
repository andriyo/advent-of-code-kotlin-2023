package day18

import println
import readInput
import java.math.BigDecimal
import kotlin.math.floor

data class Point(val x: BigDecimal, val y: BigDecimal)

val directions = mapOf(
    "U" to Pair(0, -1),
    "D" to Pair(0, 1),
    "L" to Pair(-1, 0),
    "R" to Pair(1, 0),
)

fun distance(a: Point, b: Point): BigDecimal {
    return floor(kotlin.math.hypot((b.x - a.x).toDouble(), (b.y - a.y).toDouble())).toBigDecimal()
}

fun polygonArea(points: List<Point>): BigDecimal {
    val n = points.size
    val area = points.indices.fold(BigDecimal.ZERO) { acc, i ->
        val j = (i + 1) % n
        acc + points[i].x * points[j].y - points[j].x * points[i].y
    }
    return (area / 2.toBigDecimal())
}

fun polygonPerimeter(points: List<Point>): BigDecimal {
    val n = points.size
    return points.indices.fold(BigDecimal.ZERO) { acc, i ->
        val j = (i + 1) % n
        val k = (i + 2) % n
        val a = points[i]
        val b = points[j]
        val c = points[k]
        val mainPerimeter = (distance(a, b) - 1.toBigDecimal()) / 2.toBigDecimal()
        val angleABDegrees = Math.toDegrees(
            Math.atan2((b.y - a.y).toDouble(), (b.x - a.x).toDouble()) - Math.atan2(
                (c.y - b.y).toDouble(),
                (c.x - b.x).toDouble()
            )
        )
        val extraPerimeter = if (angleABDegrees == -90.0 || angleABDegrees == 270.0) {
            0.75.toBigDecimal()
        } else {
            0.25.toBigDecimal()
        }
        acc + mainPerimeter + extraPerimeter
    }


}

fun main() {
    fun common(lines: List<String>, parser: (String) -> Pair<String, BigDecimal>): BigDecimal {
        val start = Point(BigDecimal.ZERO, BigDecimal.ZERO)
        val vectors = lines.map { line ->
            val (direction, magnitude) = parser(line)
            directions[direction]?.let {
                Point(
                    it.first.toBigDecimal() * magnitude,
                    it.second.toBigDecimal() * magnitude
                )
            } ?: error("Unknown direction: $direction")
        }

        val points = vectors.fold(listOf(start)) { points, vector ->
            points + Point(
                points.last().x + vector.x,
                points.last().y + vector.y
            )
        }
        return polygonArea(points.dropLast(1)) + polygonPerimeter(points.dropLast(1))
    }

    fun part1(lines: List<String>): BigDecimal {
        return common(lines) { line ->
            val (direction, magnitudeStr) = line.split(" ")
            val magnitude = magnitudeStr.toBigDecimal()
            direction to magnitude
        }
    }

    fun part2(lines: List<String>): BigDecimal {
        return common(lines) { line ->
            val color = line.split(" ")[2].replace("[()#]".toRegex(), "")
            val direction = when (color[color.lastIndex]) {
                '3' -> "U"
                '1' -> "D"
                '2' -> "L"
                '0' -> "R"
                else -> error("Unknown direction in : $color")
            }
            val distanceInHexString = color.substring(0, color.lastIndex)
            val magnitude = distanceInHexString.toInt(16).toBigDecimal()
            direction to magnitude
        }
    }

    part1(readInput("Day18_test")).println()
    part1(readInput("Day18")).println()
    part2(readInput("Day18_test")).println()
    part2(readInput("Day18")).println()

}
