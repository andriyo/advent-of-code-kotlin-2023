package day07

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
        val iter = generateSequence(0) {
            if (it == directions.length - 1) 0 else it + 1
        }.iterator()
        return generateSequence("AAA") {
            if (it == "ZZZ") null
            else {
                val dir = directions[iter.next()]
                requireNotNull(map[it]?.get(if (dir == 'L') 0 else 1))
            }
        }.count() - 1
    }

    fun part2(m: List<String>): Long {
        val (directions, map) = loadData(m)
        val iter = generateSequence(0) {
            if (it == directions.length - 1) 0 else it + 1
        }.iterator()

        val initialLocs = map.keys.filter { it.endsWith("A") }
        return initialLocs.map {
            generateSequence(it) {
                if (it.endsWith("Z")) null
                else {
                    val dir = directions[iter.next()]
                    requireNotNull(map[it]?.get(if (dir == 'L') 0 else 1))
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
