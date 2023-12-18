import kotlin.math.abs

@OptIn(ExperimentalStdlibApi::class)
fun main() {
    data class Data(val dir: Char, val n: Int)

    fun pick(data: List<Data>): Long {
        var i = 0
        var j = 0
        val map = data.map { (dir, n) ->
            when (dir) {
                'U', '3' -> i -= n
                'R', '0' -> j += n
                'D', '1' -> i += n
                'L', '2' -> j -= n
            }
            i to j
        }
        val b = data.sumOf { it.n.toLong() }
        return (abs(map.mapIndexed { index, (x1, y1) ->
            val (x2, y2) = map[(index + 1) % map.size]
            x1.toLong() * y2 - y1.toLong() * x2
        }.sum()) / 2) + (b / 2) + 1
    }

    fun part1(input: List<String>): Long {
        val data = input.map {
            it.split(" ").let { (d, n, c) ->
                Data(d.first(), n.toInt())
            }
        }
        return pick(data)
    }

    fun part2(input: List<String>): Long {
        val data = input.map {
            it.split(" ").let { (_, _, c) ->
                val n = c.substring(2, 7).hexToInt()
                val dir = when (c.substring(7, 8).first()) {
                    '0' -> 'R'
                    '1' -> 'D'
                    '2' -> 'L'
                    else -> 'U'
                }
                Data(dir, n)
            }
        }
        return pick(data)
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day18_test")
    check(part1(testInput) == 62L)
    check(part2(testInput) == 952408144115L)

    val input = readInput("Day18")
    part1(input).println()
    part2(input).println()
}
