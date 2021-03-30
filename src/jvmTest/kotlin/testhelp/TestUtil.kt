package testhelp

import environments.closeIfCloseable
import spaceEngineers.controller.CharacterController
import spaceEngineers.controller.ContextControllerWrapper
import spaceEngineers.controller.ProprietaryJsonTcpCharacterController
import java.io.File

const val TEST_AGENT = "you"


val TEST_MOCK_RESPONSE_LINE = """
{"AgentID":"Mock","Position":{"X":4.0,"Y":2.0,"Z":0.0},"OrientationForward":{"X":0.0,"Y":0.0,"Z":0.0},"OrientationUp":{"X":0.0,"Y":0.0,"Z":0.0},"Velocity":{"X":0.0,"Y":0.0,"Z":0.0},"Extent":{"X":0.0,"Y":0.0,"Z":0.0},"Entities":[{"Id":"Ente","Position":{"X":3.0,"Y":2.0,"Z":1.0}}],"Grids":[{"Blocks":[{"MaxIntegrity":10.0,"BuildIntegrity":1.0,"Integrity":5.0,"BlockType":"MockBlock","MinPosition":{"X":0.0,"Y":0.0,"Z":0.0},"MaxPosition":{"X":0.0,"Y":0.0,"Z":0.0},"Size":{"X":0.0,"Y":0.0,"Z":0.0},"OrientationForward":{"X":0.0,"Y":0.0,"Z":0.0},"OrientationUp":{"X":0.0,"Y":0.0,"Z":0.0},"Id":"blk","Position":{"X":5.0,"Y":5.0,"Z":5.0}}],"Id":null,"Position":{"X":5.0,"Y":5.0,"Z":5.0}}]}
""".trim()

val SCENARIO_DIR = "src/jvmTest/resources/game-saves/"

val TEST_SCENARIO_1_ID = File("${SCENARIO_DIR}Scenario1").absolutePath

fun mockController(
    response: String = TEST_MOCK_RESPONSE_LINE,
    agentId: String = TEST_AGENT,
    characterController: CharacterController = ProprietaryJsonTcpCharacterController.mock(
        agentId = agentId,
        lineToReturn = response
    ),
    block: CharacterController .() -> Unit
) {
    controller(agentId = agentId, characterController = characterController, block = block)
}

fun controller(
    agentId: String = TEST_AGENT,
    characterController: CharacterController = ProprietaryJsonTcpCharacterController.localhost(agentId),
    block: CharacterController.() -> Unit
) {
    try {
        block(characterController)
    } finally {
        characterController.closeIfCloseable()
    }
}

fun controllerWrapper(
    agentId: String = TEST_AGENT,
    characterController: ContextControllerWrapper = ContextControllerWrapper(
        ProprietaryJsonTcpCharacterController.localhost(
            agentId
        )
    ),
    block: ContextControllerWrapper.() -> Unit
) {
    try {
        block(characterController)
    } finally {
        characterController.closeIfCloseable()
    }
}