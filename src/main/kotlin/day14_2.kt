package my.advent2020.day14part2

import java.io.File

private data class Mask(val ones: Long, val xs: Long)

private fun parseMask(s: String): Mask {
    var ones = 0L
    var xs = 0L
    for (c in s) {
        ones *= 2
        xs *= 2
        when (c) {
            '1' -> {
                ones += 1
            }

            'X' -> {
                xs += 1
            }
        }
    }
    return Mask(ones, xs)
}

private fun allAddresses(addr: Long, mask: Mask): Sequence<Long> {
    return sequence {
        if (mask.ones == 0L && mask.xs == 0L) {
            yield(addr)
        } else {
            if (mask.xs and 1L == 1L) {
                for (addr in allAddresses(addr shr 1, Mask(mask.ones shr 1, mask.xs shr 1))) {
                    yield(addr * 2)
                    yield(addr * 2 + 1)
                }
            } else {
                val bit = (addr and 1L) or (mask.ones and 1L)
                for (addr in allAddresses(addr shr 1, Mask(mask.ones shr 1, mask.xs shr 1))) {
                    yield(addr * 2 + bit)
                }
            }
        }
    }
}

fun day14_2(inputFile: String) {
    val lines = File(inputFile).readLines()
    val maskRe = """mask = (.{36})""".toRegex()
    val memRe = """mem\[(\d+)] = (\d+)""".toRegex()
    val memory = mutableMapOf<Long, Long>()
    var mask = Mask(0L, 0L)
    for (line in lines) {
        val maskMatch = maskRe.matchEntire(line)
        if (maskMatch != null) {
            mask = parseMask(maskMatch.groupValues[1])
            continue
        }
        val memMatch = memRe.matchEntire(line)
        if (memMatch != null) {
            val addr = memMatch.groupValues[1].toLong()
            val value = memMatch.groupValues[2].toLong()
            for (realAddr in allAddresses(addr, mask)) {
                memory[realAddr] = value
            }
            continue
        }
        throw Exception("Unparsed line $line")
    }
    val memorySum = memory.values.sum()
    println("$memorySum")
}
