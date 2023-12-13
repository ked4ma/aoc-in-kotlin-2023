import kotlin.math.min

fun main() {
    fun toBitDigit(line: String): Int {
        var res = 0
        line.forEach { c ->
            res = res shl 1
            if (c == '#') {
                res += 1
            }
        }
        return res
    }

    fun split(input: List<String>): List<Pair<List<Int>, List<Int>>> {
        return (listOf(-1) + input.indices.filter { input[it].isBlank() } + input.size).windowed(2).map { (l, r) ->
            val block = input.subList(l + 1, r)
            run {
                val map = mutableMapOf<String, Int>()
                block.first().indices.map { i ->
                    val col = block.map { it[i] }.joinToString("")
                    map.putIfAbsent(col, toBitDigit(col))
                    map.getValue(col)
                }
            } to run {
                val map = mutableMapOf<String, Int>()
                block.map {
                    map.putIfAbsent(it, toBitDigit(it))
                    map.getValue(it)
                }
            }
        }
    }

    fun part1(input: List<String>): Long {
        fun verify(list: List<Int>, i: Int): Long? {
            val a = list.subList(0, i).asReversed()
            val b = list.subList(i, list.size)
            return if ((0 until min(a.size, b.size)).all { a[it] == b[it] }) i.toLong() else null
        }
        return split(input).sumOf { (cols, rows) ->
            (1 until cols.size).firstNotNullOfOrNull { i ->
                verify(cols, i)
            } ?: (1 until rows.size).firstNotNullOf { i ->
                verify(rows, i)
            }.let { it * 100 }
        }
    }

    fun part2(input: List<String>): Long {
        fun verify(list: List<Int>, i: Int): Long? {
            val a = list.subList(0, i).asReversed()
            val b = list.subList(i, list.size)
            return if (
                (0 until min(a.size, b.size)).sumOf { (a[it] xor b[it]).countOneBits() } == 1
            ) i.toLong() else null
        }
        return split(input).sumOf { (cols, rows) ->
            (1 until cols.size).firstNotNullOfOrNull { i ->
                verify(cols, i)
            } ?: (1 until rows.size).firstNotNullOf { i ->
                verify(rows, i)
            }.let { it * 100 }
        }
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day13_test")
    check(part1(testInput) == 405L)
    check(part2(testInput) == 400L)

    val input = readInput("Day13")
    part1(input).println()
    part2(input).println()
}
