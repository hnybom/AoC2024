package fi.solita.hnybom.aoc2024

import fi.solita.hnybom.aoc2024.Day9.StorageBlock
import fi.solita.hnybom.aoc2024.common.Common
import fi.solita.hnybom.aoc2024.common.Coordinate
import kotlin.math.abs
import kotlin.math.ceil

class Day9 {

    data class Space(val id: Long, val isFree: Boolean, val size: Long)

    data class StorageBlock(val id: Long, val isFree: Boolean)

    data class SpaceSwap(val space: List<Space>, val didRearrange: Boolean, val shouldContinue: Boolean)

    private val input = Common.readInput(9).readLines()

    private val storage = input.first().mapIndexed { index, c ->
        Space(ceil( index.toLong() / 2f).toLong(), index % 2 != 0, c.toString().toLong())
    }

    private val storageBlocks = spaceToBlocks(storage)

    private fun spaceToBlocks(space: List<Space>): List<StorageBlock> {
        return space.flatMap { s ->
            (1..s.size).map { i ->
                StorageBlock(s.id, s.isFree)
            }
        }
    }

    private fun swapFreeSpace(space: List<StorageBlock>): Pair<List<StorageBlock>, Boolean> {
        val firstFreeSpace = space.find { it.isFree }
        val firstFreeSpaceIndex = space.indexOf(firstFreeSpace)

        val lastOccupied = space.findLast { !it.isFree }
        val lastOccupiedIndex = space.lastIndexOf(lastOccupied)

        if (firstFreeSpace == null || lastOccupied == null
            || firstFreeSpaceIndex > lastOccupiedIndex) return Pair(space, false)

        return space.mapIndexed { index, c ->
            when (index) {
                firstFreeSpaceIndex -> lastOccupied
                lastOccupiedIndex -> firstFreeSpace
                else -> c
            }
        } to true

    }

    private tailrec fun reArrange(space: List<StorageBlock>): List<StorageBlock> {
        val swap = swapFreeSpace(space)
        return if(swap.second) reArrange(swap.first)
        else swap.first
    }

    private fun swapFreeSpace(space: List<Space>, skipped: Long): SpaceSwap {
        var currentlySkipped = 0L
        val lastOccupied = space.dropLastWhile {
            if(!it.isFree) currentlySkipped++
            currentlySkipped < skipped
        }.findLast { !it.isFree }

        if (lastOccupied == null) return SpaceSwap(space,  false, false)
        val lastOccupiedIndex = space.lastIndexOf(lastOccupied)

        val firstFreeSpace = space.find { it.isFree && it.size >= lastOccupied.size }
        if (firstFreeSpace == null) return SpaceSwap(space,  false, true)

        val firstFreeSpaceIndex = space.indexOf(firstFreeSpace)

        if (firstFreeSpaceIndex > lastOccupiedIndex) return SpaceSwap(space,  false, true)

        val newSpace = space.flatMapIndexed { index: Int, currentSpace: Space ->

            when (index) {
                firstFreeSpaceIndex -> {
                    val spaceFreeAfterMove = currentSpace.size - lastOccupied.size
                    if(spaceFreeAfterMove > 0) listOf(lastOccupied,  currentSpace.copy(size = spaceFreeAfterMove))
                    else listOf(lastOccupied)
                }
                lastOccupiedIndex -> listOf(lastOccupied.copy(isFree = true))
                else -> listOf(currentSpace)
            }
        }
        return SpaceSwap(newSpace, true, true)

    }

    private fun reArrange(space: List<Space>, skipped: Long): List<Space> {
        val swap = swapFreeSpace(space, skipped)
        return if(swap.shouldContinue) reArrange(swap.space, if(!swap.didRearrange) skipped + 1 else skipped)
        else swap.space
    }

    private fun calculateChecksum(storage: List<StorageBlock>): Long {
        storage.forEach {
            if(it.isFree) print(".")
            else print(it.id)
        }
        println()

        return storage.foldIndexed(0L) { index, acc, storageBlock ->
            if(storageBlock.isFree) acc
            else index * storageBlock.id + acc
        }
    }


    fun part1(): Long {
        val reArranged = reArrange(storageBlocks)
        return calculateChecksum(reArranged)
    }

    fun part2(): Long {
        val reArranged = reArrange(storage, 0)
        return calculateChecksum(spaceToBlocks(reArranged))
    }

}

fun main() {
    val day9 = Day9()
    println("Day 8")
    // Add calls to part1 and part2 when implemented
    println("Part 1: ${day9.part1()}")
    println("Part 2: ${day9.part2()}")
}