package my.advent2020.day23part2

import java.io.File

private data class Node(val n: Int, var next: Node?)

fun day23_2(inputFile: String) {
    val input = File(inputFile).readText().trim()
    val startNums = input.map { it.digitToInt() }
    val maxNum = 1000000
    val nodes = List(maxNum + 1) { n -> Node(n, null) }
    for ((n0, n1) in startNums.zipWithNext()) {
        nodes[n0].next = nodes[n1]
    }
    nodes[startNums.last()].next = nodes[startNums.size + 1]
    for (n in startNums.size + 1 until maxNum) {
        nodes[n].next = nodes[n + 1]
    }
    nodes[maxNum].next = nodes[startNums.first()]
    var cur = nodes[startNums.first()]
    for (turn in 1..10000000) {
        val picked1 = cur.next!!
        val picked2 = picked1.next!!
        val picked3 = picked2.next!!
        val pickedNums = listOf(picked1, picked2, picked3).map { it.n }
        cur.next = picked3.next
        var dest = cur.n
        do {
            --dest
            if (dest == 0) {
                dest = maxNum
            }
        } while (dest in pickedNums)
        val destNode = nodes[dest]
        picked3.next = destNode.next
        destNode.next = picked1
        cur = cur.next!!
    }
    val res1 = nodes[1].next!!.n
    val res2 = nodes[1].next!!.next!!.n
    println("$res1 $res2 ${res1.toLong() * res2}")
}
