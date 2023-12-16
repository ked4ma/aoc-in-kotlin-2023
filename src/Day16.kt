fun main() {
    val dirs = arrayOf(
        (0 to 1),  // →
        (1 to 0),  // ↓
        (0 to -1), // ←
        (-1 to 0), // ↑
    )

    fun energize(map: Array<CharArray>, start: Triple<Int, Int, Int>): Int {
        val h = map.size
        val w = map.first().size
        val visited = mutableSetOf<Triple<Int, Int, Int>>()
        val queue = ArrayDeque<Triple<Int, Int, Int>>()
        queue.add(start)
        fun addQueue(data: Triple<Int, Int, Int>, d: Int) {
            queue.addLast(Triple(data.first + dirs[d].first, data.second + dirs[d].second, d))
        }
        while (queue.isNotEmpty()) {
            val data = queue.removeFirst()
            if (data.first !in 0 until h || data.second !in 0 until w || data in visited) continue
            visited.add(data)
            when (map[data.first][data.second]) {
                '.' -> addQueue(data, data.third)
                '|' -> {
                    if (data.third == 0 || data.third == 2) {
                        addQueue(data, 1)
                        addQueue(data, 3)
                    } else {
                        addQueue(data, data.third)
                    }
                }

                '-' -> {
                    if (data.third == 1 || data.third == 3) {
                        addQueue(data, 0)
                        addQueue(data, 2)
                    } else {
                        addQueue(data, data.third)
                    }
                }

                '/' -> {
                    if (data.third == 0 || data.third == 2) {
                        addQueue(data, (data.third + 3) % 4)
                    } else {
                        addQueue(data, (data.third + 1) % 4)
                    }
                }

                '\\' -> {
                    if (data.third == 0 || data.third == 2) {
                        addQueue(data, (data.third + 1) % 4)
                    } else {
                        addQueue(data, (data.third + 3) % 4)
                    }
                }
            }
        }
        return visited.map { (i, j, _) -> i to j }.toSet().size
    }

    fun part1(input: List<String>): Int {
        val map = input.map { it.toCharArray() }.toTypedArray()
        return energize(map, Triple(0, 0, 0))
    }

    fun part2(input: List<String>): Int {
        val h = input.size
        val w = input.first().length
        val map = input.map { it.toCharArray() }.toTypedArray()
        return ((0 until w).flatMap {
            listOf(
                Triple(0, it, 1),
                Triple(h - 1, it, 3),
            )
        } + (0 until h).flatMap {
            listOf(
                Triple(it, 0, 0),
                Triple(it, w - 1, 2),
            )
        }).maxOf {
            energize(map, it)
        }
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day16_test")
    check(part1(testInput) == 46)
    check(part2(testInput) == 51)

    val input = readInput("Day16")
    part1(input).println()
    part2(input).println()
}
