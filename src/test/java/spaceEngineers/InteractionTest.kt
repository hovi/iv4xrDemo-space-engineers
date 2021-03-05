package spaceEngineers

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import spaceEngineers.SeRequest
import spaceEngineers.commands.*
import testhelp.checkMockObservation
import testhelp.environment


class InteractionTest {
    @Test
    fun equipToolbarItemTest() = environment {
        val observation = getSeResponse(SeRequest.command(
                SeAgentCommand.interact("you", InteractionArgs(InteractionType.EQUIP, 4, - 1, true))))
        checkMockObservation(observation)
    }


    @Test
    fun pageAndEquipTest() = environment {
        val observation = getSeResponse(SeRequest.command(
                SeAgentCommand.interact("you", InteractionArgs(InteractionType.EQUIP, 4, 2))))
        checkMockObservation(observation)
    }

    @Test
    fun equipAndPlace() = environment {
        var observation = getSeResponse(SeRequest.command(
                SeAgentCommand.interact("you", InteractionArgs(InteractionType.EQUIP, 3))))
        checkMockObservation(observation)
        observation = getSeResponse(SeRequest.command(
                SeAgentCommand.interact("you", InteractionArgs(InteractionType.PLACE))))
        checkMockObservation(observation)
    }

    @Test
    fun checkNewBlock()  = environment {
        var observation = getSeResponse(SeRequest.command(
                SeAgentCommand.observe("you", ObservationArgs(ObservationMode.NEW_BLOCKS))))
        assertNotNull(observation)
        assertNotNull(observation.grids)
        println("Got " + observation.grids.size + " grids.")
        for (grid in observation.grids) {
            println("Got " + grid.blocks.size + " blocks.")
        }
        observation = getSeResponse(SeRequest.command(
                SeAgentCommand.interact("you", InteractionArgs(InteractionType.EQUIP, 3, 0))))
        assertNotNull(observation)
        observation = getSeResponse(SeRequest.command(
                SeAgentCommand.interact("you", InteractionArgs(InteractionType.PLACE))))
        assertNotNull(observation)
        observation = getSeResponse(SeRequest.command(
                SeAgentCommand.observe("you", ObservationArgs(ObservationMode.NEW_BLOCKS))))
        assertNotNull(observation)
        assertNotNull(observation.grids)
        assertTrue(observation.grids.size > 0, "Expecting non-zero grid count.")
        var blocks: List<SeBlock>? = null
        for (grid in observation.grids) {
            blocks = grid.blocks
            println("Got " + blocks.size + " blocks.")
            if (blocks.size > 0) // Take first nonempty grid.
                break
        }
        for (block in blocks!!.subList(0, Math.min(2, blocks.size))) {
            println("Block max integrity: " + block.maxIntegrity)
            println("Block build integrity: " + block.buildIntegrity)
            println("Block integrity: " + block.integrity)
            println("Block type: " + block.blockType)
            println("Block position min: " + block.minPosition)
            println("Block position max: " + block.maxPosition)
            println("Block size        : " + block.size)
            println("Block orientation fwd: " + block.orientationForward)
            println("Block orientation up : " + block.orientationUp)
        }
        assertEquals(1, blocks.size, "There should be exactly 1 new block.")
    }
}