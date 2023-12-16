package day15

import println
import readInput


fun String.hash() = fold(0) { acc, c ->
    ((acc + c.code) * 17).mod(256)
}

public operator fun <K, V> LinkedHashMap<out K, V>.plus(pair: Pair<K, V>): LinkedHashMap<K, V> =
    if (this.isEmpty()) linkedMapOf(pair) else LinkedHashMap(this).apply { put(pair.first, pair.second) }
public operator fun <K, V> LinkedHashMap<out K, V>.minus(key: K): LinkedHashMap<K, V> =
    this.toMutableMap().apply { minusAssign(key) } as LinkedHashMap

fun main() {
    fun part1(lines: List<String>): Long {
        return lines.first().split(",").sumOf { it.hash() }.toLong()
    }

    fun part2(lines: List<String>): Long {
        return lines.first().split(",").fold(linkedMapOf<Int, LinkedHashMap<String, Int>>()) { acc, s ->
            val equalsSignIndex = s.indexOf("=")
            val addOperation = equalsSignIndex != -1
            val index = if (equalsSignIndex != -1) {
                equalsSignIndex
            } else {
                s.indexOf("-")
            }
            val label = s.substring(0, index)
            val labelHash = label.hash()
            if (addOperation) {
                val focalLength = s.substring(index + 1, index + 2)

                acc + (labelHash to
                        (acc.getOrElse(labelHash) { linkedMapOf() } + (label to focalLength.toInt()))
                        )
            } else {
                val slots = acc.get(labelHash)
                if (slots == null) {
                    acc
                } else {
                    val newSlots = slots.minus(label)
                    if (newSlots.isEmpty()) {
                        acc.minus(labelHash)
                    } else {
                        acc + (labelHash to newSlots)
                    }
                }
            }
        }.map { box ->
            box.value.toList().mapIndexed { slotIndex, slot ->
                (box.key + 1) * (slotIndex + 1) * slot.second
            }
        }.flatten().sum().toLong()
    }

    part1(readInput("Day15_test")).println()
    part1(readInput("Day15")).println()
    part2(readInput("Day15_test")).println()
    part2(readInput("Day15")).println()

}
