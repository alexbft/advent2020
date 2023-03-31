package my.advent2020.day20part1

import java.io.File
import kotlin.math.sqrt

private data class Tile(val id: Int, val rows: List<String>) {
    val size = rows.size
    private val sizeMinus1 = size - 1

    private fun get(x: Int, y: Int, flip: Int): Char {
        return when (flip) {
            0 -> rows[y][x]
            1 -> rows[sizeMinus1 - y][x]
            2 -> rows[y][sizeMinus1 - x]
            3 -> rows[sizeMinus1 - y][sizeMinus1 - x]
            4 -> rows[x][y]
            5 -> rows[sizeMinus1 - x][y]
            6 -> rows[x][sizeMinus1 - y]
            7 -> rows[sizeMinus1 - x][sizeMinus1 - y]
            else -> throw Exception("flip $flip")
        }
    }

    private fun ray(startX: Int, startY: Int, dirX: Int, dirY: Int, flip: Int): String {
        var x = startX
        var y = startY
        return buildString {
            for (i in 0..sizeMinus1) {
                append(get(x, y, flip))
                x += dirX
                y += dirY
            }
        }
    }

    fun left(flip: Int) = ray(0, 0, 0, 1, flip)
    fun right(flip: Int) = ray(sizeMinus1, 0, 0, 1, flip)
    fun top(flip: Int) = ray(0, 0, 1, 0, flip)
    fun bottom(flip: Int) = ray(0, sizeMinus1, 1, 0, flip)
}

private data class FlippedTile(val tile: Tile, val flip: Int) {
    fun left() = tile.left(flip)
    fun right() = tile.right(flip)
    fun top() = tile.top(flip)
    fun bottom() = tile.bottom(flip)
}

private class Solver(val tiles: Map<Int, Tile>) {
    val grid = mutableListOf<FlippedTile>()
    val usedTiles = mutableSetOf<Int>()
    val tilesNum = tiles.size
    val sideLength = sqrt(tilesNum.toDouble()).toInt()

    fun solve(pos: Int): Boolean {
        if (pos >= tilesNum) {
            return true
        }
        val posX = pos % sideLength
        val posY = pos / sideLength
        val leftSide = if (posX > 0) grid[pos - 1].right() else ""
        val topSide = if (posY > 0) grid[pos - sideLength].bottom() else ""
        for (tile in tiles.values) {
            if (usedTiles.contains(tile.id)) {
                continue
            }
            for (flip in 0..7) {
                val flippedTile = FlippedTile(tile, flip)
                if ((topSide == "" || flippedTile.top() == topSide) && (leftSide == "" || flippedTile.left() == leftSide)) {
                    grid.add(flippedTile)
                    usedTiles.add(tile.id)
                    if (solve(pos + 1)) {
                        return true
                    }
                    usedTiles.remove(tile.id)
                    grid.removeAt(pos)
                }
            }
        }
        return false
    }
}

fun day20_1(inputFile: String) {
    val text = File(inputFile).readLines().joinToString("\n")
    val tileLinesList = text.split("\n\n").map { it.split("\n") }
    var tileIdRe = """Tile (\d+):""".toRegex()
    val tiles = buildMap {
        for (tileLines in tileLinesList) {
            val id = tileIdRe.matchEntire(tileLines.first())!!.groupValues[1].toInt()
            val tile = Tile(id, tileLines.slice(1..tileLines.lastIndex))
            put(id, tile)
        }
    }
    val solver = Solver(tiles)
    if (!solver.solve(0)) {
        throw Exception("unsolved")
    }
    val tileLU = solver.grid[0]
    val tileRU = solver.grid[solver.sideLength - 1]
    val tileLD = solver.grid[solver.sideLength * (solver.sideLength - 1)]
    val tileRD = solver.grid[solver.tilesNum - 1]
    val result = tileLU.tile.id.toLong() * tileRU.tile.id * tileLD.tile.id * tileRD.tile.id
    println("${tileLU.tile.id} ${tileRU.tile.id} ${tileLD.tile.id} ${tileRD.tile.id} $result")
}
