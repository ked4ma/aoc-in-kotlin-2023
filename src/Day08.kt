fun main() {
    fun parseInput(input: List<String>): Pair<CharArray, Map<String, Pair<String, String>>> {
        val direction = input[0]
        val map = buildMap {
            for (i in 2 until input.size) {
                val (_, cur, left, right) = "([A-Z0-9]+) = \\(([A-Z0-9]+), ([A-Z0-9]+)\\)".toRegex()
                    .findAll(input[i]).first().groupValues
                put(cur, left to right)
            }
        }
        return direction.toCharArray() to map
    }

    fun calc(
        start: String,
        goal: Regex,
        dir: CharArray,
        map: Map<String, Pair<String, String>>
    ): Long {
        var i = 0L
        var cur = start
        while (!goal.matches(cur)) {
            cur = map.getValue(cur).let {
                if (dir[((i++) % dir.size).toInt()] == 'L') it.first else it.second
            }
        }
        return i
    }

    fun part1(input: List<String>): Long {
        val (dir, map) = parseInput(input)
        return calc("AAA", "ZZZ".toRegex(), dir, map)
    }

    fun part2(input: List<String>): Long {
        val (dir, map) = parseInput(input)
        fun gcd(a: Long, b: Long): Long {
            if (a < b) {
                return gcd(b, a)
            }
            if (b == 0L) return a;
            return gcd(b, a % b)
        }

        fun lcm(a: Long, b: Long): Long {
            val d = gcd(a, b)
            return a / d * b
        }

        return map.keys.filter { it.endsWith('A') }.map { key ->
            calc(key, "..Z".toRegex(), dir, map)
        }.reduce { acc, l -> lcm(acc, l) }
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day08_test")
    check(part1(testInput) == 6L)
    val testInput2 = readInput("Day08_test2")
    check(part2(testInput2) == 6L)

    val input = readInput("Day08")
    part1(input).println()
    part2(input).println()
}
