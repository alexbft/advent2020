package my.advent2020.day8part1

import java.io.File

enum class Command {
    Jmp, Acc, Nop
}

fun day8_1() {
    data class Instruction(val cmd: Command, val arg: Int)

    val instructions = mutableListOf<Instruction>()
    File("8.txt").useLines { lines ->
        val re = """(\w+) ([+-])(\d+)""".toRegex()
        for (line in lines) {
            val match = re.matchEntire(line)!!
            val cmd = when (match.groupValues[1]) {
                "acc" -> Command.Acc
                "jmp" -> Command.Jmp
                "nop" -> Command.Nop
                else -> throw Exception()
            }
            var arg = match.groupValues[3].toInt()
            if (match.groupValues[2] == "-") {
                arg = -arg
            }
            instructions.add(Instruction(cmd, arg))
        }
    }
    var iptr = 0
    var reg = 0
    val visited = mutableSetOf<Int>()
    while (iptr !in visited) {
        visited.add(iptr)
        if (iptr < 0 || iptr >= instructions.size) {
            throw Exception("iptr out of bounds")
        }
        val instruction = instructions[iptr]
        when (instruction.cmd) {
            Command.Nop -> {
                iptr++
            }

            Command.Acc -> {
                reg += instruction.arg
                iptr++
            }

            Command.Jmp -> {
                iptr += instruction.arg
            }
        }
    }
    println(reg)
}
