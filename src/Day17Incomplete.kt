package day17

import println
import readInput

typealias Point = Pair<Int, Int>

fun printLines(
    lines: List<String>,
    path: List<Pair<Point, Int>>
) {
    lines.forEachIndexed() { index, line ->
        line.mapIndexed { index2, c ->
            val foundItemInPath = path.find { it.first == Point(index, index2) }
            if (foundItemInPath != null) {
                // unicode for up, down, left and right
                when (foundItemInPath.second) {
                    -1 -> 'S'
                    0 -> '\u2190' // direction 0 is left
                    1 -> '\u2192' // direction 1 is right
                    2 -> '\u2191' // direction 2 is up
                    3 -> '\u2193' // direction 3 is down
                    else -> 'X'
                }
            } else {
                c
            }
        }.joinToString("", "$index.\t").println()
    }
    println()
}

val directions = listOf(
    Point(0, -1),
    Point(0, 1),
    Point(-1, 0),
    Point(1, 0)
)

data class Ray(
    val point: Point,
    val direction: Int,
    val length: Int,
    val cost: Int,
    val parent: Ray?
)

fun main() {


    fun part1(lines: List<String>): Long {
        fun List<Point>.realCost(): Int {
            return this.sumOf { lines[it.first][it.second].digitToInt() }

        }

        fun cost(point: Point) = lines[point.first][point.second].digitToInt().toLong()
        val startPoint = Point(0, 0)
        val endPoint = Point(lines.lastIndex, lines[0].lastIndex)
        val openSet = mutableSetOf<Point>(startPoint)
        val cameFrom = mutableMapOf<Point, Point>()
        val gScore = mutableMapOf<Pair<Point, Int>, Long>(startPoint to -1 to 0)
        val fScore = mutableMapOf<Point, Long>(startPoint to cost(startPoint))
        val labelMap = mutableMapOf<Point, Int>()
        fun reconstructPath(current: Point): List<Point> {
            val path = mutableListOf<Point>()
            var curr = current
            while (cameFrom.containsKey(curr)) {
                path.add(curr)
                curr = cameFrom[curr]!!
            }
            path.add(curr)
            return path
        }

        val maxLen = 3
        while (openSet.isNotEmpty()) {
            val current = openSet.minByOrNull { fScore.getOrDefault(it, Long.MAX_VALUE) }!!
            val basePath = reconstructPath(current)
            val basePathDirections = basePath.take(maxLen).map { labelMap.getOrDefault(it, -1) }
            "Considering options for $current".println()
            printLines(lines, basePath.map { it to labelMap.getOrDefault(it, -1) })
            if (current == endPoint) {

                "Happy path".println()
                printLines(lines, basePath.map { it to labelMap.getOrDefault(it, -1) })
                "Actual path value".println()
                basePath.also { it.println() }.sumOf { lines[it.first][it.second].digitToInt() }
                    .println()
                return -1 // gScore[current]!!
            }
            openSet.remove(current)
            val neighbours = listOf(
                current.copy(second = current.second - 1),
                current.copy(second = current.second + 1),
                current.copy(first = current.first - 1),
                current.copy(first = current.first + 1),
            )

            neighbours.forEachIndexed { label, neighbour ->
                if (lines.getOrNull(neighbour.first)?.getOrNull(neighbour.second) == null) return@forEachIndexed
                val newPath = listOf(neighbour) + reconstructPath(current)
                val sameDirectionCount = basePathDirections.count { it == label }
                val goneTooFarInSameDirection = sameDirectionCount == maxLen
                val extra = if (goneTooFarInSameDirection) {
                    1000
                } else {
                    0
                }
                val distanceToEndCorner = (endPoint.first - neighbour.first) + (endPoint.second - neighbour.second)

                val tentativeGScore = gScore.getOrDefault(current to label, 10000000000) + cost(neighbour)
//                val actualCost = (listOf( neighbour) + fullPath.map { it.first }).sumOf { lines[it.first][it.second].digitToInt() }
//                "Path  ${fullPath} score = $tentativeGScore is better ${tentativeGScore <= gScore.getOrDefault(neighbour, Long.MAX_VALUE)}  (${gScore.getOrDefault(neighbour, Long.MAX_VALUE)}) actual cost = $actualCost".println()
                val outcome = tentativeGScore <= gScore.getOrDefault(neighbour to label, Long.MAX_VALUE)
                if (outcome) {
                    cameFrom[neighbour] = current
                    gScore[neighbour to label] = tentativeGScore
                    fScore[neighbour] = tentativeGScore + extra
                    labelMap[neighbour] = label
                    openSet.add(neighbour)
                }
                "Over this $neighbour with direction $label:".println()
                printLines(lines, listOf(neighbour to label) + basePath.map { it to labelMap.getOrDefault(it, -1) })
                "Outcome: $outcome".println()
                if (!outcome) {
                    "Tentative score $tentativeGScore is worse than ${
                        gScore.getOrDefault(
                            neighbour to label,
                            Long.MAX_VALUE
                        )
                    }".println()
                    "Map for existing neighbour".println()
                    printLines(lines, reconstructPath(neighbour).map { it to labelMap.getOrDefault(it, -1) })
                    "Real cost ${newPath.realCost()} supposedly better cost is ${reconstructPath(neighbour).realCost()} ".println()
                }
                println()
            }
        }

        return -1
    }



    fun part2(lines: List<String>): Long {
        return 1L
    }


    part1(readInput("Day17_test")).println()
//    part1(readInput("Day17")).println()
//    part2(readInput("Day17_test")).println()
//    part2(readInput("Day17")).println()

}
