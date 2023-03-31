package my.advent2020.day10part2

import java.io.File

fun day10_2(inputFile: String) {
    val nums = File(inputFile).readLines().map { it.toInt() }
    val numSet = nums.toSet()
    val maxNum = nums.max()
    var paths0 = 1L
    var paths1 = 0L
    var paths2 = 0L
    for (cur in 0 until maxNum) {
        var newPaths0 = paths1
        var newPaths1 = paths2
        var newPaths2 = 0L
        if (cur + 1 in numSet) {
            newPaths0 += paths0
        }
        if (cur + 2 in numSet) {
            newPaths1 += paths0
        }
        if (cur + 3 in numSet) {
            newPaths2 += paths0
        }
        paths0 = newPaths0
        paths1 = newPaths1
        paths2 = newPaths2
    }
    println(paths0)
}
