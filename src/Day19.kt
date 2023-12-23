package day19

import println
import readInput

sealed class Expression {
    data class Rule(
        val field: Char,
        val greater: Boolean,
        val constant: Int,
        val left: Expression,
        val right: Expression
    ) : Expression()

    data class Decision(val accepted: Boolean) : Expression()
}

val regExpSplit = Regex("[<>:]")

fun loadData(lines: List<String>) =
    lines.groupBy { if (it.startsWith("{")) "parts" else if (it.isEmpty()) "blank" else "rules" }

fun loadRules(input: Map<String, List<String>>) =
    requireNotNull(input["rules"]).associate { line ->
        val items = line.split("{")
        val ruleName = items[0]
        val rule = items[1].dropLast(1)
        ruleName to rule
    }

fun parseExpressionTree(rulesMap: Map<String, String>, ruleStr: String): Expression {
    val rules = ruleStr.split(",")
    val rule = rules[0]
    if (rule.startsWith("A") || rule.startsWith("R")) {
        return Expression.Decision(rule.startsWith("A"))
    }
    if (rule.contains("<") || rule.contains(">")) {
        val (fieldStr, constantStr, trueExpression) = rule.split(regExpSplit)
        return Expression.Rule(
            fieldStr[0],
            rule.contains(">"),
            constantStr.toInt(),
            parseExpressionTree(rulesMap, trueExpression),
            parseExpressionTree(
                rulesMap,
                List(rules.size - 1) { rules[it + 1] }.joinToString(",")
            )
        )
    }
    return parseExpressionTree(rulesMap, requireNotNull(rulesMap[rule]))
}

fun evaluate(expression: Expression, partMap: Map<Char, Int>): Boolean {
    return when (expression) {
        is Expression.Rule -> {
            if (partMap.containsKey(expression.field)) {
                val value = requireNotNull(partMap[expression.field])
                val matchedResult = if (expression.greater) {
                    value > expression.constant
                } else {
                    value < expression.constant
                }
                if (matchedResult) {
                    evaluate(expression.left, partMap)
                } else {
                    evaluate(expression.right, partMap)
                }
            } else {
                evaluate(expression.right, partMap)
            }
        }

        is Expression.Decision -> expression.accepted
    }
}

fun cutRange(range: IntRange, point: Int, greater: Boolean): Pair<IntRange, IntRange> {
    return if (range.contains(point)) {
        val lowerRange = range.first..(point + if (greater) 0 else -1)
        val upperRange = (point + (if (greater) 1 else 0))..range.last
        lowerRange to upperRange
    } else {
        if (range.first > point) {
            IntRange.EMPTY to range
        } else {
            range to IntRange.EMPTY
        }
    }
}

fun evaluateCombinations(expression: Expression, partMap: Map<Char, IntRange>): Long {
    return when (expression) {
        is Expression.Rule -> {
            val range = requireNotNull(partMap[expression.field])
            val (lowerRange, upperRange) = cutRange(range, expression.constant, expression.greater)
            val partMapForLowerRange = partMap + (expression.field to lowerRange)
            val partMapForUpperRange = partMap + (expression.field to upperRange)
            if (expression.greater) {
                evaluateCombinations(expression.left, partMapForUpperRange) +
                        evaluateCombinations(expression.right, partMapForLowerRange)
            } else {
                evaluateCombinations(expression.left, partMapForLowerRange) +
                        evaluateCombinations(expression.right, partMapForUpperRange)
            }
        }

        is Expression.Decision -> if (expression.accepted) {
            partMap.values.map { (it.last - it.first + 1).toLong() }.reduce(Long::times)
        } else {
            0
        }
    }
}

fun simplifyTree(expression: Expression): Expression {
    return when (expression) {
        is Expression.Rule -> {
            val left = simplifyTree(expression.left)
            val right = simplifyTree(expression.right)
            if (left is Expression.Decision && right is Expression.Decision &&
                (left.accepted && right.accepted || (!left.accepted && !right.accepted))
            ) {
                left
            } else
                Expression.Rule(expression.field, expression.greater, expression.constant, left, right)
        }

        is Expression.Decision -> expression
    }
}

fun main() {
    fun part1(lines: List<String>): Long {
        val input = loadData(lines)
        val rulesMap = loadRules(input)
        val expressionTree = simplifyTree(parseExpressionTree(rulesMap, "in"))
        val parts = input["parts"]!!
        return parts.sumOf { part ->
            val partMap = part.removeSurrounding("{", "}").split(",").associate {
                val (key, value) = it.split("=")
                key[0] to value.toInt()
            }
            if (evaluate(expressionTree, partMap)) {
                partMap.values.sum()
            } else {
                0
            }
        }.toLong()
    }


    fun part2(lines: List<String>): Long {
        val input = loadData(lines)
        val rulesMap = loadRules(input)
        val expressionTree = simplifyTree(parseExpressionTree(rulesMap, "in"))
        val rangeMap = "xmas".map { it to 1..4000 }.toMap()
        return evaluateCombinations(expressionTree, rangeMap)
    }
    part1(readInput("Day19_test")).println()
    part1(readInput("Day19")).println()
    part2(readInput("Day19_test")).println()
    part2(readInput("Day19")).println()

}
