package fi.solita.hnybom.aoc2024

import fi.solita.hnybom.aoc2024.common.Common
import kotlin.math.abs

class Day2 {

    private val input = Common.readInput(2).readLines()
        .map { row ->
            val split = row.split(" ")
            split.map { it.toLong() }
        }


    private fun isSafe(report: List<Long>): Boolean {
        val diffs = report.zipWithNext().map { (a, b) -> b - a }
        return diffs.all { it in 1..3 } || diffs.all { it in -3..-1 }
    }

    fun part1() : Int {
        val map = input.map { report ->
            isSafe(report)
        }
        return map.count { it }
    }

    data class ReportNode(val number: Long)

    fun part2() : Int {

        fun createTree(list: List<Long>, parent: ReportNode?, acc : List<ReportNode>, dropped: Int): List<List<ReportNode>> {
            if(dropped > 1) return emptyList()
            if(list.isEmpty()) return listOf(acc)
            val node = ReportNode(list.first())
            return createTree(list.drop(1), node, acc + node, dropped) +
                    createTree(list.drop(1), parent, acc, dropped + 1)
        }

        fun isTreeValid(tree: List<ReportNode>) : Boolean {
            return isSafe(tree.map { it.number })
        }

        val trees = input.map { report ->
            createTree(report, null, emptyList(), 0)
        }

        return trees.count { tree ->
            tree.any { nodes -> isTreeValid(nodes) }
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