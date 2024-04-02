package day20

import println
import readInput


data class Pulse(val from: String, val to: String, val low: Boolean)

sealed class Module {
    abstract fun process(pulse: Pulse, pulseCount: Long? = null): List<Pulse>
    data class Broadcaster(val modules: List<String>) : Module() {
        override fun process(pulse: Pulse, pulseCount: Long?): List<Pulse> {
            return modules.map { Pulse(from = "broadcaster", to = it, low = pulse.low) }
        }
    }

    data class FlipFlop(val moduleName: String, val modules: List<String>, var onState: Boolean) : Module() {
        override fun process(pulse: Pulse, pulseCount: Long?): List<Pulse> {
            if (pulse.low.not()) return emptyList()
            onState = onState.not()
            return modules.map { destinationModule ->
                if (onState) {
                    Pulse(from = moduleName, to = destinationModule, low = false)
                } else {
                    Pulse(from = moduleName, to = destinationModule, low = true)
                }
            }
        }
    }

    data class Conjunction(
        val moduleName: String,
        val inputModuleStateMap: MutableMap<String, Boolean>,
        val outputModules: List<String>
    ) : Module() {
        private var currentCount = 1
        override fun process(pulse: Pulse, pulseCount: Long?): List<Pulse> {
            inputModuleStateMap[pulse.from] = pulse.low
            val allInputsAreHigh = inputModuleStateMap.values.all { low -> low.not() }

            return outputModules.map {
                if (it == "rx" && inputModuleStateMap.values.count { it.not() } == currentCount) {
                    "found rx for $currentCount at $pulseCount".println()
                    currentCount++
                }
                Pulse(from = moduleName, to = it, low = allInputsAreHigh)
            }
        }
    }
}

fun main() {
    fun loadModules(lines: List<String>): Map<String, Module> {
        val rawModules = lines.map { line ->
            val (typeStr, destinationMoulesStr) = line.split(" -> ")
            val destinationModules = destinationMoulesStr.split(", ")
            val type = typeStr[0]
            val moduleName = if (type == 'b') "broadcaster" else typeStr.substring(1)
            (type to moduleName) to destinationModules
        }
        val outgoingModules = rawModules.map { pair ->
            val (typeToModulePair, destinationModules) = pair
            val (_, moduleName) = typeToModulePair
            destinationModules.map { it to moduleName }
        }.flatten().groupBy { it.first }.mapValues { it.value.map { it.second } }
        val modules = rawModules.associate { pair ->
            val (typeToModulePair, destinationModules) = pair
            val (type, moduleName) = typeToModulePair
            moduleName to when (type) {
                'b' -> Module.Broadcaster(destinationModules)
                '%' -> Module.FlipFlop(moduleName, destinationModules, false)
                '&' -> Module.Conjunction(
                    moduleName,
                    requireNotNull(outgoingModules[moduleName]).associateWith { true }.toMutableMap(),
                    destinationModules
                )

                else -> error("Unknown type $type")
            }
        }
        return modules
    }

    fun part1(lines: List<String>): Long {
        val modules = loadModules(lines)
        var lowPulses = 0L
        var highPulses = 0L
        val queue = ArrayDeque<Pulse>()
        repeat(1000) {
            queue.addLast(Pulse(from = "button", to = "broadcaster", low = true))
            lowPulses++
            while (queue.isNotEmpty()) {
                val pulse = queue.removeFirst()
                modules[pulse.to]?.let { module ->
                    val newPulses = module.process(pulse)
                    newPulses.forEach {
                        "${it.from} -${if (it.low) "low" else "high"}-> ${it.to}".println()
                        if (it.low) {
                            lowPulses++
                        } else {
                            highPulses++
                        }
                    }
                    queue.addAll(newPulses)
                }
            }
        }
        "Low pulses: $lowPulses, high pulses: $highPulses".println()
        return lowPulses * highPulses
    }

    fun part2(lines: List<String>): Long {
        val modules = loadModules(lines)
        var pressCount = 0L
        val queue = ArrayDeque<Pulse>()
        outer@ while (true) {
            queue.addLast(Pulse(from = "button", to = "broadcaster", low = true))
            pressCount++
//            "Press $pressCount".println()
//            modules.forEach { (t, u) ->
//                when (u) {
//                    is Module.FlipFlop -> "FlipFlop ${u.moduleName} is ${u.onState}".println()
//                    is Module.Conjunction -> "Conjunction ${u.moduleName} is ${u.inputModuleStateMap}".println()
//                    else -> {}
//                }
//            }
            modules.filter { it.value is Module.FlipFlop }.mapNotNull { (t, u) ->
                if ((u is Module.FlipFlop)) {
                    if (u.onState) "1" else "0"
                } else null
            }.sorted().joinToString("").println()

            while (queue.isNotEmpty()) {
                val pulse = queue.removeFirst()
                var hasRx = false
                modules[pulse.to]?.let { module ->
                    val newPulses = module.process(pulse, pressCount)
                    hasRx = newPulses.any { it.to == "rx" && it.low }
                    queue.addAll(newPulses)
                }
                if (hasRx) {
                    break@outer
                }
            }
        }
        return pressCount
    }


//    part1(readInput("Day20_test")).println()
//    part1(readInput("Day20")).println()
//    part2(readInput("Day20_test")).println()
    part2(readInput("Day20")).println()

}
