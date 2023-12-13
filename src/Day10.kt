package day10

import println
import readInput
import kotlin.reflect.KFunction1

typealias Pos = Pair<Int, Int>

fun Pos.add(other: Pos) = Pos(first + other.first, second + other.second)
fun Pos.opposite() = Pos(-first, -second)
fun Pos.rotR() = Pos(second, -first)
fun Pos.rotL() = Pos(-second, first)


val directions = mapOf(
    (-1 to 0) to "|F7",
    (1 to 0) to "|LJ",
    (0 to -1) to "-FL",
    (0 to 1) to "-J7",
)

fun List<String>.get(pos: Pos) = this.getOrNull(pos.first)?.getOrNull(pos.second) ?: ' '
fun findBigLoop(
    startPosition: Pair<Int, Int>,
    lines: List<String>
) = directions.mapNotNull {
    val firstNeighborPos = startPosition.add(it.key)
    if (it.value.indexOf(lines.get(firstNeighborPos)) != -1) {
        val loop = generateSequence((startPosition to firstNeighborPos) to 1) { (movement, stepCount) ->
            val (previousPosition, currentPosition) = movement
            if (currentPosition == startPosition) null else {
                val curr = lines.get(currentPosition)
                directions.mapNotNull {
                    val dir = it.key
                    val neighborPos = currentPosition.add(dir)
                    if (neighborPos == previousPosition) null else {
                        val neighbor = lines.get(neighborPos)
                        if ((it.value.indexOf(neighbor) != -1 || neighbor == 'S')
                            && directions[dir.opposite()]!!.indexOf(curr) != -1
                        )
                            (currentPosition to neighborPos) to stepCount + 1 else null
                    }
                }.firstOrNull()
            }
        }.toList()

        if (loop.last().first.second == startPosition) {
            loop
        } else null
    } else null
}.maxBy { it.last().second }

fun main() {
    fun getStartPosition(lines: List<String>) = lines.mapIndexedNotNull { x, s ->
        val y = s.indexOf("S")
        if (y != -1) x to y else null
    }.first()

    fun part1(lines: List<String>): Int {
        val startPosition = getStartPosition(lines)
        val bigLoop = findBigLoop(startPosition, lines)
        return bigLoop.last().second / 2
    }

    fun isValid(
        currentPos: Pos,
        acc: Set<Pos>,
        visitedPositions: Set<Pos>,
        bigLoopSet: Set<Pos>
    ): Boolean {
        return (!acc.contains(currentPos) && !visitedPositions.contains(currentPos) && !bigLoopSet.contains(
            currentPos
        ))
    }

    fun countOnSide(
        bigLoop: List<Pos>,
        bigLoopSet: Set<Pos>,
        rot: KFunction1<Pos, Pos>,
        lines: List<String>,
    ) = bigLoop.zipWithNext().map { (src, dst) ->
        val dir = (dst.first - src.first) to (dst.second - src.second)
        listOf(src.add(rot(dir)), dst.add(rot(dir)))
    }.flatten().fold(emptySet<Pos>() to false) { acc, pos ->
        val result = generateSequence(Triple(emptySet<Pos>() to false, emptySet<Pos>(), setOf(pos))) { parameters ->
            val (result, alreadyVisited, positionsToExplore) = parameters
            if (result.second) return@generateSequence null
            if (positionsToExplore.isEmpty())
                null
            else {
                val validatedPositionsToExplore = positionsToExplore.filter { pos ->
                    isValid(pos, acc.first.toSet(), alreadyVisited, bigLoopSet)
                }.toSet()
                Triple(
                    (result.first + validatedPositionsToExplore) to positionsToExplore.any { lines.get(it) == ' ' },
                    alreadyVisited + positionsToExplore,
                    validatedPositionsToExplore.map { pos -> directions.keys.map { pos.add(it) } }.flatten().toSet()
                )
            }
        }.last().first
        (acc.first + result.first) to (acc.second || result.second)
    }.takeIf { it.second.not() }?.first ?: emptySet()


    fun part2(lines: List<String>): Int {
        val startPosition = getStartPosition(lines)
        val bigLoop = findBigLoop(startPosition, lines).map { it.first.first }.toList()
        val bigLoopSet = bigLoop.toSet()

        return (countOnSide(bigLoop, bigLoopSet, Pos::rotR, lines)
                + countOnSide(
            bigLoop, bigLoopSet, Pos::rotL, lines
        )).size
    }


    part1(readInput("Day10_test")).println()
    part1(readInput("Day10")).println()
    part2(readInput("Day10_test")).println()
    part2(readInput("Day10")).println()

}
