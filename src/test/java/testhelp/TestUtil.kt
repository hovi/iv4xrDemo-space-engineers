package testhelp

import eu.iv4xr.framework.spatial.Vec3
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Assertions.assertTrue
import spaceEngineers.*

const val TEST_AGENT = "you"

fun environment(
    environment: SpaceEngEnvironment = SpaceEngEnvironment.localhost(),
    block: SpaceEngEnvironment.() -> Unit
) {
    try {
        block(environment)
    } finally {
        environment.close()
    }
}

fun controller(
    agentId: String = TEST_AGENT,
    characterController: BaseCharacterController = BaseCharacterController.localhost(agentId = agentId),
    block: BaseCharacterController.() -> Unit
) {
    try {
        block(characterController)
    } finally {
        characterController.socketReaderWriter.close()
    }
}

fun checkMockObservation(observation: SeObservation?) {
    Assertions.assertNotNull(observation)
    Assertions.assertEquals("Mock", observation?.agentID)
    Assertions.assertEquals(Vec3(4.0f, 2.0f, 0.0f), observation?.position)
}