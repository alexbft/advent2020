package my.advent2020.day17part2

import java.io.File
import kotlin.math.abs

private val maxSize = 20

private data class Point4d(val x: Int, val y: Int, val z: Int, val w: Int)

private class Grid4d {
    private val cells = mutableListOf<Int>()
    private var minX = 0
    private var maxX = 0
    private var minY = 0
    private var maxY = 0
    private var minZ = 0
    private var maxZ = 0
    private var minW = 0
    private var maxW = 0

    private fun toIndex(p: Point4d): Int {
        val (x, y, z, w) = p
        val sx = if (x < 0) 1 else 0
        val sy = if (y < 0) 1 else 0
        val sz = if (z < 0) 1 else 0
        val sw = if (w < 0) 1 else 0
        return (abs(w) * maxSize * maxSize * maxSize + abs(z) * maxSize * maxSize + abs(y) * maxSize + abs(x)) * 16 + (sw * 8 + sz * 4 + sy * 2 + sx)
    }

    fun get(p: Point4d): Int {
        val index = toIndex(p)
        if (index >= cells.size) {
            return 0
        }
        return cells[index]
    }

    fun set(p: Point4d, v: Int) {
        if (v != 0) {
            if (p.x < minX) minX = p.x
            if (p.x > maxX) maxX = p.x
            if (p.y < minY) minY = p.y
            if (p.y > maxY) maxY = p.y
            if (p.z < minZ) minZ = p.z
            if (p.z > maxZ) maxZ = p.z
            if (p.w < minW) minW = p.w
            if (p.w > maxW) maxW = p.w
        }
        val index = toIndex(p)
        if (index > cells.size) {
            cells.addAll(IntArray(index - cells.size).asList())
            cells.add(v)
        } else {
            cells[index] = v
        }
    }

    fun sumNeighbors(p: Point4d): Int {
        var sum = 0
        for (w in p.w - 1..p.w + 1) {
            for (z in p.z - 1..p.z + 1) {
                for (y in p.y - 1..p.y + 1) {
                    for (x in p.x - 1..p.x + 1) {
                        sum += get(Point4d(x, y, z, w))
                    }
                }
            }
        }
        sum -= get(p)
        return sum
    }

    fun forEachWithBorder(fn: (p: Point4d, v: Int) -> Unit) {
        for (w in minW - 1..maxW + 1) {
            for (z in minZ - 1..maxZ + 1) {
                for (y in minY - 1..maxY + 1) {
                    for (x in minX - 1..maxX + 1) {
                        val p = Point4d(x, y, z, w)
                        fn(p, get(p))
                    }
                }
            }
        }
    }
}

fun day17_2(inputFile: String) {
    val lines = File(inputFile).readLines()
    var grid = Grid4d()
    for ((y, line) in lines.withIndex()) {
        for ((x, c) in line.withIndex()) {
            if (c == '#') {
                grid.set(Point4d(x, y, 0, 0), 1)
            }
        }
    }
    for (turn in 1..6) {
        val newGrid = Grid4d()
        grid.forEachWithBorder { p, v ->
            val neighborCount = grid.sumNeighbors(p)
            if (neighborCount == 3 || neighborCount == 2 && v == 1) {
                newGrid.set(p, 1)
            }
        }
        grid = newGrid
    }
    var total = 0
    grid.forEachWithBorder { _, v -> total += v }
    println("$total")
}
