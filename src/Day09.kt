fun main() {
    fun diff(l: List<Long>): List<List<Long>> {
        val list = mutableListOf<List<Long>>()
        var cur = l
        while (cur.any { it != 0L }) {
            list.add(cur)
            cur = cur.windowed(2).map { (a, b) -> b - a }
        }
        return list
    }

    fun part1(input: List<String>): Long {
        return input.map { i -> i.split(" ").map { it.toLong() } }.sumOf {
            val list = diff(it)
            list.map(List<Long>::last).asReversed().fold(0L) { acc, l -> acc + l }
        }
    }

    fun part2(input: List<String>): Long {
        return input.map { i -> i.split(" ").map { it.toLong() } }.sumOf {
            val list = diff(it)
            list.map(List<Long>::first).asReversed().fold(0L) { acc, l -> l - acc }
        }
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day09_test")
    check(part1(testInput) == 114L)
    check(part2(testInput) == 2L)

    val input = readInput("Day09")
    part1(input).println()
    part2(input).println()
}
