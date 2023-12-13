package day08

import println
import readInput


fun List<Long>.lcm(): Long {
    return this.reduce { a, b ->
        val gcd = generateSequence(a to b) { (a, b) ->
            if (b == 0L) null else b to a.mod(b)
        }.last().first
        a * b / gcd
    }
}

fun loadData(m: List<String>) =
    m[0] to m.drop(2).associate {
        val (source, destinations) = it.split(" = ")
        source to (
                destinations.substring(1, destinations.length - 1).split(", ")
                )
    }

fun main() {
    fun part1(m: List<String>): Int {
        val (directions, map) = loadData(m)
        return generateSequence("AAA" to 0) { (loc, dirIndex) ->
            if (loc == "ZZZ") null
            else {
                val dir = directions[dirIndex]
                val newDirIndex = if (dirIndex == directions.length - 1) 0 else dirIndex + 1
                requireNotNull(map[loc]?.get(if (dir == 'L') 0 else 1)) to newDirIndex
            }
        }.count() - 1
    }

    fun part2(m: List<String>): Long {
        val (directions, map) = loadData(m)
        val initialLocs = map.keys.filter { it.endsWith("A") }
        return initialLocs.map {
            generateSequence(it to 0) { (loc, dirIndex) ->
                if (loc.endsWith("Z")) null
                else {
                    val dir = directions[dirIndex]
                    val newDirIndex = if (dirIndex == directions.length - 1) 0 else dirIndex + 1
                    requireNotNull(map[loc]?.get(if (dir == 'L') 0 else 1)) to newDirIndex
                }
            }.fold(0L) { acc, _ ->
                acc + 1
            } - 1
        }.lcm()
    }


//    part1(readInput("Day08_test")).println()
//    part1(readInput("Day08")).println()
    part2(readInput("Day08_test")).println()
    part2(readInput("Day08")).println()

}
