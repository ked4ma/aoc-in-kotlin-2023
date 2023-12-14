fun main() {
    fun tilt(arr: Array<Array<Char>>, dir: Pair<Int, Int>) {
        val h = arr.size
        val w = arr.first().size
        val (iRange, jRange) = mapOf(
            (1 to 0) to ((0 until h) to (0 until w)),
            (-1 to 0) to ((h - 1 downTo 0) to (0 until w)),
            (0 to 1) to ((0 until h) to (0 until w)),
            (0 to -1) to ((0 until h) to (w - 1 downTo 0)),
        ).getValue(dir)
        iRange.forEach { i ->
            jRange.forEach { j ->
                if (arr[i][j] == 'O' || arr[i][j] == '#') return@forEach
                var (i2, j2) = (i + dir.first to j + dir.second)
                while (i2 in 0 until h && j2 in 0 until w && arr[i2][j2] == '.') {
                    i2 += dir.first
                    j2 += dir.second
                }
                if (i2 in 0 until h && j2 in 0 until w && arr[i2][j2] == 'O') {
                    arr[i][j] = 'O'
                    arr[i2][j2] = '.'
                }
            }
        }
    }

    fun part1(input: List<String>): Long {
        val arr = input.map { it.toCharArray().toTypedArray() }.toTypedArray()
        tilt(arr, 1 to 0)
        return arr.reversedArray().mapIndexed { index, chars ->
            (chars.count { it == 'O' } * (index + 1)).toLong()
        }.sum()
    }

    fun part2(input: List<String>): Long {
        val arr = input.map { it.toCharArray().toTypedArray() }.toTypedArray()
        val states = mutableListOf(
            arr.joinToString("") {
                it.joinToString("")
            }
        )
        for (i in 0 until 1_000_000_000) {
            tilt(arr, 1 to 0)
            tilt(arr, 0 to 1)
            tilt(arr, -1 to 0)
            tilt(arr, 0 to -1)
            val next = arr.joinToString(",") {
                it.joinToString("")
            }
            if (states.contains(next)) {
                break
            }
            states.add(next)
        }
        val cur = arr.joinToString(",") {
            it.joinToString("")
        }
        val index = states.indexOf(cur)
        return states[((1_000_000_000 - (index + 1)) % (states.size - index)) + index + 1]
            .split(",").reversed()
            .mapIndexed { i, s ->
                s.count { it == 'O' } * (i + 1).toLong()
            }.sum()
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day14_test")
    check(part1(testInput) == 136L)
    check(part2(testInput) == 64L)

    val input = readInput("Day14")
    part1(input).println()
    part2(input).println()
}
