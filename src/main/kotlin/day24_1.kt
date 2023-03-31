package my.advent2020.day24part1

import java.io.File

private data class Point(val x: Int, val y: Int) {
    operator fun plus(v: Vector): Point {
        return Point(v.x + x, v.y + y)
    }
}
private typealias Vector = Point

private fun dirToVector(dir: String): Vector {
    return when (dir) {
        "w" -> Vector(-1, 0)
        "e" -> Vector(1, 0)
        "nw" -> Vector(-1, -1)
        "ne" -> Vector(0, -1)
        "sw" -> Vector(0, 1)
        "se" -> Vector(1, 1)
        else -> throw Exception("dir $dir")
    }
}

fun day24_1(inputFile: String) {
    val lines = File(inputFile).readLines()
    val dirRe = """w|e|nw|ne|sw|se""".toRegex()
    val grid = mutableMapOf<Point, Boolean>()
    for (line in lines) {
        val dirs = dirRe.findAll(line).map { dirToVector(it.value) }
        val coords = dirs.fold(Point(0, 0)) { acc, v -> acc + v }
        grid[coords] = !(grid[coords] ?: false)
    }
    val result = grid.values.count { it }
    println(result)
}
