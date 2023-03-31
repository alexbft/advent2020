package my.advent2020.day15part2

import java.io.File

fun day15_2(inputFile: String) {
    val lines = File(inputFile).readLines()
    for (line in lines) {
        val nums = line.split(',').map { it.toInt() }
        val slots = mutableMapOf<Int, Int>()
        for ((i, num) in nums.withIndex()) {
            if (i != nums.lastIndex) {
                slots[num] = i
            }
        }
        var next = nums.last()
        var index = nums.size - 1
        while (index < 30000000 - 1) {
            val lastIndexOfNext = slots[next]
            slots[next] = index
            next = if (lastIndexOfNext == null) 0 else index - lastIndexOfNext
            index += 1
        }
        println("$line $next")
    }
}
