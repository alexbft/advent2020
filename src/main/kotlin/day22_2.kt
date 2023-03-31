package my.advent2020.day22part2

import java.io.File

private data class GameResult(val winner: Int, val winningDeck: List<Int>)

private data class GameState(val decks: List<List<Int>>)

private fun play(decks: List<List<Int>>): GameResult {
    val playerDecks = decks.map { ArrayDeque(it) }
    val prevStates = mutableSetOf<GameState>()
    while (playerDecks.all { deck -> deck.isNotEmpty() }) {
        val state = GameState(playerDecks)
        if (state in prevStates) {
            return GameResult(0, playerDecks[0])
        }
        prevStates.add(state)
        val card1 = playerDecks[0].removeFirst()
        val card2 = playerDecks[1].removeFirst()
        val winner = if (playerDecks[0].size >= card1 && playerDecks[1].size >= card2) {
            play(listOf(playerDecks[0].subList(0, card1), playerDecks[1].subList(0, card2))).winner
        } else {
            if (card1 > card2) 0 else 1
        }
        when (winner) {
            0 -> {
                playerDecks[0].addLast(card1)
                playerDecks[0].addLast(card2)
            }

            1 -> {
                playerDecks[1].addLast(card2)
                playerDecks[1].addLast(card1)
            }

            else -> throw Exception("unreachable")
        }
    }
    val (winner, winningDeck) = playerDecks.withIndex().first { (_, deck) -> deck.isNotEmpty() }
    return GameResult(winner, winningDeck)
}

fun day22_2(inputFile: String) {
    val text = File(inputFile).readLines().joinToString("\n")
    val playerLines = text.split("\n\n").map { it.split("\n") }
    val playerDecks = playerLines.map { lines -> lines.subList(1, lines.size).map { it.toInt() } }
    val gameResult = play(playerDecks)
    val winningDeck = gameResult.winningDeck

    val result = winningDeck.reversed().foldIndexed(0) { index, acc, x -> acc + x * (index + 1) }
    println(result)
}
