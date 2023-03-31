package my.advent2020.day9part1

import java.io.File

private fun allSums(collection: List<Long>): List<Long> {
    val result = mutableListOf<Long>()
    for (i in collection.indices) {
        for (j in 0 until i) {
            result.add(collection[i] + collection[j])
        }
    }
    return result
}

fun day9_1(inputFile: String, preambleLength: Int) {
    val nums = File(inputFile).readLines().map { it.toLong() }
    val prevNums = ArrayDeque(nums.slice(0 until preambleLength))
    val tail = nums.slice(preambleLength..nums.lastIndex)
    for (num in tail) {
        val isSum = allSums(prevNums).contains(num)
        if (!isSum) {
            println(num)
            break
        }
        prevNums.removeFirst()
        prevNums.addLast(num)
    }
}
