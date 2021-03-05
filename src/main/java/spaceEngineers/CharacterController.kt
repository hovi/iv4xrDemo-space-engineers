package spaceEngineers

import com.google.gson.FieldNamingPolicy
import com.google.gson.GsonBuilder
import environments.SocketReaderWriter
import environments.sendAndReceiveLine
import spaceEngineers.commands.MoveTowardsArgs
import spaceEngineers.commands.MovementArgs
import spaceEngineers.commands.SeAgentCommand
import java.lang.reflect.Modifier


interface Vector3: Vector2 {
    val z: Float
}

interface Vector2 {
    val x: Float
    val y: Float
}


interface CharacterController {

    fun moveAndRotate(movementArgs: MovementArgs): SeObservation

    fun moveTowards(moveTowardsArgs: MoveTowardsArgs): SeObservation
}

class BaseCharacterController(val agentId: String = "me", val socketReaderWriter: SocketReaderWriter) :
    CharacterController {

    val gson = socketReaderWriter.gson

    override fun moveAndRotate(movementArgs: MovementArgs): SeObservation {
        val request = SeRequest.command(SeAgentCommand.moveAndRotate(agentId, movementArgs))
        val responseJson = socketReaderWriter.sendAndReceiveLine(gson.toJson(request))
        return gson.fromJson(responseJson, request.responseType)
    }

    override fun moveTowards(moveTowardsArgs: MoveTowardsArgs): SeObservation {
        val request = SeRequest.command(SeAgentCommand.moveTowardCommand(agentId, moveTowardsArgs))
        val responseJson = socketReaderWriter.sendAndReceiveLine(gson.toJson(request))
        return gson.fromJson(responseJson, request.responseType)
    }

    companion object {
        const val DEFAULT_PORT = 9678

        private val SPACE_ENG_GSON = GsonBuilder()
            .serializeNulls()
            .setFieldNamingPolicy(FieldNamingPolicy.UPPER_CAMEL_CASE)
            .excludeFieldsWithModifiers(Modifier.TRANSIENT)
            .create()

        fun localhost(agentId: String = "me"): BaseCharacterController {
            return BaseCharacterController(
                agentId = agentId,
                socketReaderWriter = SocketReaderWriter(
                    host = "localhost",
                    port = DEFAULT_PORT,
                    gson = SPACE_ENG_GSON
                )
            )
        }
    }
}