package day14

import println
import readInput
import java.util.TreeMap

fun List<String>.transpose(): List<String> {
    return (0..this[0].lastIndex).fold(emptyList()) { acc, i ->
        acc.plusElement(this.map { it[i] }.joinToString(""))
    }
}

// not used
fun List<String>.rotateLeft(): List<String> {
    return this.fold(List(this[0].length) { "" }) { acc, str ->
        str.reversed().toList().zip(acc).map { it.second + it.first }
    }
}

fun List<String>.rotateRight(): List<String> {
    return this.fold(List(this[0].length) { "" }) { acc, str ->
        str.toList().zip(acc).map { it.first.toString() + it.second }
    }
}


// compact version of tilted grid
fun List<String>.tilt() = this.map { row ->
    row.foldIndexed(mapOf(0 to 0)) { index, map, c ->
        when (c) {
            '#' -> map + ((index + 1) to 0)
            'O' -> {
                val lastEntry = map.entries.last()
                map + (lastEntry.key to (lastEntry.value + 1))
            }

            else -> map
        }
    }.toMap(TreeMap())
}

// from compact to regular
fun List<TreeMap<Int, Int>>.expand(): List<String> {
    return map { m ->
        m.entries.fold("" to -1) { acc, entry ->
            val (str, previousIndex) = acc
            val dotCount = entry.key - previousIndex - 1
            val rock = if (entry.key > 0) {
                "#"
            } else ""
            (str + ".".repeat(dotCount) + rock + "O".repeat(entry.value)) to (entry.key + entry.value)
        }.first.padEnd(size, '.')
    }
}

// version that works on compact representation
fun List<TreeMap<Int, Int>>.load() =
    this.indices.map { index ->
        this.map {
            val (rockIndex, roundCount) = it.floorEntry(index)
            if (index < rockIndex + roundCount) 1 else 0
        }.sum()
    }.mapIndexed { index, i -> (this.size - index) * i }.sum().toLong()

// version that works on expanded grid
fun List<String>.loadOnBeams() =
    mapIndexed { index, s ->
        (size - index) * s.count { it == 'O' }
    }.sum()


fun main() {

    fun part1(lines: List<String>): Long {
        val tiltedGrid = lines.transpose().tilt()
        return tiltedGrid.load()
    }

    fun part2(lines: List<String>): Long {
        val numCycle = 1000
        val cycled = (0..<(numCycle * 4)).fold(lines) { currentLines, i ->
            val tiltedMap = currentLines.transpose().tilt()
            val newLines = tiltedMap.expand().transpose()
            newLines.rotateRight()
        }

        "Final".println()
        cycled.map { it.println() }
        return cycled.loadOnBeams().toLong()
    }

    part1(readInput("Day14_test")).println()
    part1(readInput("Day14")).println()
    part2(readInput("Day14_test")).println()
    part2(readInput("Day14")).println()

}
