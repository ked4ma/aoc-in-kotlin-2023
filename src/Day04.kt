fun main() {
    fun parseCard(card: String): Pair<List<Int>, List<Int>> {
        val (_, numbers) = card.split(":")
        return numbers.split("|").let { (left, right) ->
            left.trim().split("\\s+".toRegex()).map { it.trim().toInt() } to
                    right.trim().split("\\s+".toRegex()).map { it.trim().toInt() }
        }
    }

    fun part1(input: List<String>): Long {
        return input.map {
            parseCard(it)
        }.map { (wn, nums) ->
            val set = wn.toSet()
            val count = nums.count { it in set }
            if (count == 0) return@map 0L
            (1 until count).fold(1L) { acc, _ -> acc * 2 }
        }.sum()
    }

    fun part2(input: List<String>): Long {
        val arr = LongArray(input.size) { 1L }
        input.map {
            parseCard(it)
        }.forEachIndexed { index, (wn, nums) ->
            val set = wn.toSet()
            val count = nums.count { it in set }
            if (count == 0) return@forEachIndexed
            for (j in index + 1..(index + count).coerceAtMost(input.lastIndex)) {
                arr[j] = arr[j] + arr[index]
            }
        }
        return arr.sum()
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day04_test")
    check(part1(testInput) == 13L)
    check(part2(testInput) == 30L)

    val input = readInput("Day04")
    part1(input).println()
    part2(input).println()
}
