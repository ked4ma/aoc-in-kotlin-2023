fun main() {
    val cardPointMap = mapOf(
        '2' to 2,
        '3' to 3,
        '4' to 4,
        '5' to 5,
        '6' to 6,
        '7' to 7,
        '8' to 8,
        '9' to 9,
        'T' to 10,
        'J' to 11,
        'Q' to 12,
        'K' to 13,
        'A' to 14,
    )

    fun parseTypeStrength(card: CharArray, hasJoker: Boolean = false): Int {
        val jokers = if (hasJoker) {
            card.count { it == 'J' }
        } else 0
        val numCounts =
            (card.filter { !hasJoker || it != 'J' }.toList().groupingBy { it }
                .eachCount().values.takeIf { it.isNotEmpty() } ?: listOf(0))
                .sortedDescending()
                .mapIndexed { index, i -> if (index == 0) i + jokers else i }.takeIf { it.isNotEmpty() } ?: listOf(0)
        if (numCounts.sum() != 5) {
            println(card)
            println(numCounts)
        }
        return when (numCounts) {
            listOf(5) -> 6 // five
            listOf(4, 1) -> 5 // four
            listOf(3, 2) -> 4 // full house
            listOf(3, 1, 1) -> 3// three
            listOf(2, 2, 1) -> 2 // two pair
            listOf(2, 1, 1, 1) -> 1 // one pair
            listOf(1, 1, 1, 1, 1) -> 0 // high card
            else -> 0
        }
    }

    fun parseInput(input: List<String>, hasJoker: Boolean = false): List<Triple<List<Int>, Int, Long>> = input.map {
        it.split(" ").let { (cards, bid) ->
            Triple(
                cards.toCharArray().map { c ->
                    if (hasJoker && c == 'J') 1 else cardPointMap.getValue(c)
                },
                parseTypeStrength(cards.toCharArray(), hasJoker),
                bid.toLong()
            )
        }
    }

    fun Triple<List<Int>, Int, Long>.compareTo(other: Triple<List<Int>, Int, Long>): Int {
        if (this.second != other.second) return this.second - other.second
        for (i in 0 until 5) {
            if (this.first[i] == other.first[i]) continue
            return (this.first[i] - other.first[i])
        }
        return 0
    }

    fun part1(input: List<String>): Long {
        val data = parseInput(input).sortedWith { a, b ->
            a.compareTo(b)
        }
        return data.mapIndexed { index, (_, _, bed) ->
            (index + 1) * bed
        }.sum()
    }

    fun part2(input: List<String>): Long {
        val data = parseInput(input, true).sortedWith { a, b ->
            a.compareTo(b)
        }
        return data.mapIndexed { index, (_, _, bed) ->
            (index + 1) * bed
        }.sum()
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day07_test")
    check(part1(testInput) == 6440L)
    check(part2(testInput) == 5905L)

    val input = readInput("Day07")
    part1(input).println()
    part2(input).println()
}
