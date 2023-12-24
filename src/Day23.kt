fun main() {
    val dirs = arrayOf(
        Triple(0, 1, '>'),  // →
        Triple(1, 0, 'v'),  // ↓
        Triple(0, -1, '<'), // ←
        Triple(-1, 0, '^'), // ↑
    )

    fun longestPath(map: Array<CharArray>): Int {
        val h = map.size
        val w = map.first().size
        fun straightfoward(
            start: Pair<Int, Int>,
            cost: Int = 0,
            visited: MutableSet<Pair<Int, Int>> = mutableSetOf()
        ): Int {
            val queue = ArrayDeque<Triple<Int, Int, Int>>()
            queue.add(Triple(start.first, start.second, cost))
            while (queue.isNotEmpty()) {
                val (i, j, c) = queue.removeFirst()
                if (i == h - 1 && j == w - 2) {
                    return c
                }
                if (i to j in visited) return -1
                visited.add(i to j)
                val nexts = dirs.map { (di, dj, d) ->
                    Triple(i + di, j + dj, d)
                }.filter { (i2, j2, d) ->
                    i2 in 0 until h && j2 in 0 until w && i2 to j2 !in visited && map[i2][j2] in arrayOf('.', d)
                }
                if (nexts.size == 1) {
                    queue.add(nexts.first().let { (i2, j2, _) -> Triple(i2, j2, c + 1) })
                } else if (nexts.size > 1) {
//                    "$i, $j, $nexts".println()
                    return nexts.maxOf { (i2, j2, _) ->
                        straightfoward(i2 to j2, c + 1, visited.toMutableSet())
                    }
                }
            }
            return -1
        }
        return straightfoward(0 to 1)
    }

    fun part1(input: List<String>): Int {
        val map = input.map { it.toCharArray() }.toTypedArray()
        return longestPath(map)
    }

    fun part2(input: List<String>): Int {
        val map = input.map { row ->
            row.toCharArray().map { c ->
                if (c == '#') c else '.'
            }.toCharArray()
        }.toTypedArray()
        val h = map.size
        val w = map.first().size
        val nodes = (buildList {
            for (i in 0 until h) {
                for (j in 0 until w) {
                    if (map[i][j] == '#') continue
                    val neighbors = dirs.map { (di, dj, _) ->
                        i + di to j + dj
                    }.filter { (i2, j2) ->
                        i2 in 0 until h && j2 in 0 until w && map[i2][j2] == '.'
                    }
                    if (neighbors.size > 2) {
                        add(i to j)
                    }
                }
            }
        } + listOf(0 to 1, h - 1 to w - 2)).mapIndexed { index, p ->
            p to index
        }.toMap()
        val costsMap = Array(nodes.size) { mutableMapOf<Int, Int>() }
        nodes.forEach { (i, j), index ->
            println("node: $i $j")
            val neighbors = dirs.map { (di, dj, _) ->
                i + di to j + dj
            }.filter { (i2, j2) ->
                i2 in 0 until h && j2 in 0 until w && map[i2][j2] == '.'
            }
            neighbors.mapNotNull { (i2, j2) ->
                val visited = mutableMapOf((i to j) to 0)
                val queue = ArrayDeque<Triple<Int, Int, Int>>()
                queue.add(Triple(i2, j2, 1))
                while (queue.isNotEmpty()) {
                    val (i3, j3, c) = queue.removeFirst()
                    if (i3 to j3 in nodes) return@mapNotNull nodes.getValue(i3 to j3) to c
                    if (i3 to j3 in visited) continue
                    visited[i3 to j3] = c
                    val next = dirs.map { (di, dj, _) ->
                        i3 + di to j3 + dj
                    }.first { (i4, j4) ->
                        i4 in 0 until h && j4 in 0 until w && map[i4][j4] == '.' && i4 to j4 !in visited
                    }
                    queue.add(Triple(next.first, next.second, c + 1))
                }
                return@mapNotNull null
            }.let {
                costsMap[index].putAll(it)
            }
        }
        costsMap.forEachIndexed { index, mutableMap ->
            println("$index $mutableMap")
        }
        fun search(start: Int, cost: Int = 0, visited: Set<Int> = setOf()): Int {
            if (start == nodes.size - 1) return cost
            if (start in visited) return -1
            return costsMap[start].filterKeys { it !in visited }.maxOfOrNull { (next, c) ->
                search(next, cost + c, visited + start)
            } ?: -1
        }
        return search(nodes.size - 2)
//        val h = map.size
//        val w = map.first().size
//        val costs = Array(h) { Array(w) { Int.MAX_VALUE } }
//        val queue = PriorityQueue<Triple<Int, Int, Int>> { a, b -> a.third - b.third }
//        val visited = mutableSetOf<Pair<Int, Int>>()
//        queue.add(Triple(0, 1, 0))
//        while (queue.isNotEmpty()) {
//            val (i, j, c) = queue.poll()
//            if (i to j in visited) continue
//            costs[i][j] = c
//            visited.add(i to j)
//            val nexts = dirs.map { (di, dj, _) ->
//                i + di to j + dj
//            }.filter { (i2, j2) ->
//                i2 in 0 until h && j2 in 0 until w && map[i2][j2] == '.' && i2 to j2 !in visited
//            }
//            if (nexts.isEmpty())
//        }
//        return longestPath(map)
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day23_test")
    check(part1(testInput) == 94)
    check(part2(testInput) == 154)

    val input = readInput("Day23")
    part1(input).println()
    part2(input).println()
}
