package spaceEngineers

import kotlin.test.*
import spaceEngineers.commands.SeSessionCommand
import testhelp.controller

@Ignore("Not ready for unit testing")
class SessionRequestTest {
    // Ignore annotation somehow not working in IDEA
    //@Test
    fun sessionLoadTest() = controller {
        // TODO(PP): support relative path
        val request = SeRequest.session(SeSessionCommand.load(
            "C:\\Users\\<user>\\AppData\\Roaming\\SpaceEngineers\\Saves\\76561198248453892\\Alien Planet 2020-09-18 12-41"))
        //TODO: call request using CharacterController api
    }
}