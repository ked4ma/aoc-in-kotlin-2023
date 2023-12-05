fun main() {
    fun parseInput(input: List<String>): Map<String, Pair<String, Map<LongRange, Long>>> {
        val indices = input.mapIndexedNotNull { index, s ->
            if (s.contains("map")) index - 1 else null
        } + input.size
        return indices.windowed(2, 1).associate { (start, end) ->
            val (srcCate, dstCate) = input[start + 1].split(" ").first().split("-to-")
            val map = ((start + 2) until end).associate {
                val (dst, src, len) = input[it].split(" ").map(String::toLong)
                src until src + len to dst
            }
            srcCate to (dstCate to map)
        }
    }

    fun parseCrossPoints(input: List<String>): Map<LongRange, LongRange> {
        val maps = parseInput(input)
        var key = "seed"
        var rangeMap = mapOf((0 until Long.MAX_VALUE) to (0 until Long.MAX_VALUE))

        while (key in maps) {
            val (next, map: Map<LongRange, Long>) = maps.getValue(key)
            val points = map.flatMap { (srcRange, _) -> listOf(srcRange.first, srcRange.last + 1) }

            rangeMap.flatMap { (srcRange, dstRange) ->
                (points.filter {
                    dstRange.first <= it && it <= dstRange.last + 1
                } + listOf(dstRange.first, dstRange.last + 1))
                    .sorted()
                    .distinct()
                    .windowed(2)
                    .map { (first, lastExclusive) ->
                        val len = lastExclusive - first
                        val targetStart = map.firstNotNullOfOrNull {
                            if (first in it.key) {
                                it.value + (first - it.key.first)
                            } else null
                        } ?: first
                        (srcRange.first + (first - dstRange.first)).let { it until it + len } to (targetStart until targetStart + len)
                    }
            }.toMap()
                .also { rangeMap = it }

            key = next
        }
        return rangeMap
    }

    fun getLocation(seed: Long, map: Map<LongRange, LongRange>): Long =
        map.firstNotNullOf { if (seed in it.key) it else null }.let { (src, dst) ->
            dst.first + seed - src.first
        }

    fun part1(input: List<String>): Long {
        val seeds = input.first().split(": ").last().split(" ").map(String::toLong)
        val rangeMap = parseCrossPoints(input)
        return seeds.minOf {
            getLocation(it, rangeMap)
        }
    }

    fun part2(input: List<String>): Long {
        val seeds = input.first().split(": ").last().split(" ").map(String::toLong).windowed(2, 2)
        val rangeMap = parseCrossPoints(input)
        val points = rangeMap.keys.flatMap { listOf(it.first, it.last + 1) }.sorted().distinct()
        return seeds.minOf { (start, len) ->
            val end = start + len
            (points.filter { it in start until end } + listOf(start, end - 1)).minOf { s ->
                getLocation(s, rangeMap)
            }
        }
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day05_test")
    check(part1(testInput) == 35L)
    check(part2(testInput) == 46L)

    val input = readInput("Day05")
    part1(input).println()
    part2(input).println()
}
