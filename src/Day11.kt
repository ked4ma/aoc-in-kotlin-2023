fun main() {
    fun expandables(input: List<String>): Pair<List<Int>, List<Int>> {
        val expandableRows = input.mapIndexedNotNull { index, s -> if (s.all { it == '.' }) index else null }
        val expandableCols = input[0].indices.mapNotNull { index ->
            if (input.all { it[index] == '.' }) index else null
        }
        return expandableRows to expandableCols
    }

    fun distance(x1: Int, y1: Int, x2: Int, y2: Int, exRows: List<Int>, exCols: List<Int>, k: Int): Long {
        val (xl, xr) = if (x1 <= x2) x1 to x2 else x2 to x1
        val (yl, yr) = if (y1 <= y2) y1 to y2 else y2 to y1
        val exR = exRows.filter { it in xl..xr }
        val exC = exCols.filter { it in yl..yr }
        return (xr - xl) + (yr - yl) + (exR.size + exC.size) * (k - 1).toLong()
    }

    fun slv(input: List<String>, k: Int): Long {
        val g = buildList {
            for (i in input.indices) {
                for (j in input[0].indices) {
                    if (input[i][j] == '#') {
                        add(i to j)
                    }
                }
            }
        }
        val (exRows, exCols) = expandables(input)
        return (0 until g.lastIndex).sumOf { i ->
            (i + 1 until g.size).sumOf { j ->
                distance(g[i].first, g[i].second, g[j].first, g[j].second, exRows, exCols, k)
            }
        }
    }

    fun part1(input: List<String>): Long {
        return slv(input, 2)
    }

    fun part2(input: List<String>): Long {
        return slv(input, 1_000_000)
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day11_test")
    check(slv(testInput, 2) == 374L)
    check(slv(testInput, 10) == 1030L)
    check(slv(testInput, 100) == 8410L)

    val input = readInput("Day11")
    part1(input).println()
    part2(input).println()
}
