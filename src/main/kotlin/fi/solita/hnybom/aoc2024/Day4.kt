package fi.solita.hnybom.aoc2024

import fi.solita.hnybom.aoc2024.common.Common
import fi.solita.hnybom.aoc2024.common.Coordinate

class Day4 {

    data class Letter(val letter: Char, val Coordinate: Coordinate)

    private val input = Common.readInput(4).readLines()

    private val letterMap = input.flatMapIndexed { y, line ->
        line.mapIndexed { x, c ->
            val coord = Coordinate(x.toLong(), y.toLong())
            coord to Letter(c, coord)
        }
    }.toMap()

    fun getFourLetters(coord: Coordinate) : List<List<Letter>> {
        val right = listOf(
            letterMap[Coordinate(coord.x, coord.y)],
            letterMap[Coordinate(coord.x + 1, coord.y)],
            letterMap[Coordinate(coord.x + 2, coord.y)],
            letterMap[Coordinate(coord.x + 3, coord.y)]
        ).filterNotNull()
        val left = listOf(
            letterMap[Coordinate(coord.x, coord.y)],
            letterMap[Coordinate(coord.x - 1, coord.y)],
            letterMap[Coordinate(coord.x - 2, coord.y)],
            letterMap[Coordinate(coord.x - 3, coord.y)]
        ).filterNotNull()
        val up = listOf(
            letterMap[Coordinate(coord.x, coord.y)],
            letterMap[Coordinate(coord.x, coord.y - 1)],
            letterMap[Coordinate(coord.x, coord.y - 2)],
            letterMap[Coordinate(coord.x, coord.y - 3)]
        ).filterNotNull()
        val down = listOf(
            letterMap[Coordinate(coord.x, coord.y)],
            letterMap[Coordinate(coord.x, coord.y + 1)],
            letterMap[Coordinate(coord.x, coord.y + 2)],
            letterMap[Coordinate(coord.x, coord.y + 3)]
        ).filterNotNull()
        val upRight = listOf(
            letterMap[Coordinate(coord.x, coord.y)],
            letterMap[Coordinate(coord.x + 1, coord.y - 1)],
            letterMap[Coordinate(coord.x + 2, coord.y - 2)],
            letterMap[Coordinate(coord.x + 3, coord.y - 3)]
        ).filterNotNull()
        val upLeft = listOf(
            letterMap[Coordinate(coord.x, coord.y)],
            letterMap[Coordinate(coord.x - 1, coord.y - 1)],
            letterMap[Coordinate(coord.x - 2, coord.y - 2)],
            letterMap[Coordinate(coord.x - 3, coord.y - 3)]
        ).filterNotNull()
        val downRight = listOf(
            letterMap[Coordinate(coord.x, coord.y)],
            letterMap[Coordinate(coord.x + 1, coord.y + 1)],
            letterMap[Coordinate(coord.x + 2, coord.y + 2)],
            letterMap[Coordinate(coord.x + 3, coord.y + 3)]
        ).filterNotNull()
        val downLeft = listOf(
            letterMap[Coordinate(coord.x, coord.y)],
            letterMap[Coordinate(coord.x - 1, coord.y + 1)],
            letterMap[Coordinate(coord.x - 2, coord.y + 2)],
            letterMap[Coordinate(coord.x - 3, coord.y + 3)]
        ).filterNotNull()

        return listOf(right, left, up, down, upRight, upLeft, downRight, downLeft)
    }

    fun isXmasLettersInOrder(letters: List<Letter>) : Boolean {
        val xmas = "XMAS"
        val joinToString = letters.map { it.letter }.joinToString("")
        return joinToString.contains(xmas)
    }

    fun isXmasLettersInOrderInAnyDirection(letters: List<List<Letter>>) : List<List<Letter>> {
        return letters.filter { isXmasLettersInOrder(it) }
    }


    fun part1() : Int {
        val matches = letterMap.values.flatMap { letter ->
            val allDirs = getFourLetters(letter.Coordinate)
            isXmasLettersInOrderInAnyDirection(allDirs)
        }
        return matches.count()

    }

    data class XMasLetter(val letter: List<Letter>)

    fun isCrossMas(x : Pair<XMasLetter, XMasLetter>) : Boolean {
        val xmas = "MAS"
        val first = x.first.letter.map { it.letter }.joinToString("")
        val second = x.second.letter.map { it.letter }.joinToString("")
        return (first.contains(xmas) || first.contains(xmas.reversed())) &&
                (second.contains(xmas) || second.contains(xmas.reversed()))
    }

    fun findXMas(coord: Coordinate) : Pair<XMasLetter, XMasLetter> {
        val firstCross = listOf(
            letterMap[Coordinate(coord.x - 1, coord.y - 1)],
            letterMap[Coordinate(coord.x, coord.y)],
            letterMap[Coordinate(coord.x + 1, coord.y + 1)]
        ).filterNotNull()

        val secondCross = listOf(
            letterMap[Coordinate(coord.x + 1, coord.y - 1)],
            letterMap[Coordinate(coord.x, coord.y)],
            letterMap[Coordinate(coord.x - 1, coord.y + 1)]
        ).filterNotNull()

        return XMasLetter(firstCross) to XMasLetter(secondCross)

    }

    fun part2() : Int {
        val matches = letterMap.values.mapNotNull { letter ->
            findXMas(letter.Coordinate)
        }.filter {
            isCrossMas(it)
        }

        return matches.count()
    }

    fun run() {
        println("Day 1")
        println("Part 1: ${part1()}")
        println("Part 2: ${part2()}")
    }
}

fun main() {
    Day4().run()
}