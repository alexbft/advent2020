package my.advent2020.day18part1

import java.io.File

private fun calc(operation: String, operand1: Long, operand2: Long): Long {
    return when (operation) {
        "+" -> operand1 + operand2
        "*" -> operand1 * operand2
        else -> throw Exception("Unknown operation: '$operation'")
    }
}

private class Parser(val source: String) {
    var ptr = 0
    fun readExpression(): Long {
        var op1 = readOperand()
        while (true) {
            val op = readOperatorOrClosingBracket()
            if (op == ")") {
                return op1
            }
            val op2 = readOperand()
            op1 = calc(op, op1, op2)
        }
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

fun day18_1(inputFile: String) {
    val lines = File(inputFile).readLines()
    var total = 0L
    for (line in lines) {
        val result = solve(line)
        println("$line = $result")
        total += result
    }
    println("Total: $total")
}
