package my.advent2020.day16part2

import java.io.File

private data class FieldInfo(
    val name: String,
    val range1: IntRange,
    val range2: IntRange
)

fun day16_2(inputFile: String) {
    val text = File(inputFile).readLines().joinToString("\n")
    val fieldRe = """([\w ]+): (\d+)-(\d+) or (\d+)-(\d+)""".toRegex()
    val (fieldLines, yourLines, nearbyLines) = text.split("\n\n").map { it.split("\n") }
    val fields = fieldLines.map { line ->
        val match = fieldRe.matchEntire(line)!!
        FieldInfo(
            match.groupValues[1],
            match.groupValues[2].toInt()..match.groupValues[3].toInt(),
            match.groupValues[4].toInt()..match.groupValues[5].toInt()
        )
    }
    val yourData = yourLines[1].split(",").map { it.toInt() }
    val nearbyData = nearbyLines.slice(1..nearbyLines.lastIndex).map { line ->
        line.split(",").map { it.toInt() }
    }
    val nearbyDataFiltered =
        nearbyData.filter { data -> data.all { num -> fields.any { field -> num in field.range1 || num in field.range2 } } }
    val fieldToColMap = mutableMapOf<Int, Int>()
    val freeFields = (0..fields.lastIndex).toMutableSet()
    val freeCols = freeFields.toMutableSet()
    var iter = 0
    while (freeCols.isNotEmpty() && (iter++) < 100000) {
        val cols = freeCols.toList()
        for (col in cols) {
            var matchingFields = freeFields.toList()
            for (data in nearbyDataFiltered) {
                val num = data[col]
                matchingFields =
                    matchingFields.filter { index -> num in fields[index].range1 || num in fields[index].range2 }
                if (matchingFields.isEmpty()) {
                    throw Exception("no match for column $col")
                }
            }
            if (matchingFields.size == 1) {
                val matchingField = matchingFields.single()
                fieldToColMap[matchingField] = col
                freeFields.remove(matchingField)
                freeCols.remove(col)
            }
        }
    }
    println("Iterations: $iter")
    var result = 1L
    for ((index, field) in fields.withIndex()) {
        println("${field.name} - ${fieldToColMap[index]}")
        if (field.name.startsWith("departure")) {
            result *= yourData[fieldToColMap[index]!!]
        }
    }
    println("$result")
}
