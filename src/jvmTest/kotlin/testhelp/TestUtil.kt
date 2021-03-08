package testhelp

import environments.closeIfCloseable
import spaceEngineers.controller.CharacterController
import spaceEngineers.controller.ProprietaryJsonTcpCharacterController

const val TEST_AGENT = "you"

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