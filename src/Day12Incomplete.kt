package day12

import println
import readInput


fun main() {
    fun part1(lines: List<String>): Long {
        return lines.map {
            val (field, countStr) = it.split(" ")
            val counts = countStr.split(",").map(String::toInt)
            val countRegex = counts.joinToString("\\.+", "\\.*", "\\.*") { "#{$it}" }.toRegex()
            val countOfUnknown = field.count { it == '?' }

            (0..<(1 shl countOfUnknown)).map { i ->
                val combo = (0..<countOfUnknown).map { (i shr it and 1) }.toList()
//                combo.println()
                val newFiled = generateSequence(field to 0) { (currentField, iCombo) ->
                    if (currentField.indexOf('?') != -1) {
                        currentField.replaceFirst('?', if (combo[iCombo] == 1) '#' else '.') to (iCombo + 1)
                    } else null
                }.last().first
                countRegex.matches(newFiled)//.also { "$newFiled $it".println() }
            }.count { it }

        }.sum().toLong()

    }

//    data class Branch(val segments: List<Pair<Char, IntRange>>, val counts: List<Int>)

    fun Pair<Char, IntRange>.length() =
        second.last - second.first + 1

    fun Pair<Char, IntRange>.type() = first

    data class Branch(val nextIndex: Int, val remainingCounts: List<Int>)

    fun part2(lines: List<String>): Long {
        return lines.map {
            val (originalField, countStr) = it.split(" ")
            val originalCounts = countStr.split(",").map(String::toInt)
            val counts = List(5) { originalCounts }.flatten()
            val field = List(5) { originalField }.joinToString("?")
            generateSequence(listOf(Branch(0, counts))) { branches ->
                val matchedBranches = branches.mapNotNull { branch ->
                    val count = branch.remainingCounts.first()
                    val subField = field.substring(branch.nextIndex, branch.nextIndex + count)
                    if (subField.all { it == '#' || it == '?' }) Branch(
                        branch.nextIndex + count,
                        branch.remainingCounts.drop(1)
                    ) else null
                }
                val advancedBranches = branches.map {branch ->
                    if (field[branch.nextIndex] == '.')
                        listOf(branch.copy(nextIndex = branch.nextIndex + 1))
                    else if (field[branch.nextIndex] == '?'){

                    } else null
                }
                branches
            }
            1L
        }.sum()
    }

//    part1(readInput("Day12_test")).println()
//    part1(readInput("Day12")).println()
    part2(readInput("Day12_test")).println()
//    part2(readInput("Day12")).println()

}

//    val moreSplitter = "(#+)|([?]+)|(\\.+)".toRegex()
//    field.println()
//    val segments = moreSplitter.findAll(field).map { matchResult ->
//        matchResult.value.first() to matchResult.range
//    }.toList()
//    generateSequence(listOf(Branch(segments, counts)) to 0) { (branches, arrangements) ->
//        if (branches.isEmpty()) null else {
//            val finishedBranchesAndResults = branches.mapNotNull { branch ->
//                val firstSegment = branch.segments.firstOrNull()
//                val count = branch.counts.firstOrNull()
//                if (count == null && firstSegment == null) {
//                    branch to 1
//                } else {
//                    if (count == null && branch.segments.any { it.first == '?' }
//                        || firstSegment == null) branch to 0
//                    else
//                        null
//                }
//            }
//            val finishedBranches = finishedBranchesAndResults.map { it.first }.toList()
//            val workingBranches = branches - finishedBranches.toSet()
//            val processedBranches = workingBranches.mapNotNull { branch ->
//                val firstSegment = branch.segments.first()
//                val count = branch.counts.first()
//
//                if (firstSegment.type() == '#') {
//                    if (count == firstSegment.length()) {
//                        val secondSegment = branch.segments.getOrNull(1)
//                        val newSegments = if (secondSegment?.type() == '?')
//                            listOf(
//                                secondSegment.type() to (secondSegment.second.first + 1)..secondSegment.second.last
//                            ) + segments.drop(2)
//                        else segments.drop(1)
//                        listOf(Branch(newSegments, branch.counts.drop(1)))
//                    } else {
//                        //invalid
//                        null
//                    }
//                }
//                if (firstSegment.type() == '.') {
//                    listOf(Branch(branch.segments.drop(1), branch.counts.drop(1)))
//                }
//
//                if (firstSegment.type() == '?'){
//
//                    (1..count).map {
//
//                    }
//                    // count
//                    // ---------
//                    // ?????####
//                    // .....
//                    // ?????.
//
//                }
//
//                listOf(branch)
//            }.flatten()
//
//            processedBranches to (arrangements + finishedBranchesAndResults.sumOf { it.second })
//        }
//    }.last().second.toLong()
//}.sum()
