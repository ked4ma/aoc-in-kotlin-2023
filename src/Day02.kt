import kotlin.math.max

fun main() {
    fun parseId(str: String): Pair<Int, String> {
        return "^Game (\\d+): (.*)$".toRegex().find(str)!!.let {
            it.groups.get(1)!!.value.toInt() to it.groups.last()!!.value
        }
    }

    val colors = mapOf(
        "red" to 12,
        "green" to 13,
        "blue" to 14,
    )

    fun part1(input: List<String>): Int {
        return input.map {
            parseId(it)
        }.filter { (_, data) ->
            val map = mutableMapOf(
                "red" to 0,
                "green" to 0,
                "blue" to 0,
            )
            data.split("; ").forEach { round ->
                round.split(", ").forEach { d ->
                    d.split(" ").let { (num, color) ->
                        map[color] = max(map.getValue(color), num.toInt())
                    }
                }
            }
            map.all { colors.getValue(it.key) - it.value >= 0 }
        }.map {
            it.first
        }.sum()
    }

    fun part2(input: List<String>): Int {
        return input.map {
            parseId(it)
        }.map{ (_, data) ->
            val map = mutableMapOf(
                "red" to 0,
                "green" to 0,
                "blue" to 0,
            )
            data.split("; ").forEach { round ->
                round.split(", ").forEach { d ->
                    d.split(" ").let { (num, color) ->
                        map[color] = max(map.getValue(color), num.toInt())
                    }
                }
            }
            map.values.reduce { acc, i -> acc * i }
        }.sum()
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day02_test")
    check(part1(testInput) == 8)
    check(part2(testInput) == 2286)

    val input = readInput("Day02")
    part1(input).println()
    part2(input).println()
}
