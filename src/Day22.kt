import kotlin.math.max

fun main() {
    data class Brick(val x: IntRange, val y: IntRange, val z: IntRange)

    fun parse(input: List<String>): List<Brick> {
        return input.map {
            val values = "(\\d+),(\\d+),(\\d+)~(\\d+),(\\d+),(\\d+)".toRegex()
                .find(it)!!.groupValues.mapNotNull(String::toIntOrNull)
            Brick(values[0]..values[3], values[1]..values[4], values[2]..values[5])
        }.sortedBy { it.z.first }
    }

    fun fall(bricks: List<Brick>, area: Array<Array<Array<Int>>>): List<Brick> {
        return bricks.mapIndexed { bi, (x, y, z) ->
            var zz = 1
            for (i in x) {
                for (j in y) {
                    var k = z.first
                    while (k > 1 && area[i][j][k - 1] == -1) {
                        k--
                    }
                    zz = max(zz, k)
                }
            }
            Brick(x, y, zz..(zz + z.last - z.first)).also { (x2, y2, z2) ->
                for (i in x2) {
                    for (j in y2) {
                        for (k in z2) {
                            area[i][j][k] = bi
                        }
                    }
                }
            }
        }
    }

    fun deps(bricks: List<Brick>): Pair<Map<Int, Set<Int>>, Map<Int, Set<Int>>> {
        val X = bricks.maxOf { it.x.last }
        val Y = bricks.maxOf { it.y.last }
        val Z = bricks.maxOf { it.z.last }
        val area = Array(X + 1) { Array(Y + 1) { Array(Z + 1) { -1 } } }
        val upwards = bricks.indices.associateWith { mutableSetOf<Int>() }
        val downwards = bricks.indices.associateWith { mutableSetOf<Int>() }
        fall(bricks, area).forEachIndexed { bi, (x, y, z) ->
            for (i in x) {
                for (j in y) {
                    if (area[i][j][z.first - 1] > -1) {
                        downwards.getValue(bi).add(area[i][j][z.first() - 1])
                    }
                    if (z.last + 1 <= Z && area[i][j][z.last + 1] > -1) {
                        upwards.getValue(bi).add(area[i][j][z.last + 1])
                    }
                }
            }
        }
        return downwards to upwards
    }

    fun part1(input: List<String>): Int {
        val bricks = parse(input)
        val essentials = deps(bricks).first
            .mapNotNull { (_, dep) ->
                if (dep.size == 1) dep.first() else null
            }
            .toSet()
        return bricks.size - essentials.size
    }

    fun part2(input: List<String>, test: Int? = null): Int {
        val bricks = parse(input)
        val (downwards, upwards) = deps(bricks)
        fun bfs(biSet: Set<Int>, moves: Set<Int>): Int {
            if (biSet.isEmpty()) return moves.size - 1
            val falls = biSet.mapNotNull { bi ->
                if (downwards.getValue(bi).filterNot { it in moves }.isEmpty()) bi else null
            }
            val next = falls.flatMap { upwards.getValue(it) }.toSet()
            return bfs(next, moves + falls)
        }
        return if (test != null) {
            bfs(upwards.getValue(test), setOf(test))
        } else {
            bricks.indices.sumOf {
                bfs(upwards.getValue(it), setOf(it))
            }
        }
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day22_test")
    check(part1(testInput) == 5)
    check(part2(testInput, 0) == 6)
    check(part2(testInput, 1) == 0)
    check(part2(testInput, 5) == 1)

    val input = readInput("Day22")
    part1(input).println()
    part2(input).println()
}
