fun main() {
    data class Node(val x: Int, val y: Int) {
        private val _links = mutableSetOf<Node>()
        val links: Set<Node> = _links
        fun addLink(node: Node) {
            _links.add(node)
        }
    }

    fun parse(input: Array<Array<Char>>): List<List<Node>> {
        val nodes = input.mapIndexed { i, s ->
            s.mapIndexed { j, _ -> Node(i, j) }
        }
        val h = input.size
        val w = input[0].size
        fun checkTop(i: Int, j: Int) {
            if (i > 0 && input[i - 1][j] in listOf('|', '7', 'F')) {
                nodes[i][j].addLink(nodes[i - 1][j])
            }
        }

        fun checkBottom(i: Int, j: Int) {
            if (i < h - 1 && input[i + 1][j] in listOf('|', 'L', 'J')) {
                nodes[i][j].addLink(nodes[i + 1][j])
            }
        }

        fun checkLeft(i: Int, j: Int) {
            if (j > 0 && input[i][j - 1] in listOf('-', 'L', 'F')) {
                nodes[i][j].addLink(nodes[i][j - 1])
            }
        }

        fun checkRight(i: Int, j: Int) {
            if (j < w - 1 && input[i][j + 1] in listOf('-', 'J', '7')) {
                nodes[i][j].addLink(nodes[i][j + 1])
            }
        }
        input.forEachIndexed { i, s ->
            s.forEachIndexed { j, c ->
                when (c) {
                    '|' -> {
                        checkTop(i, j)
                        checkBottom(i, j)
                    }

                    '-' -> {
                        checkLeft(i, j)
                        checkRight(i, j)
                    }

                    'L' -> {
                        checkTop(i, j)
                        checkRight(i, j)
                    }

                    'J' -> {
                        checkTop(i, j)
                        checkLeft(i, j)
                    }

                    '7' -> {
                        checkLeft(i, j)
                        checkBottom(i, j)
                    }

                    'F' -> {
                        checkRight(i, j)
                        checkBottom(i, j)
                    }
                }
            }
        }
        return nodes
    }

    fun findLoop(input: Array<Array<Char>>, sx: Int, sy: Int): Set<Node> {
        for (s in listOf('|', '-', 'L', 'J', '7', 'F')) {
            input[sx][sy] = s
            val nodes = parse(input)
            val visited = mutableSetOf<Node>()
            val queue = ArrayDeque<Pair<Node, Node>>()
            visited.add(nodes[sx][sy])
            nodes[sx][sy].links.forEach { n ->
                queue.add(n to nodes[sx][sy])
            }
            while (queue.isNotEmpty()) {
                val (n, p) = queue.removeFirst()
                visited.add(n)
                val list = nodes[n.x][n.y].links.filterNot { it.x == p.x && it.y == p.y }
                if (list.isEmpty()) continue
                val nn = list.first()
                if (nn in visited) {
                    return visited
                }
                queue.addLast(nn to n)
            }
        }
        return emptySet()
    }

    fun part1(input: List<String>): Int {
        val arr = input.map { it.toCharArray().toTypedArray() }.toTypedArray()
        val x = input.indexOfFirst { 'S' in it }
        val y = input[x].indexOfFirst { it == 'S' }
        return findLoop(arr, x, y).size / 2
    }

    fun part2(input: List<String>): Int {
        val arr = input.map { it.toCharArray().toTypedArray() }.toTypedArray()
        val x = input.indexOfFirst { 'S' in it }
        val y = input[x].indexOfFirst { it == 'S' }
        val loop = findLoop(arr, x, y)
        val nodes = parse(arr)
        var count = 0
        for (i in arr.indices) {
            var io = false
            var ioC = '.'
            for (j in arr[0].indices) {
                if (nodes[i][j] in loop) {
                    if (arr[i][j] == '-') continue
                    if (arr[i][j] in arrayOf('|', 'L', 'F')) {
                        io = !io
                        ioC = arr[i][j]
                    } else if ((arr[i][j] == 'J' && ioC == 'L') || (arr[i][j] == '7' && ioC == 'F')) {
                        io = !io
                    }
                } else if (io) {
                    count++
                }
            }
        }
        return count
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day10_test")
    check(part1(testInput) == 8)
    val testInput2 = readInput("Day10_test2")
    check(part2(testInput2) == 8)

    val input = readInput("Day10")
    part1(input).println()
    part2(input).println()
}
