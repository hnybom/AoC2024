package fi.solita.hnybom.aoc2024

import fi.solita.hnybom.aoc2024.common.Common
import fi.solita.hnybom.aoc2024.common.Coordinate
import fi.solita.hnybom.aoc2024.common.Direction

class Day6 {

    data class MapLocation(val coordinate: Coordinate, val isBlocked:  Boolean = false)

    data class Guard(val location: Coordinate, val direction: Direction)

    data class GuardStep(val guard: Guard, val step: Long, val turns: Long)

    private val input = Common.readInput(6).readLines()
    private var initialGuard : Guard? = null

    private val startingMap = input.flatMapIndexed { y: Int, line ->
        line.mapIndexed { x: Int, col ->

            val coordinate = Coordinate(x.toLong(), y.toLong())
            if (initialGuard == null) {
                initialGuard = when (col) {
                    '^' -> Guard(coordinate, Direction.UP)
                    '>' -> Guard(coordinate, Direction.RIGHT)
                    '<' -> Guard(coordinate, Direction.LEFT)
                    'v' -> Guard(coordinate, Direction.DOWN)
                    else -> null
                }
            }

            coordinate to when(col) {
                '#' -> MapLocation(coordinate, true)
                else -> MapLocation(coordinate)
            }
        }
    }.toMap()

    private fun rotateDirection(direction: Direction): Direction {
        return when(direction) {
            Direction.UP -> Direction.RIGHT
            Direction.RIGHT -> Direction.DOWN
            Direction.DOWN -> Direction.LEFT
            Direction.LEFT -> Direction.UP
        }
    }

    private fun getDirectionCoordinate(coordinate: Coordinate, direction: Direction, currentMap: Map<Coordinate, MapLocation>): MapLocation? {
        return when(direction) {
            Direction.UP -> currentMap[Coordinate(coordinate.x, coordinate.y - 1)]
            Direction.DOWN -> currentMap[Coordinate(coordinate.x, coordinate.y + 1)]
            Direction.LEFT -> currentMap[Coordinate(coordinate.x - 1, coordinate.y)]
            Direction.RIGHT -> currentMap[Coordinate(coordinate.x + 1, coordinate.y)]
        }
    }
    private fun getNextStep(step: GuardStep?, currentMap: Map<Coordinate, MapLocation>): GuardStep? {
        if (step == null) return null
        val nextMapLocation = getDirectionCoordinate(step.guard.location, step.guard.direction, currentMap) ?: return null

        if (nextMapLocation.isBlocked) { return getNextStep(
            step.copy(
                guard = step.guard.copy(direction = rotateDirection(step.guard.direction)),
                turns = step.turns + 1
            ), currentMap)
        }

        return step.copy(
            guard = step.guard.copy(location = nextMapLocation.coordinate),
            step = step.step + 1
        )
    }

    private fun getGuardRoute(guard: Guard, currentMap: Map<Coordinate, MapLocation>) : Sequence<GuardStep> {
        var previousStep: GuardStep? = GuardStep(guard, 0, 0)
        return generateSequence {
            previousStep = getNextStep(previousStep, currentMap)
            previousStep
        }
    }

    private fun willObstructionCauseLoop(obstructionPosition: Coordinate): Boolean {
        if (obstructionPosition == initialGuard!!.location) return false
        if (startingMap[obstructionPosition]?.isBlocked == true) return false

        val mapWithObstruction = startingMap + (obstructionPosition to MapLocation(obstructionPosition, true))
        val visitedStates = HashSet<Pair<Coordinate, Direction>>()

        return getGuardRoute(initialGuard!!, mapWithObstruction).any { step ->
            val stateKey = step.guard.location to step.guard.direction
            if (visitedStates.contains(stateKey)) {
                true // Found a loop
            } else {
                visitedStates.add(stateKey)
                false
            }
        }
    }

    private fun part1(): Int {
        return getGuardRoute(initialGuard!!, startingMap).map { it.guard.location }.distinct().count()
    }

    // 1740
    private fun part2(): Int {
        val guardRoute = getGuardRoute(initialGuard!!, startingMap).map { it.guard.location }.distinct().toList()

        return guardRoute.count { position ->
            willObstructionCauseLoop(position)
        }
    }

    fun run() {
        if (initialGuard == null) throw IllegalStateException("Initial Guard is null")
        println(part1())
        println(part2())
    }

}

fun main() {
    Day6().run()
}