package my.advent2020.day13part1

import java.io.File

fun day13_1(inputFile: String) {
    val lines = File(inputFile).readLines()
    val timeStamp = lines[0].toInt()
    val buses = lines[1].split(",").filter { it != "x" }.map { it.toInt() }
    var minDelay = Int.MAX_VALUE
    var minBus = 0
    for (bus in buses) {
        var delay = timeStamp % bus
        if (delay > 0) {
            delay = bus - delay
        }
        if (delay < minDelay) {
            minDelay = delay
            minBus = bus
        }
    }
    println("$minBus $minDelay ${minBus * minDelay}")
}
