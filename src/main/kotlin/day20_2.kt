package my.advent2020.day20part2

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

    private fun ray(startX: Int, startY: Int, dirX: Int, dirY: Int, flip: Int, rayLength: Int): String {
        var x = startX
        var y = startY
        return buildString {
            for (i in 0 until rayLength) {
                append(get(x, y, flip))
                x += dirX
                y += dirY
            }
        }
    }

    fun left(flip: Int) = ray(0, 0, 0, 1, flip, size)
    fun right(flip: Int) = ray(sizeMinus1, 0, 0, 1, flip, size)
    fun top(flip: Int) = ray(0, 0, 1, 0, flip, size)
    fun bottom(flip: Int) = ray(0, sizeMinus1, 1, 0, flip, size)

    fun inner(flip: Int): List<String> {
        return buildList {
            for (y in 1..this@Tile.size - 2) {
                add(ray(1, y, 1, 0, flip, this@Tile.size - 2))
            }
        }
    }

    fun hasMonster(x0: Int, y0: Int, flip: Int, monster: List<String>): Boolean {
        for ((y1, row) in monster.withIndex()) {
            for ((x1, c) in row.withIndex()) {
                val y = y0 + y1
                val x = x0 + x1
                if (c == '#' && get(x, y, flip) != '#') {
                    return false
                }
            }
        }
        return true
    }
}

private data class FlippedTile(val tile: Tile, val flip: Int) {
    fun left() = tile.left(flip)
    fun right() = tile.right(flip)
    fun top() = tile.top(flip)
    fun bottom() = tile.bottom(flip)
    fun inner() = tile.inner(flip)
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

fun day20_2(inputFile: String) {
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
    val innerTileSize = solver.grid[0].tile.size - 2
    val imageSideLength = innerTileSize * solver.sideLength
    val image = MutableList(imageSideLength * imageSideLength) { '.' }
    for (tileY in 0 until solver.sideLength) {
        for (tileX in 0 until solver.sideLength) {
            val fTile = solver.grid[tileY * solver.sideLength + tileX]
            val innerTile = fTile.inner()
            for (y in 0 until innerTileSize) {
                for (x in 0 until innerTileSize) {
                    val imageX = tileX * innerTileSize + x
                    val imageY = tileY * innerTileSize + y
                    val imagePos = imageY * imageSideLength + imageX
                    image[imagePos] = innerTile[y][x]
                }
            }
        }
    }
    val imageTile = Tile(0, image.chunked(imageSideLength).map { it.joinToString("") })
    val monster = """
        ..................#.
        #....##....##....###
        .#..#..#..#..#..#...
    """.trimIndent().split("\n")
    val monsterWidth = monster[0].length
    val monsterHeight = monster.size
    val monsterPixels = monster.joinToString("").count { it == '#' }
    var monsters = 0
    for (flip in 0..7) {
        for (y in 0..imageSideLength - monsterHeight) {
            for (x in 0..imageSideLength - monsterWidth) {
                if (imageTile.hasMonster(x, y, flip, monster)) {
                    monsters++
                }
            }
        }
    }
    val waterCount = image.count { it == '#' }
    println("${waterCount - monsters * monsterPixels}")
}
