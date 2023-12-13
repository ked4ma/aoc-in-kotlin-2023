fun main() {

    fun dfs(data: String, groups: List<Int>): Long {
        val map = mutableMapOf<Triple<Int, Int, List<Int>>, Long>()
        fun _dfs(index: Int, cur: Int, rem: List<Int>): Long {
            val key = Triple(index, cur, rem)
            if (key in map) return map.getValue(key)
            if (index == data.length) {
                return if ((rem.isEmpty() && cur == 0) || (rem.size == 1 && cur == rem.first())) 1L else 0L
            }
            val c = data[index]
            fun procDot(): Long = if (cur > 0) {
                if (rem.isNotEmpty() && cur == rem.first()) {
                    _dfs(index + 1, 0, rem.subList(1, rem.size))
                } else 0L
            } else _dfs(index + 1, cur, rem)

            fun procHash(): Long =
                if (rem.isEmpty() || (cur + 1 > rem.first())) 0L else _dfs(index + 1, cur + 1, rem)

            val res = when (c) {
                '.' -> procDot()
                '#' -> procHash()
                else -> procDot() + procHash()
            }
            map[key] = res
            return res
        }
        return _dfs(0, 0, groups)
    }

    fun part1(input: List<String>): Long {
        return input.sumOf {
            val (data, groupStr) = it.split(" ")
            val groups = groupStr.split(",").map(String::toInt)
            dfs(data, groups)
        }
    }

    fun part2(input: List<String>): Long {
        return input.sumOf {
            val (data, groupStr) = it.split(" ")
            val groups = groupStr.split(",").map(String::toInt)
            dfs(
                Array(5) { data }.joinToString("?"),
                Array(5) { groups }.reduce { acc, ints -> acc + ints }
            )
        }
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day12_test")
    check(part1(testInput) == 21L)
    check(part2(testInput) == 525152L)

    val input = readInput("Day12")
    part1(input).println()
    part2(input).println()
}
