import kotlin.math.max

fun main() {
    fun startPoint(map: Array<CharArray>): Pair<Int, Int> {
        map.forEachIndexed { i, row ->
            row.forEachIndexed { j, c ->
                if (c == 'S') return i to j
            }
        }
        return -1 to -1
    }

    val dirs = arrayOf(
        0 to 1,  // →
        1 to 0,  // ↓
        0 to -1, // ←
        -1 to 0, // ↑
    )

    fun part1(input: List<String>, steps: Int): Long {
        val map = input.map { it.toCharArray() }.toTypedArray()
        val h = map.size
        val w = map.first().size
        val queue = ArrayDeque<Pair<Int, Int>>()
        queue.add(startPoint(map))
        repeat(steps) {
            buildSet {
                repeat(queue.size) {
                    val (i, j) = queue.removeFirst()
                    dirs.forEach {
                        val i2 = i + it.first
                        val j2 = j + it.second
                        if (i2 in 0 until h && j2 in 0 until w && map[i2][j2] != '#') {
                            add(i2 to j2)
                        }
                    }
                }
            }.forEach(queue::addLast)
        }
        return queue.size.toLong()
    }
//
//    fun part2(input: List<String>, steps: Int): Long {
//        val map = input.map { it.toCharArray() }.toTypedArray()
//        val h = map.size
//        val w = map.first().size
//        val queue = ArrayDeque<Triple<Int, Int, Set<Pair<Int, Int>>>>()
//        val start = startPoint(map)
//        queue.add(Triple(start.first, start.second, setOf(0 to 0)))
//        repeat(steps) {
//            val nexts = mutableMapOf<Pair<Int, Int>, MutableSet<Pair<Int, Int>>>()
//            while (queue.isNotEmpty()) {
//                val (i, j, n) = queue.removeFirst()
//                dirs.forEach {
//                    val i2 = i + it.first
//                    val j2 = j + it.second
//                    nexts.putIfAbsent(i2 to j2, mutableSetOf())
//                    nexts.getValue(i2 to j2).add(i2 % h to j2 % w)
//                }
//            }
//            val nexts = buildMap {
//                while (queue.isNotEmpty()) {
//                    val (i, j, n) = queue.removeFirst()
//                    dirs.forEach {
//                        val i2 = i + it.first
//                        val j2 = j + it.second
//                        val prev: Long = getOrDefault(i2 to j2, 0L)
//                        if (map[((i2 % h) + h) % h][((j2 % w) + w) % w] != '#') {
//                            put(i2 to j2, max(prev, n))
//                        }
//                    }
//                }
//            }
//        }
//        return queue.fold(0L) { acc, d -> acc + d.third }
//            .apply { println() }
//    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day21_test")
    check(part1(testInput, 6) == 16L)
//    check(part2(testInput, 6) == 16L)
//    check(part2(testInput, 10) == 50L)
//    check(part2(testInput, 50) == 1594L)
//    check(part2(testInput, 100) == 6536L)
//    check(part2(testInput, 500) == 167004L)
//    check(part2(testInput, 1000) == 668697L)
//    check(part2(testInput, 5000) == 16733044L)

    val input = readInput("Day21")
    part1(input, 64).println()
//    part2(input, 26501365).println()
}
