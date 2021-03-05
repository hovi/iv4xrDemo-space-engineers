package spaceEngineers

import eu.iv4xr.framework.spatial.Vec3
import org.junit.jupiter.api.Test
import spaceEngineers.commands.MovementArgs
import spaceEngineers.commands.SeAgentCommand
import testhelp.TEST_AGENT
import testhelp.checkMockObservation
import testhelp.environment
import java.util.*
import java.util.function.BiConsumer


class MoveAgentTest {

    @Test
    fun moveTest() = environment {
        // Single move request is not actually visible on the character movement, see manyMovesTest below
        val observation = getSeResponse(
                SeRequest.command(
                        SeAgentCommand.moveTowardCommand(TEST_AGENT, Vec3(0f, 0f, -1f), false)
                )
        )
        checkMockObservation(observation)
    }


    @Test
    fun moveAndRotateTest() = environment {
        // Single move request is not actually visible on the character movement, see manyMovesTest below
        val observation = getSeResponse(
                SeRequest.command(
                        SeAgentCommand.moveAndRotate(TEST_AGENT, Vec3(0f, 0f, -1f), Vec3(0.3f, 0f, 0f), 0f)
                )
        )
        checkMockObservation(observation)
    }

    @Test
    fun manyMovesTest() = environment {
        val moves = ArrayList<Vec3>()
        val addMoves = BiConsumer { move: Vec3, count: Int -> for (n in 0 until count) moves.add(move) }
        val stepBoost = 1 // Increase to 10 to 20 to slow down the movement (and see it better)
        addMoves.accept(Vec3(0f, 0f, -1f), 4 * stepBoost) // Forward
        addMoves.accept(Vec3(1f, 0f, 0f), 1 * stepBoost) // Right
        addMoves.accept(Vec3(0f, 0f, -1f), 2 * stepBoost)
        addMoves.accept(Vec3(-1f, 0f, 0f), 2 * stepBoost) // Left
        moves.add(Vec3(0f, 0.5f, 0f)) // Up
        for (move in moves) {
            val observation = getSeResponse(
                    SeRequest.command(
                            SeAgentCommand.moveTowardCommand(TEST_AGENT, move, false)
                    )
            )
            checkMockObservation(observation)
        }
    }

    @Test
    fun manyMovesAndRotationsTest() = environment {
        val moves = ArrayList<MovementArgs>()
        val addMoves =
                BiConsumer { movementArgs: MovementArgs, count: Int -> for (n in 0 until count) moves.add(movementArgs) }
        val stepBoost = 1 // Increase to 10 to 20 to slow down the movement (and see it better)
        val forwardArgs = MovementArgs(Vec3(0f, 0f, -1f))
        addMoves.accept(forwardArgs, 3 * stepBoost)
        val rotateArgs = MovementArgs(Vec3.zero(), Vec3(0f, 9f, 0f), 0f)
        addMoves.accept(rotateArgs, 2 * stepBoost)
        addMoves.accept(forwardArgs, 3 * stepBoost)
        addMoves.accept(MovementArgs(Vec3(-1f, 0f, 0f)), 20 * stepBoost) // Left
        val rollArgs = MovementArgs(Vec3.zero(), Vec3.zero(), -2f)
        addMoves.accept(rollArgs, 2 * stepBoost)
        val allArgs = MovementArgs(Vec3(0f, 0.7f, 0.2f), Vec3(5f, 7f, 0f), 1.5f)
        addMoves.accept(allArgs, 4 * stepBoost)
        for (move in moves) {
            val observation = getSeResponse(
                    SeRequest.command(
                            SeAgentCommand.moveAndRotate(TEST_AGENT, move)
                    )
            )
            checkMockObservation(observation)
        }
    }
}