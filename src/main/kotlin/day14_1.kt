package my.advent2020.day14part1

import java.io.File

private data class Mask(val ones: Long, val zeroes: Long)

private const val maxMask = (1L shl 36) - 1

private fun parseMask(s: String): Mask {
    var ones = 0L
    var zeroes = 0L
    for (c in s) {
        ones *= 2
        zeroes *= 2
        when (c) {
            '1' -> {
                ones += 1
                zeroes += 1
            }

            'X' -> {
                zeroes += 1
            }
        }
    }
    return Mask(ones, zeroes)
}

private fun applyMask(x: Long, mask: Mask): Long {
    return (x or mask.ones) and mask.zeroes
}

fun day14_1(inputFile: String) {
    val lines = File(inputFile).readLines()
    val maskRe = """mask = (.{36})""".toRegex()
    val memRe = """mem\[(\d+)] = (\d+)""".toRegex()
    val memory = mutableMapOf<Int, Long>()
    var mask = Mask(0L, maxMask)
    for (line in lines) {
        val maskMatch = maskRe.matchEntire(line)
        if (maskMatch != null) {
            mask = parseMask(maskMatch.groupValues[1])
            continue
        }
        val memMatch = memRe.matchEntire(line)
        if (memMatch != null) {
            val addr = memMatch.groupValues[1].toInt()
            val value = applyMask(memMatch.groupValues[2].toLong(), mask)
            memory[addr] = value
            continue
        }
        throw Exception("Unparsed line $line")
    }
    val memorySum = memory.values.sum()
    println("$memorySum")
}
