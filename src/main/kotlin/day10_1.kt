package my.advent2020.day10part1

import java.io.File

fun day10_1(inputFile: String) {
    val nums = File(inputFile).readLines().map { it.toInt() }
    val sortedNums = nums.sorted()
    var diff1 = 0
    var diff3 = 1
    var prev = 0
    for (cur in sortedNums) {
        if (cur - prev == 1) {
            ++diff1
        } else if (cur - prev == 3) {
            ++diff3
        }
        prev = cur
    }
    println("$diff1 $diff3 ${diff1 * diff3}")
}
