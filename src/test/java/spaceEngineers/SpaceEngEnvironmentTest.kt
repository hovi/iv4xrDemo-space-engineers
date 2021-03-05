package spaceEngineers

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import spaceEngineers.SeRequest
import spaceEngineers.commands.ObservationArgs
import spaceEngineers.commands.ObservationMode
import spaceEngineers.commands.SeAgentCommand
import testhelp.TEST_AGENT
import testhelp.checkMockObservation
import testhelp.environment

class SpaceEngEnvironmentTest {
    @Test
    fun disconnectTest() = environment { 
    }

    @Test
    fun observeTest() = environment {
        val observation = getSeResponse(SeRequest.command(SeAgentCommand.observe(TEST_AGENT)))
        checkMockObservation(observation)
        println("OrientationFwd: " + observation.orientationForward)
        println("OrientationUp : " + observation.orientationUp)
    }

    @Test
    fun observeManyTimesTest() = environment {
        for (i in 0..4) {
            val observation = getSeResponse(SeRequest.command(SeAgentCommand.observe(TEST_AGENT)))
            checkMockObservation(observation)
        }
    }

    @Test
    fun observeEntitiesTest() = environment {
        val observation = getSeResponse(SeRequest.command(
                SeAgentCommand.observe(TEST_AGENT, ObservationArgs(ObservationMode.ENTITIES))))
        assertNotNull(observation)
        assertNotNull(observation.entities)
        assertTrue(observation.entities.size > 0)
        println("Got " + observation.entities.size + " entities.")
        println("First entity position: " + observation.entities[0].position)
    }

    @Test
    fun observeBlocksTest() = environment {
        val observation = getSeResponse(SeRequest.command(
                SeAgentCommand.observe(TEST_AGENT, ObservationArgs(ObservationMode.BLOCKS))))
        checkMockObservation(observation)
        val blocks = observation.grids.first().blocks
        assertNotNull(blocks)
        assertTrue(blocks.size > 0, "Expecting non-zero block count.")
        assertEquals(1, observation.grids.size, "Expecting 1 grid count.")
        assertEquals(1, observation.grids[0].blocks.size, "Expecting 1 grid block count.")
        println("Got " + blocks.size + " blocks.")
        val firstBlock = blocks.first()
        println("First block max integrity: " + firstBlock.maxIntegrity)
        println("First block build integrity: " + firstBlock.buildIntegrity)
        println("First block integrity: " + firstBlock.integrity)
    }
}