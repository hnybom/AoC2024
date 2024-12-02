package fi.solita.hnybom.aoc2024

import fi.solita.hnybom.aoc2024.common.Common
import kotlin.math.abs

class Day2 {

    private val input = Common.readInput(2).readLines()
        .map { row ->
            val split = row.split(" ")
            split.map { it.toLong() }
        }

    enum class DIRECTION {
        UNKNOWN, INC, DEC;

        companion object {
            fun getDirection(sum: Long) : DIRECTION {
                return if(sum == 0L) {
                    UNKNOWN
                } else if(sum < 0) {
                    DEC
                } else INC
            }
        }
    }



    fun part1() : Int {
        val map = input.map { report ->
            report.zipWithNext().fold(Pair(DIRECTION.UNKNOWN, true)) { acc, pair ->
                if (!acc.second) acc
                else {
                    val minus = pair.first - pair.second
                    if (abs(minus) > 3 || minus == 0L) acc.first to false
                    else {
                        when (acc.first) {
                            DIRECTION.UNKNOWN -> {
                                if (minus < 0) DIRECTION.DEC to true
                                else DIRECTION.INC to true
                            }
                            DIRECTION.INC -> if (minus < 0) DIRECTION.DEC to false else acc.first to true
                            DIRECTION.DEC -> if (minus > 0) DIRECTION.INC to false else acc.first to true
                        }
                    }
                }
            }
        }
        return map.count { it.second }
    }

    data class ReportNode(val number: Long,
                          val parent: ReportNode? = null) {

        fun getDirection() : DIRECTION {
            return if(parent == null) DIRECTION.UNKNOWN
            else DIRECTION.getDirection(number - parent.number)
        }

        fun diff() : Long {
            return number - (parent?.number ?: 0)
        }

        fun isValid() : Boolean {
            if(parent == null) return true
            if (diff() == 0L) return false
            if (abs(diff()) > 3) return false
            return when(parent.getDirection()) {
                DIRECTION.UNKNOWN -> true
                DIRECTION.INC -> getDirection() == DIRECTION.INC
                DIRECTION.DEC -> getDirection() == DIRECTION.DEC
            }
        }
    }

    fun part2() : Int {

        fun createTree(list: List<Long>, parent: ReportNode?, acc : List<ReportNode>, dropped: Int): List<Pair<Int, List<ReportNode>>> {
            if(list.isEmpty()) return listOf(dropped to acc)
            val node = ReportNode(list.first(), parent)
            return createTree(list.drop(1), node, acc + node, dropped) +
                    createTree(list.drop(1), parent, acc, dropped + 1)
        }

        fun isTreeValid(dropped: Int, tree: List<ReportNode>) : Boolean {
            if(dropped > 1) return false
            return tree.all { it.isValid() }
        }

        val trees = input.map { report ->
            createTree(report, null, emptyList(), 0)
        }.map {
            it.filter { (dropped, _) -> dropped <= 1 }
        }

        return trees.count { tree ->
            tree.any { (dropped, nodes) -> isTreeValid(dropped, nodes) }
        }

    }

    fun run() {
        println("Day 1")
        println("Part 1: ${part1()}")
        println("Part 2: ${part2()}")
    }

}

fun main() {
    Day2().run()
}