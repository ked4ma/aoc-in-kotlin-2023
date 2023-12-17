import java.util.*
import kotlin.math.min

fun main() {
    val dirs = arrayOf(
        (0 to 1),  // →
        (1 to 0),  // ↓
        (0 to -1), // ←
        (-1 to 0), // ↑
    )

    fun solve(arr: List<List<Int>>, atLeast: Int, atMost: Int): Int {
        val h = arr.size
        val w = arr.first().size
        val visited = mutableSetOf<List<Int>>()
        val map = mutableMapOf<List<Int>, Int>()
        val queue = PriorityQueue<IntArray> { a, b -> a[0] - b[0] }
        queue.add(intArrayOf(0, 0, 0, -1)) // cost, i, j, dir
        while (queue.isNotEmpty()) {
            val (cost, i, j, dir) = queue.poll()

            if (i == h - 1 && j == w - 1) return cost

            if (i !in 0 until h || j !in 0 until w || listOf(i, j, dir) in visited) continue
            visited.add(listOf(i, j, dir))

            dirs.forEachIndexed { index, d ->
                if (index == dir || (index + 2) % 4 == dir) return@forEachIndexed
                var c = cost
                for (cnt in 1..atMost) {
                    val i2 = i + d.first * cnt
                    val j2 = j + d.second * cnt
                    if (i2 !in 0 until h || j2 !in 0 until w) break
                    c += arr[i2][j2]
                    if (cnt < atLeast) continue
                    val key = listOf(i2, j2, index)
                    if (map.getOrDefault(key, Int.MAX_VALUE) <= c) continue
                    map[key] = c
                    queue.offer(intArrayOf(c, i2, j2, index))
                }
            }
        }
        return -1
    }

    fun part1(input: List<String>): Int {
        val arr = input.map { it.map(Char::digitToInt) }
        return solve(arr, 1, 3)
    }

    fun part2(input: List<String>): Int {
        val arr = input.map { it.map(Char::digitToInt) }
        return solve(arr, 4, 10)
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day17_test")
    check(part1(testInput) == 102)
    check(part2(testInput) == 94)

    val input = readInput("Day17")
    part1(input).println()
    part2(input).println()
}
