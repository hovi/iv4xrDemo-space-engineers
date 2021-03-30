package spaceEngineers.controller

import spaceEngineers.commands.*
import spaceEngineers.model.SeObservation
import spaceEngineers.model.ToolbarLocation
import spaceEngineers.model.Vec3

fun CharacterController.equip(toolbarLocation: ToolbarLocation) {
    interact(InteractionArgs.equip(toolbarLocation = toolbarLocation))
}

fun CharacterController.moveForward(velocity: Float = 1f): SeObservation {
    moveAndRotate(MovementArgs(movement = Vec3(0f, 0f, -velocity)))
    return observe()
}

fun CharacterController.blockingMoveForwardByDistance(
    distance: Float,
    velocity: Float = 1f,
    maxTries: Int = 1000
): SeObservation {
    val startPosition = observe().position
    repeat(maxTries) {
        moveForward(velocity = velocity).let {
            if (it.position.distanceTo(startPosition) >= distance) {
                return it
            }
        }
    }
    error("timeout after $maxTries tries, currentDistance: ${observe().position.distanceTo(startPosition)}")
}

fun CharacterController.blockingRotateUntilOrientationForward(
    finalOrientation: Vec3,
    rotation: Vec3,
    delta: Float = 0.01f,
    maxTries: Int = 1000,
): SeObservation {
    repeat(maxTries) {
        val observation = moveAndRotate(MovementArgs(rotation3 = rotation))
        observation.let { it.orientationForward }
            .let { orientationForward ->
                if (finalOrientation.similar(orientationForward, delta = delta)) {
                    println("DONE")
                    return observation
                }
            }
    }
    error("timeout after $maxTries tries")
}

fun CharacterController.observe(): SeObservation {
    return observe(ObservationArgs())
}

fun CharacterController.observe(observationMode: ObservationMode): SeObservation {
    return observe(ObservationArgs(observationMode))
}
