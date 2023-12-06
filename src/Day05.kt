package day05

import println
import readInput
import kotlin.math.max
import kotlin.math.min

fun LongRange.commonWith(other: LongRange): LongRange? {
    val start = max(start, other.start)
    val last = min(last, other.last)
    if (start > last) return null
    return start..last
}

fun LongRange.mapToDestination(dest: Long) = dest..(last - first + dest)

fun main() {

    fun findLocationMap(maps: List<List<Pair<LongRange, Long>>>, i: Int, curr: Long): Long {
        if (i == maps.size) return curr
        val map = maps[i]
        val newCurr = map.firstOrNull { (sourceRange, _) ->
            sourceRange.contains(curr)
        }?.let { it.second + curr - it.first.first } ?: curr
        return findLocationMap(maps, i + 1, newCurr)
    }

    fun findLocationFast(maps: List<List<Pair<LongRange, Long>>>, i: Int, currRange: LongRange): Long {
        "Current range is $currRange".println()
        if (i == maps.size) return currRange.first
        val map = maps[i]
        val matchedRanges = map.mapNotNull { (range, dest) ->
            "Matching $range ($dest) with $currRange is ${range.commonWith(currRange)}".println()
            range.commonWith(currRange)?.let { it to (dest + it.first - range.first) }
        }
        val outsideRanges = buildList {
            val firstMatchedRangeStart = matchedRanges.firstOrNull()?.first?.start
            val firstRange = if (firstMatchedRangeStart == null) {
                currRange.start..currRange.last
            } else {
                if (firstMatchedRangeStart > currRange.start) {
                    currRange.start..firstMatchedRangeStart.minus(1)
                } else null
            }
            firstRange?.let {
                "outside first range $firstRange".println()
                add(firstRange to firstRange.start)
            }

            addAll(matchedRanges.mapIndexedNotNull { index, pair ->
                if (index != matchedRanges.lastIndex) {
                    val nextPair = matchedRanges[index + 1]
                    val aRange = pair.first
                    val bRange = nextPair.first
                    val inRangeStart = aRange.last + 1
                    val inRangeLast = bRange.start - 1
                    if (inRangeLast - inRangeStart > 0) {
                        ((inRangeStart..inRangeLast) to inRangeStart).also { "in range $it".println() }
                    } else null
                } else null
            })
            val lastRange = (matchedRanges.lastOrNull()?.first?.last?.plus(1) ?: currRange.last)..currRange.last
            if (lastRange.start < lastRange.last) {
                "outside last range $lastRange".println()
                add(lastRange to lastRange.start)
            }
        }
        (matchedRanges + outsideRanges).println()
        return (matchedRanges + outsideRanges).map {
            findLocationFast(
                maps,
                i + 1,
                it.first.mapToDestination(it.second)
            )
        }
            .min().also { "Min $it".println() }
    }


    fun part1(m: List<String>): Long {
        val seeds = m[0].replace("seeds: ", "").split(" ").map { it.toLong() }
        val maps = m.drop(1).fold(emptyList<List<String>>()) { acc, s ->
            if (s.isEmpty() || s.contains("map")) {
                acc + listOf(emptyList())
            } else {
                val tail = listOf((acc.lastOrNull() ?: emptyList()) + s)
                acc.dropLast(1) + tail
            }
        }.filter { it.isNotEmpty() }.map {
            it.map {
                val (dest, source, range) = it.split(" ").map { it.toLong() }
                (source..<(source + range)) to dest
            }
        }
        return seeds.map { seed ->
            findLocationMap(maps, 0, seed)
        }.min()

    }


    fun part2(m: List<String>): Long {
        val seeds = m[0].replace("seeds: ", "").split(" ").map { it.toLong() }.chunked(2).map {
            it[0]..<(it[0] + it[1])
        }
        val maps = m.drop(1).fold(emptyList<List<String>>()) { acc, s ->
            if (s.isEmpty() || s.contains("map")) {
                acc + listOf(emptyList())
            } else {
                val tail = listOf((acc.lastOrNull() ?: emptyList()) + s)
                acc.dropLast(1) + tail
            }
        }.filter { it.isNotEmpty() }.map {
            it.map {
                val (dest, source, range) = it.split(" ").map { it.toLong() }
                (source..<(source + range)) to dest
            }
        }.map { it.sortedBy { it.first.last } }
        return seeds.map { seedRange ->
            findLocationFast(maps, 0, seedRange)
        }.min()

    }

    part1(readInput("Day05_test")).println()
    part1(readInput("Day05")).println()
    part2(readInput("Day05_test")).println()
    part2(readInput("Day05")).println()

}
