package my.advent2020.day25part1

import java.io.File

private const val modBase = 20201227

fun day25_1(inputFile: String) {
    val pubKeys = File(inputFile).readLines().map { it.toInt() }
    var n = 1L
    var loop = 1
    while (true) {
        n = n * 7 % modBase
        if (n.toInt() == pubKeys[0]) {
            break
        }
        ++loop
    }
    n = 1L
    for (i in 1..loop) {
        n = n * pubKeys[1] % modBase
    }
    println(n)
}
