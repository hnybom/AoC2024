package fi.solita.hnybom.aoc2024

import fi.solita.hnybom.aoc2024.common.Common
import fi.solita.hnybom.aoc2024.common.Coordinate
import kotlin.math.abs

class Day8 {

    data class Antenna(val frequency : Char, val location: Coordinate)

    data class Antinode(val frequency : Char, val location : Coordinate, val antennasCausingNode: List<Antenna>)

    private val input = Common.readInput(8).readLines().flatMapIndexed { y, line ->
        line.mapIndexed { x, col ->
            Coordinate(x.toLong(), y.toLong()) to when (col) {
                '.' -> null
                else -> Antenna(col, Coordinate(x.toLong(), y.toLong()))
            }
        }
    }.toMap()

    private val maxX = input.keys.maxBy { it.x }
    private val maxY = input.keys.maxBy { it.y }

    private val antennaMap: Map<Char, List<Antenna>> = input.values.filterNotNull().groupBy { it.frequency }

    private fun spinAround(l1: Coordinate, l2: Coordinate): Coordinate? {
        if (l1 == l2) return null
        val xDiff = abs(l1.x - l2.x)
        val x = if (l1.x > l2.x) l1.x + xDiff else l1.x - xDiff

        val yDiff = abs(l1.y - l2.y)
        val y = if (l1.y > l2.y) l1.y + yDiff else l1.y - yDiff

        return if ( x >= 0 && x <= maxX.x && y >= 0 && y <= maxY.y) Coordinate(x, y)
        else null

    }

    private tailrec fun spinRecursively(l1: Coordinate, l2: Coordinate, acc: List<Coordinate>): List<Coordinate> {
        val spin = spinAround(l1, l2)
        return if (spin == null) acc
        else {
            spinRecursively(spin, l1, acc + spin )
        }
    }


    fun part1(): Int {
        val resonancesPoints = antennaMap.values.flatMap { antennas ->
            antennas.flatMap { antenna ->
                val others = antennas.filter { it != antenna }
                others.mapNotNull {
                    spinAround(antenna.location, it.location)
                }
            }
        }
        return resonancesPoints.distinct().size
    }

    fun part2(): Int {
        val resonancesPoints = antennaMap.values.map { antennas ->
            val antinodesPerF = antennas.map { antenna ->
                val others = antennas.filter { it != antenna }
                val antinodes = others.flatMap {
                    spinRecursively(antenna.location, it.location, emptyList())
                }
                antenna.frequency to antinodes
            }
            antinodesPerF
        }.flatten()

        val addAntennas = antennaMap.values.flatMap { antennaList ->
            if(antennaList.count() > 1) antennaList.map { it.frequency to listOf(it.location) }
            else emptyList()
        }

        val includedAntennas = resonancesPoints + addAntennas

        val antinodeMap = includedAntennas.flatMap { it.second }.associateWith { it }
        (0..maxY.y).forEach { y ->
            (0..maxX.x).forEach { x ->
                when(antinodeMap[Coordinate(x,y)]) {
                    null -> print(".")
                    else -> print("X")
                }
            }
            println()
        }
        return includedAntennas.flatMap { it.second }.distinct().count()
    }

}

fun main() {
    val day8 = Day8()
    println("Day 8")
    // Add calls to part1 and part2 when implemented
    println("Part 1: ${day8.part1()}")
    println("Part 2: ${day8.part2()}")
}