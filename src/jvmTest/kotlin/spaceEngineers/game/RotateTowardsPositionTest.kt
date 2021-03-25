package spaceEngineers.game

import spaceEngineers.controller.WorldController
import spaceEngineers.controller.blockingRotateUntilOrientationForward
import spaceEngineers.model.ToolbarLocation
import spaceEngineers.model.Vec3
import spaceEngineers.model.allBlocks
import testhelp.*
import java.io.File
import java.lang.Thread.sleep
import kotlin.test.Test


fun WorldController.loadScenario(scenarioId: String) {
    load(File("$SCENARIO_DIR$scenarioId").absolutePath)
}

class RotateTowardsPositionTest {


    @Test
    fun rotateTowardsPosition() = controllerWrapper {
        check(controller is WorldController)
        (controller as WorldController).loadScenario("simple-place-grind-torch-with-tools")
        sleep(1000)
        observeNewBlocks()
        equip(ToolbarLocation(0, 0))
        sleep(1000)
        place()
        sleep(1000)
        equip(ToolbarLocation(9, 0))

        val observation = observeNewBlocks()
        val block = observation.allBlocks.first()
        println(block.position)
        println(observation.position)
        println(observation.orientationForward)
        val orientationTowardsBlock = (block.position - observation.position).normalized()
        println(orientationTowardsBlock)
        assertVecEquals(orientationTowardsBlock, observation.orientationForward, diff = 0.1f)

        blockingRotateUntilOrientationForward(
            finalOrientation = Vec3(0f, 0f, -1f),
            rotation = Vec3.ROTATE_RIGHT,
            maxTries = 999999
        )


    }
}