package my.advent2020.day9part2

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

fun day9_2(inputFile: String, preambleLength: Int) {
    val nums = File(inputFile).readLines().map { it.toLong() }
    val prevNums = ArrayDeque(nums.slice(0 until preambleLength))
    val tail = nums.slice(preambleLength..nums.lastIndex)
    var targetNum = 0L
    for (num in tail) {
        val isSum = allSums(prevNums).contains(num)
        if (!isSum) {
            targetNum = num
            println(num)
            break
        }
        prevNums.removeFirst()
        prevNums.addLast(num)
    }
    var startP = 0
    var endP = 0
    var sum = 0L
    while (sum != targetNum && endP < nums.size) {
        while (sum < targetNum && endP < nums.size) {
            sum += nums[endP]
            ++endP
        }
        while (sum > targetNum) {
            sum -= nums[startP]
            ++startP
        }
    }
    val targetSlice = nums.subList(startP, endP)
    val minN = targetSlice.min()
    val maxN = targetSlice.max()
    println("$minN $maxN ${minN + maxN}")
}
