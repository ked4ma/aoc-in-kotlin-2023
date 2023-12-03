fun main() {
    fun part1(input: List<String>): Int {
        return buildList {
            for (i in input.indices) {
                "\\d+".toRegex().findAll(input[i]).filter { match ->
                    ((i - 1).coerceAtLeast(0)..(i + 1).coerceAtMost(input.lastIndex)).any { j ->
                        ((match.range.first - 1).coerceAtLeast(0)..(match.range.last + 1).coerceAtMost(input[i].lastIndex)).any { k ->
                            input[j][k] !in '0'..'9' && input[j][k] != '.'
                        }
                    }
                }.forEach {
                    add(it.value.toInt())
                }
            }
        }.sum()
    }

    fun part2(input: List<String>): Int {
        val map = mutableMapOf<Pair<Int, Int>, MutableList<Int>>()
        for (i in input.indices) {
            "\\d+".toRegex().findAll(input[i]).forEach { match ->
                for (j in (i - 1).coerceAtLeast(0)..(i + 1).coerceAtMost(input.lastIndex)) {
                    for (k in (match.range.first - 1).coerceAtLeast(0)..(match.range.last + 1).coerceAtMost(input[i].lastIndex)) {
                        if (input[j][k] == '*') {
                            map.putIfAbsent(j to k, mutableListOf())
                            map.getValue(j to k).add(match.value.toInt())
                        }
                    }
                }
            }
        }

        return map.values.filter { it.size == 2 }.sumOf { (a, b) -> a * b }
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day03_test")
    check(part1(testInput) == 4361)
    check(part2(testInput) == 467835)

    val input = readInput("Day03")
    part1(input).println()
    part2(input).println()
}
