package my.advent2020.day11part2

import java.io.File

private class Grid(private val cells: List<MutableList<Char>>) {
    val maxX = cells.first().size
    val maxY = cells.size

    fun map(mapFn: (c: Char, x: Int, y: Int) -> Char): Grid {
        val newCells = mutableListOf<MutableList<Char>>()
        for (y in 0 until maxY) {
            val newRow = mutableListOf<Char>()
            for (x in 0 until maxX) {
                val newChar = mapFn(cells[y][x], x, y)
                newRow.add(newChar)
            }
            newCells.add(newRow)
        }
        return Grid(newCells)
    }

    fun countNeighbors(x: Int, y: Int): Int {
        var result = 0
        for (dy in -1..1) {
            for (dx in -1..1) {
                if (dx == 0 && dy == 0) {
                    continue
                }
                var steps = 1
                while (true) {
                    val nx = x + dx * steps
                    val ny = y + dy * steps
                    if (!isInside(nx, ny) || cells[ny][nx] == 'L') {
                        break
                    }
                    if (cells[ny][nx] == '#') {
                        ++result
                        break
                    }
                    steps += 1
                }
            }
        }
        return result
    }

    private fun isInside(x: Int, y: Int): Boolean {
        return x in 0 until maxX && y in 0 until maxY
    }

    fun allCells(): Iterable<Char> {
        return cells.flatten()
    }

    override fun toString(): String {
        return cells.joinToString("\n") { it.joinToString("") }
    }
}

val limit = 1000

fun day11_2(inputFile: String) {
    val cells = File(inputFile).readLines().map { it.toMutableList() }
    var grid = Grid(cells)
    for (cycle in 0 until limit) {
        var changes = 0
        //println("Cycle $cycle")
        //println(grid.toString())
        grid = grid.map { c, x, y ->
            if (c != '.' && c != '#' && c != 'L') {
                throw Exception("unknown item")
            }
            if (c == '.') {
                return@map '.'
            }
            val neighbors = grid.countNeighbors(x, y)
            if (neighbors == 0) {
                if (c != '#') {
                    ++changes
                }
                return@map '#'
            }
            if (neighbors >= 5) {
                if (c != 'L') {
                    ++changes
                }
                return@map 'L'
            }
            return@map c
        }
        if (changes == 0) {
            break
        }
        if (cycle == limit - 1) {
            println("too many cycles")
        }
    }
    val occupied = grid.allCells().count { it == '#' }
    println(occupied)
}
