fun main() {
    fun part1(input: List<String>): Int {
        return input.map { str ->
            str.filter { it in '0'..'9' }.let {
                "${it.first()}${it.last()}".toInt()
            }
        }.sum()
    }

    fun part2(input: List<String>): Int {
        val map = mapOf(
            "one" to '1',
            "two" to '2',
            "three" to '3',
            "four" to '4',
            "five" to '5',
            "six" to '6',
            "seven" to '7',
            "eight" to '8',
            "nine" to '9',
        )
        return input.map { str ->
            val arr = Array<Char?>(str.length) { null }
            str.forEachIndexed { index, c ->
                if (c in '0'..'9') {
                    arr[index] = c
                }
            }
            map.map { (key, value) ->
                key.toRegex().findAll(str).forEach {
                    arr[it.range.first] = value
                }
            }
            arr.filterNotNull().joinToString("")
        }.let {
            part1(it)
        }
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day01_test")
    check(part1(testInput) == 142)
    val testInput2 = readInput("Day01_test2")
    check(part2(testInput2) == 281)

    val input = readInput("Day01")
    part1(input).println()
    part2(input).println()
}
