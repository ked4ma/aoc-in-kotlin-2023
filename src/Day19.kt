import kotlin.math.max
import kotlin.math.min

fun main() {
    data class Condition(val v: Int, val cond: Char, val n: Int, val dst: Int)
    data class Workflow(val conds: List<Condition>)

    fun parse(input: List<String>): Pair<List<Workflow>, Int> {
        var spl = 0
        while (input[spl].isNotBlank()) {
            spl++
        }
        val splitted = (0 until spl).map {
            val (_, wf, conds) = "(.+)\\{(.+)\\}".toRegex().find(input[it])!!.groupValues
            Triple(it, wf, conds)
        }
        val wfIndex = splitted.associate { (i, wf, _) ->
            wf to i
        }
        return splitted.map { (_, wf, conds) ->
            val list = conds.split(",").map {
                if (it.contains(':')) {
                    val (_, v, cond, n, dst) = "(.)(.)(\\d+):(.+)".toRegex().find(it)!!.groupValues
                    val vn = when (v) {
                        "x" -> 0
                        "m" -> 1
                        "a" -> 2
                        else -> 3 // s
                    }
                    val dstInt = wfIndex.getOrDefault(dst, if (dst == "A") -1 else -2)
                    Condition(vn, cond.first(), n.toInt(), dstInt)
                } else {
                    Condition(0, '>', -1, wfIndex.getOrDefault(it, if (it == "A") -1 else -2))
                }
            }
            Workflow(list)
        } to wfIndex.getValue("in")
    }

    fun part1(input: List<String>): Long {
        val (wfList, start) = parse(input)
        var ans = 0L
        (wfList.size + 1 until input.size).forEach { i ->
            val data = "\\{x=(\\d+),m=(\\d+),a=(\\d+),s=(\\d+)\\}".toRegex()
                .find(input[i])!!.groupValues.subList(1, 5)
                .map(String::toInt)
            var cur = start
            while (cur >= 0) {
                val wf = wfList[cur]
                cur = wf.conds.first {
                    val n = data[it.v]
                    (it.cond == '>' && n > it.n) || (it.cond == '<' && n < it.n)
                }.dst
            }
            if (cur == -1) {
                ans += data.sum()
            }
        }
        return ans
    }

    fun part2(input: List<String>): Long {
        val (wfList, start) = parse(input)
        fun dfs(cur: Int = start, conds: MutableList<Condition> = mutableListOf()): Long {
            if (cur == -2) {
                return 0L
            }
            if (cur == -1) {
                val ranges = Array(4) { 1..4000 }
                conds.forEach {
                    var s = ranges[it.v].first
                    var e = ranges[it.v].last
                    if (it.cond == '<') {
                        e = min(e, it.n - 1)
                    } else {
                        s = max(s, it.n + 1)
                    }
                    ranges[it.v] = s..e
                }
                return ranges.fold(1L) { acc, r -> acc * r.count().toLong() }
            }
            var res = 0L
            wfList[cur].conds.forEach {
                conds.add(it)
                res += dfs(it.dst, conds)
                conds.remove(it)
                conds.add(
                    it.copy(
                        cond = if (it.cond == '>') '<' else '>',
                        n = if (it.cond == '>') it.n + 1 else it.n - 1
                    )
                )
            }
            repeat(wfList[cur].conds.size) {
                conds.removeLast()
            }
            return res
        }
        return dfs()
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day19_test")
    check(part1(testInput) == 19114L)
    check(part2(testInput) == 167409079868000L)

    val input = readInput("Day19")
    part1(input).println()
    part2(input).println()
}
