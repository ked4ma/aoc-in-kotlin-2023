fun main() {
    fun parse(input: List<String>): List<Pair<Triple<Double, Double, Double>, Triple<Double, Double, Double>>> {
        return input.map {
            val (ps, vs) = it.split(" @ ")
            val (px, py, pz) = ps.split(",").map(String::toDouble)
            val (vx, vy, vz) = vs.split(",").map(String::toDouble)
            Triple(px, py, pz) to Triple(vx, vy, vz)
        }
    }

    fun part1(input: List<String>, range: ClosedFloatingPointRange<Double>): Int {
        val data = parse(input)
        var ans = 0
        for (i in 0 until data.lastIndex) {
            val (p1, v1) = data[i]
            for (j in i + 1 until data.size) {
                val (p2, v2) = data[j]
                val x =
                    (v1.first * (v2.first * p2.second - v2.second * p2.first) - v2.first * (v1.first * p1.second - v1.second * p1.first)) /
                            (v2.first * v1.second - v1.first * v2.second)
                val y = (v1.second * x + v1.first * p1.second - v1.second * p1.first) / v1.first
                val t1 = (x - p1.first) / v1.first
                val t2 = (x - p2.first) / v2.first
                if (t1 >= 0 && t2 >= 0 && x in range && y in range) {
                    ans++
                }
            }
        }
        return ans
    }

    fun part2(input: List<String>, range: ClosedFloatingPointRange<Double>): Int {
        return 0
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day24_test")
    check(part1(testInput, 7.0..27.0) == 2)
    check(part2(testInput, 7.0..27.0) == 47)

    val input = readInput("Day24")
    part1(input, 200000000000000.0..400000000000000.0).println()
    part2(input, 200000000000000.0..400000000000000.0).println()
}
