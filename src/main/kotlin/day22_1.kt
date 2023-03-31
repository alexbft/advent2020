package my.advent2020.day22part1

import java.io.File

fun day22_1(inputFile: String) {
    val text = File(inputFile).readLines().joinToString("\n")
    val playerLines = text.split("\n\n").map { it.split("\n") }
    val playerDecks = playerLines.map { lines -> ArrayDeque(lines.subList(1, lines.size).map { it.toInt() }) }
    while (playerDecks.all { deck -> deck.isNotEmpty() }) {
        val card1 = playerDecks[0].removeFirst()
        val card2 = playerDecks[1].removeFirst()
        if (card1 > card2) {
            playerDecks[0].addLast(card1)
            playerDecks[0].addLast(card2)
        } else if (card2 > card1) {
            playerDecks[1].addLast(card2)
            playerDecks[1].addLast(card1)
        } else {
            throw Exception("cards should be unique")
        }
    }
    val winningDeck = playerDecks.first { deck -> deck.isNotEmpty() }
    val result = winningDeck.reversed().foldIndexed(0) { index, acc, x -> acc + x * (index + 1) }
    println(result)
}
