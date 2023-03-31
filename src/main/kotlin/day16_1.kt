package my.advent2020.day16part1

import java.io.File

private data class FieldInfo(
    val name: String,
    val range1: IntRange,
    val range2: IntRange
)

fun day16_1(inputFile: String) {
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
    var result = 0
    for (data in nearbyData) {
        for (num in data) {
            if (fields.all { field -> num !in field.range1 && num !in field.range2 }) {
                result += num
            }
        }
    }
    println("$result")
}
