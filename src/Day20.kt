import kotlin.math.max

data class Module(
    val label: String,
    val type: Char,
    var state: Boolean,
    var children: List<String>,
    val parents: MutableList<Pair<String, Boolean>>
)

fun main() {
    fun lcm(a: Long, b: Long): Long {
        val larger = max(a, b)
        var target = larger
        while (target < Long.MAX_VALUE) {
            if (target % a == 0L && target % b == 0L)
                return target
            target += larger
        }
        return 0L
    }

    fun lcm(nums: List<Long>): Long {
        var lcm = nums[0]
        for (num in nums) {
            lcm = lcm(lcm, num)
        }
        return lcm
    }

    fun parseModule(input: String): Module {
        val (l, r) = input.split(" -> ")
        val type = l.first()
        val label = if (type == 'b') l else l.drop(1)
        val children = r.split(", ")
        return Module(label, type, false, children, mutableListOf())
    }

    fun connectChildren(modules: List<Module>) {
        for (module in modules) {
            for (child in module.children) {
                val node = modules.find { it.label == child }
                node?.parents?.add(Pair(module.label, false))
            }
        }
    }

    fun part1(input: List<String>): Long {
        val modules = input.map { parseModule(it) }
            .plus(Module("output", 'o', false, emptyList(), mutableListOf()))
            .plus(Module("rx", 'o', false, emptyList(), mutableListOf()))
        connectChildren(modules)

        var lows = 0L
        var highs = 0L
        for (i in 0 until 1000) {
            val queue = ArrayDeque<Triple<String, String, Boolean>>()
            queue.addLast(Triple("button", "broadcaster", false))
            while (queue.isNotEmpty()) {
                val (from, to, pulse) = queue.removeFirst()
                if (!pulse) lows++ else highs++
                val currentModule = modules.find { it.label == to }!!
                when (currentModule.type) {
                    '%' -> {
                        if (pulse) continue
                        currentModule.state = !currentModule.state
                        for (child in currentModule.children) {
                            queue.addLast(Triple(currentModule.label, child, currentModule.state))
                        }
                    }

                    '&' -> {
                        val parent = currentModule.parents.find { it.first == from }!!
                        currentModule.parents.remove(parent)
                        currentModule.parents.add(Pair(parent.first, pulse))
                        val nextPulse = currentModule.parents.any { !it.second }
                        for (child in currentModule.children) {
                            queue.addLast(Triple(currentModule.label, child, nextPulse))
                        }
                    }

                    'b' -> {
                        for (child in currentModule.children) {
                            queue.addLast(Triple(currentModule.label, child, pulse))
                        }
                    }
                }
            }
        }

        return lows * highs
    }

    fun pruneChildren(modules: List<Module>, keep: String): List<Module> {
        val broadcaster = modules.find { it.label == "broadcaster" }!!
        val newBroadcaster = Module(broadcaster.label, 'b', false, listOf(keep), mutableListOf())
        val newModules = mutableSetOf(newBroadcaster)
        var count = 0
        while (count != newModules.size) {
            count = newModules.size
            for (module in newModules.toList()) {
                for (c in module.children) {
                    newModules.add(modules.find { it.label == c }!!)
                }
            }
        }
        return newModules.toList()
    }

    fun part2(input: List<String>): Long {
        val allModules = input.map { parseModule(it) }
            .plus(Module("output", 'o', false, emptyList(), mutableListOf()))
            .plus(Module("rx", 'o', false, emptyList(), mutableListOf()))

        val broadcaster = allModules.find { it.label == "broadcaster" }!!
        val lcms = mutableListOf<Long>()
        for (cycle in broadcaster.children) {
            val modules = pruneChildren(allModules, cycle)
            for (module in modules) {
                module.parents.clear()
            }
            connectChildren(modules)

            var count = 0L
            var found = false
            while (!found) {
                count++
                val queue = ArrayDeque<Triple<String, String, Boolean>>()
                queue.addLast(Triple("button", "broadcaster", false))
                var rx = 0
                while (queue.isNotEmpty()) {
                    val (from, to, pulse) = queue.removeFirst()
                    if (to == "rx" && !pulse)
                        rx = 1
                    val currentModule = modules.find { it.label == to }!!
                    when (currentModule.type) {
                        '%' -> {
                            if (!pulse) {
                                currentModule.state = !currentModule.state
                                for (child in currentModule.children) {
                                    queue.addLast(Triple(currentModule.label, child, currentModule.state))
                                }
                            }
                        }

                        '&' -> {
                            val parent = currentModule.parents.find { it.first == from }!!
                            currentModule.parents.remove(parent)
                            currentModule.parents.add(Pair(parent.first, pulse))
                            val nextPulse = currentModule.parents.any { !it.second }
                            for (child in currentModule.children) {
                                queue.addLast(Triple(currentModule.label, child, nextPulse))
                            }
                        }

                        'b' -> {
                            for (child in currentModule.children) {
                                queue.addLast(Triple(currentModule.label, child, pulse))
                            }
                        }
                    }
                }
                if (rx == 1) {
                    lcms.add(count)
                    found = true
                }
            }
        }
        return lcm(lcms)
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day20_test")
    check(part1(testInput) == 32000000L)
    val testInput2 = readInput("Day20_test2")
    check(part1(testInput2) == 11687500L)

    val input = readInput("Day20")
    part1(input).println()
    part2(input).println()
}
