package my.advent2020.day23part1

import java.io.File

fun day23_1(inputFile: String) {
    val input = File(inputFile).readText().trim()
    var nums = input.map { it.digitToInt() }.toMutableList()
    var curIndex = 0
    for (turn in 1..100) {
        val looped = buildList { for (i in 0..1) addAll(nums) }
        val picked = looped.subList(curIndex + 1, curIndex + 4)
        val cur = nums[curIndex]
        var dest = cur - 1
        if (dest == 0) dest = 9
        while (dest in picked) {
            --dest
            if (dest == 0) {
                dest = 9
            }
        }
        nums = nums.filter { it !in picked }.toMutableList()
        val destIndex = nums.indexOf(dest)
        nums.addAll(destIndex + 1, picked)
        curIndex = (nums.indexOf(cur) + 1) % nums.size
        println("$turn ${nums.mapIndexed { i, n -> if (i == curIndex) "($n)" else "$n" }.joinToString("")}")
    }
    val looped = buildList { for (i in 0..1) addAll(nums) }
    val startIndex = looped.indexOf(1) + 1
    val result = looped.subList(startIndex, startIndex + 8)
    println(result.joinToString(""))
}
