package fi.solita.hnybom.aoc2024.common

import java.io.File

data class Coordinate(val x: Long, val y: Long)

enum class Direction {
    UP, DOWN, LEFT, RIGHT
}

object Common {

    fun readInput(day: Int, test: Boolean = false) : File {
        if(test) {
            return File("/Users/henri.nybom/work/own/AoC2024/src/main/resources/input${day}_test.txt")
        }
        return File("/Users/henri.nybom/work/own/AoC2024/src/main/resources/input$day.txt")
    }

    fun getAdjacentCoordinates(coordinate: Coordinate) : List<Coordinate> {
        return listOf(
            Coordinate(coordinate.x - 1, coordinate.y),
            Coordinate(coordinate.x + 1, coordinate.y),
            Coordinate(coordinate.x, coordinate.y - 1),
            Coordinate(coordinate.x, coordinate.y + 1),
            Coordinate(coordinate.x - 1, coordinate.y - 1),
            Coordinate(coordinate.x + 1, coordinate.y + 1),
            Coordinate(coordinate.x - 1, coordinate.y + 1),
            Coordinate(coordinate.x + 1, coordinate.y - 1)
        )
    }


    fun isAdjacentTo(coordinate: Coordinate, map: Map<Coordinate, *>) : Boolean {
        return map.containsKey(Coordinate(coordinate.x - 1, coordinate.y)) ||
                map.containsKey(Coordinate(coordinate.x + 1, coordinate.y)) ||
                map.containsKey(Coordinate(coordinate.x, coordinate.y - 1)) ||
                map.containsKey(Coordinate(coordinate.x, coordinate.y + 1)) ||
                map.containsKey(Coordinate(coordinate.x - 1, coordinate.y - 1)) ||
                map.containsKey(Coordinate(coordinate.x + 1, coordinate.y + 1)) ||
                map.containsKey(Coordinate(coordinate.x - 1, coordinate.y + 1)) ||
                map.containsKey(Coordinate(coordinate.x + 1, coordinate.y - 1))
    }
    fun areCoordinatesAdjacent(c1: Coordinate, c2: Coordinate) : Boolean {
        return c1 == Coordinate(c2.x - 1, c2.y) ||
                c1 == Coordinate(c2.x + 1, c2.y) ||
                c1 == Coordinate(c2.x, c2.y - 1) ||
                c1 == Coordinate(c2.x, c2.y + 1) ||
                c1 == Coordinate(c2.x - 1, c2.y - 1) ||
                c1 == Coordinate(c2.x + 1, c2.y + 1) ||
                c1 == Coordinate(c2.x - 1, c2.y + 1) ||
                c1 == Coordinate(c2.x + 1, c2.y - 1)
    }
}

class PathFinder<T> {

    data class Node<T>(val value: T, val paths: MutableList<Node<T>>)
    data class DijkstraResult<T>(val target: Node<T>, val timeCost: Long, val route: Map<Node<T>, Node<T>?> )

    val pathCache = HashMap<Pair<Node<T>, Node<T>>, DijkstraResult<T>>()

    fun dijkstra(list: Collection<Node<T>>, start: Node<T>, end: Node<T>) : DijkstraResult<T>? {
        val c = pathCache[start to end]
        if(c != null) return c

        val costs = list.associateWith { Long.MAX_VALUE }.toMutableMap()
        val route = list.associateWith<Node<T>, Node<T>?> { null }.toMutableMap()

        costs[start] = 0
        val q = mutableListOf(start)

        while (q.isNotEmpty()) {
            val u = q.minByOrNull { costs[it]!! }!!
            q.remove(u)
            u.paths.forEach { v ->
                val alt = costs[u]!! + 1
                if (alt < costs[v]!!) {
                    costs[v] = alt
                    route[v] = u
                    if (v == end) {
                        val r = DijkstraResult(v, alt, route.toMap())
                        pathCache[start to end] = r
                        return r
                    }
                    q.add(v)
                }
            }
        }
        return null
    }

}