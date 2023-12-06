fun main() {
    fun dist(charge: Long, rem: Long): Long {
        return charge * (rem - charge)
    }

    fun part1(input: List<String>): Long {
        val (times, records) = input.map {
            it.split(":").last().trim().split("\\s+".toRegex()).map(String::toLong)
        }
        val races = times.zip(records)
        return races.map { (time, record) ->
            (0..time).count {
                dist(it, time) > record
            }.toLong()
        }.reduce { acc, i -> acc * i }
    }

    fun part2(input: List<String>): Long {
        val (time, record) = input.map { it.split(":").last().replace(" ", "").toLong() }

        fun bs(pred: (Long) -> Boolean): Long {
            var left = 0L
            var right = time + 1L
            while (left < right) {
                val mid = (left + right) / 2
                if (pred(dist(mid, time))) {
                    left = mid + 1
                } else {
                    right = mid
                }
            }
            return left
        }

        val left = bs { it < record }
        val right = bs { it > record }
        return right - left
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day06_test")
    check(part1(testInput) == 288L)
    check(part2(testInput) == 71503L)

    val input = readInput("Day06")
    part1(input).println()
    part2(input).println()
}
