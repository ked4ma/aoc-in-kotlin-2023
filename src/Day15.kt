fun main() {
    fun hash(str: String): Int = str.map { it.code }.fold(0) { acc, n -> (acc + n).times(17).rem(256) }

    fun part1(input: List<String>): Int {
        return input.first().split(",").sumOf { hash(it) }
    }

    fun part2(input: List<String>): Int {
        val arr: Array<HashMap<String, Int>> = Array(256) { linkedMapOf() }
        input.first().split(",").forEach {
            if (it.last() == '-') {
                val key = it.substring(0, it.lastIndex)
                val box = hash(key)
                arr[box].remove(key)
            } else { // xxx=n
                val key = it.substring(0, it.length - 2)
                val box = hash(key)
                val num = it.last().digitToInt()
                arr[box][key] = num
            }
        }
        return arr.mapIndexed { i, box ->
            box.toList().mapIndexed { j, (_, n) -> (i + 1) * (j + 1) * n }.sum()
        }.sum()
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day15_test")
    check(part1(testInput) == 1320)
    check(part2(testInput) == 145)

    val input = readInput("Day15")
    part1(input).println()
    part2(input).println()
}
