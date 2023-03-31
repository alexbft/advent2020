package my.advent2020.day18part2

import java.io.File

private class Parser(val source: String) {
    var ptr = 0
    fun readExpression(): Long {
        val operands = mutableListOf<Long>()
        var op1 = readOperand()
        while (true) {
            val op = readOperatorOrClosingBracket()
            if (op == ")") {
                break
            }
            val op2 = readOperand()
            if (op == "+") {
                op1 = op1 + op2
            } else /* op == "*" */ {
                operands.add(op1)
                op1 = op2
            }
        }
        operands.add(op1)
        return operands.reduce { acc, x -> acc * x }
    }

    private fun readOperand(): Long {
        val result = readNumberOrOpeningBracket()
        if (result == "(") {
            return readExpression()
        }
        return result.toLong()
    }

    private fun readNumberOrOpeningBracket(): String {
        while (ptr < source.length) {
            val c = readChar()
            if (c == ' ') {
                continue
            }
            if (c == '(') {
                return "("
            }
            if (c in '0'..'9') {
                return "$c${readNumber()}"
            }
            throw Exception("Unexpected char: $c at $ptr")
        }
        throw Exception("Unexpected EOLN")
    }

    private fun readNumber(): String {
        var result = ""
        while (ptr < source.length && peekChar() in '0'..'9') {
            result += readChar()
        }
        return result
    }

    private fun readOperatorOrClosingBracket(): String {
        while (ptr < source.length) {
            val c = readChar()
            if (c == ' ') {
                continue
            }
            if (c == '*' || c == '+' || c == ')') {
                return "$c"
            }
            throw Exception("Unexpected char: $c at $ptr")
        }
        throw Exception("Unexpected EOLN")
    }

    private fun peekChar(): Char {
        return source[ptr]
    }

    private fun readChar(): Char {
        return source[ptr++]
    }
}

private fun solve(line: String): Long {
    return Parser("$line)").readExpression()
}

fun day18_2(inputFile: String) {
    val lines = File(inputFile).readLines()
    var total = 0L
    for (line in lines) {
        val result = solve(line)
        println("$line = $result")
        total += result
    }
    println("Total: $total")
}
