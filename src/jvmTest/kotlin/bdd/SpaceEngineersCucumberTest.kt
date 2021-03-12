package bdd

import environments.closeIfCloseable
import io.cucumber.java.After
import io.cucumber.java.Before
import io.cucumber.java.en.Given
import io.cucumber.java.en.Then
import io.cucumber.java.en.When
import io.cucumber.junit.Cucumber
import org.junit.runner.RunWith
import spaceEngineers.commands.InteractionArgs
import spaceEngineers.commands.InteractionType
import spaceEngineers.commands.ObservationArgs
import spaceEngineers.commands.ObservationMode
import spaceEngineers.controller.CharacterController
import spaceEngineers.controller.ProprietaryJsonTcpCharacterController
import spaceEngineers.controller.WorldController
import spaceEngineers.controller.observe
import spaceEngineers.game.blockingMoveForwardByDistance
import spaceEngineers.model.SeBlock
import spaceEngineers.model.SeObservation
import spaceEngineers.model.Vec3
import spaceEngineers.model.allBlocks
import testhelp.*
import java.io.File
import java.lang.Thread.sleep
import kotlin.test.assertEquals
import kotlin.test.assertTrue

@RunWith(Cucumber::class)
class SpaceEngineersCucumberTest {
    lateinit var environment: CharacterController

    val observations: MutableList<SeObservation> = mutableListOf()

    val context = SpaceEngineersTestContext()

    @Before
    fun setup() {
        observations.clear()
    }

    @After
    fun cleanup() {
        observations.clear()
        if (this::environment.isInitialized) {
            environment.closeIfCloseable()
        }
    }

    fun CharacterController.equip(inventoryLocation: InventoryLocation) {
        interact(InteractionArgs(InteractionType.EQUIP, slot = inventoryLocation.slot, page = inventoryLocation.page))
    }

    @Given("I am using mock data source.")
    fun i_am_connected_to_mock_server() {
        environment =
            ProprietaryJsonTcpCharacterController.mock(agentId = TEST_AGENT, lineToReturn = TEST_MOCK_RESPONSE_LINE)
    }

    @Given("I am connected to real game.")
    fun i_am_connected_to_real_game() {
        environment = ProprietaryJsonTcpCharacterController.localhost(agentId = TEST_AGENT)
    }

    @Given("Inventory has mapping:")
    fun inventory_has_mapping(dataTable: List<Map<String, String>>) {
        context.updateInventoryLocation(dataTable)
    }

    @Given("Grinder is in slot {int}, page {int}.")
    fun grinder_is_at(slot: Int, page: Int) {
        context.grinderLocation = InventoryLocation(slot = slot, page = page)
    }

    @Given("Torch is in slot {int}, page {int}.")
    fun torch_is_at(slot: Int, page: Int) {
        context.torchLocation = InventoryLocation(slot = slot, page = page)
    }

    @Given("I load scenario {string}.")
    fun i_load_scenario(scenarioId: String) {
        environment?.let {
            check(it is WorldController)
            it.load(File("$SCENARIO_DIR$scenarioId").absolutePath)
        }
        sleep(500)
        // All blocks are new for the first request.
        environment.observe(ObservationArgs(ObservationMode.NEW_BLOCKS)).let {
            observations.add(it)
        }
    }

    @When("I request for blocks.")
    fun i_request_for_blocks() {
        environment.observe(ObservationArgs(ObservationMode.BLOCKS)).let { observations.add(it) }
    }

    @When("I observe.")
    fun i_observe() {
        environment.observe().let { observations.add(it) }
    }

    @Then("Character is at \\({double}, ?{double}, ?{double}).")
    fun i_see_character_at_x_y_z(x: Double, y: Double, z: Double) {
        val position = Vec3(x, y, z)
        observations.last().let { observation ->
            assertVecEquals(position, observation.position, diff = 0.1f)
        }
    }

    @Then("Character forward orientation is \\({double}, {double}, {double}).")
    fun character_is_facing(x: Double, y: Double, z: Double) {
        val position = Vec3(x, y, z)
        observations.last().let { observation ->
            assertVecEquals(position, observation.orientationForward, diff = 0.1f)
        }
    }

    @When("Character moves forward for {int} units.")
    fun character_moves_forward_for_units(units: Int) {
        environment.blockingMoveForwardByDistance(distance = units.toFloat()).let { observations.add(it) }
    }

    @Then("Character is {int} units away from starting location.")
    fun character_is_units_away_from_starting_location(units: Int) {
        assertFloatEquals(
            units.toFloat(),
            observations.first().position.distanceTo(observations.last().position)
        )
    }

    private fun observeBlocks(): List<SeBlock> {
        return environment.observe(ObservationArgs(ObservationMode.BLOCKS)).allBlocks
    }

    private fun blockToGrind(): SeBlock {
        return observeBlocks().first { it.id == context.lastNewBlockId }
    }

    @When("Character grinds to {double} integrity.")
    fun character_grinds_until_to_integrity(integrity: Double) {
        var currentIntegrity = blockToGrind().integrity
        environment.equip(context.grinderLocation!!)
        sleep(500)
        environment.interact(InteractionArgs(InteractionType.BEGIN_USE))
        while (currentIntegrity > integrity) {
            currentIntegrity = blockToGrind().integrity
        }
        environment.interact(InteractionArgs(InteractionType.END_USE))
    }

    @When("Character grinds to {double}% integrity.")
    fun character_grinds_until_to_integrity_percentage(percentage: Double) {
        val blockToGrind = blockToGrind()
        var currentIntegrity = blockToGrind.integrity
        val integrity = blockToGrind.maxIntegrity * percentage * 0.01
        environment.equip(context.grinderLocation!!)
        sleep(500)
        environment.interact(InteractionArgs(InteractionType.BEGIN_USE))
        while (currentIntegrity > integrity) {
            currentIntegrity = blockToGrind().integrity
        }
        environment.interact(InteractionArgs(InteractionType.END_USE))
    }

    @When("Character torches block back up to max integrity.")
    fun character_torches_block_back_up_to_max_integrity() {
        val blockToGrind = blockToGrind()
        var currentIntegrity = blockToGrind.integrity
        environment.equip(context.torchLocation!!)
        sleep(500)
        environment.interact(InteractionArgs(InteractionType.BEGIN_USE))
        while (currentIntegrity < blockToGrind.maxIntegrity) {
            currentIntegrity = blockToGrind().integrity

        }
        environment.interact(InteractionArgs(InteractionType.END_USE))
    }


    @Then("I receive observation.")
    fun i_receive_observation() {
        assertTrue(observations.isNotEmpty())
    }

    @Then("I see {int} grid with {int} block.")
    fun i_see_grid_and_with_block(grids: Int, blocks: Int) {
        val observation = observations.last()
        assertEquals(grids, observation.grids?.size)
        assertEquals(blocks, observation.grids?.first()?.blocks?.size)
    }

    @When("Character selects block {string} and places it.")
    fun character_places_selects_block_and_places_it(blockType: String) {
        val inventoryLocation: InventoryLocation = context.blockInventoryLocation(blockType)
        environment.interact(InteractionArgs(InteractionType.EQUIP, inventoryLocation.slot, inventoryLocation.page))
        environment.interact(InteractionArgs(InteractionType.PLACE))
    }

    @Then("I see no block of type {string}.")
    fun i_see_no_block_of_type(string: String) {
        val observation = environment.observe(ObservationArgs(ObservationMode.BLOCKS))
        observations.add(observation)
        assertTrue(
            observation.allBlocks
                .none { it.blockType == string }
        )
    }

    @Then("I can see {int} new block\\(s) with data:")
    fun i_can_see_new_block_with_data(blockCount: Int, data: List<Map<String, String>>) {
        val observation = environment.observe(ObservationArgs(ObservationMode.NEW_BLOCKS))
        observations.add(observation)
        val allBlocks = observation.allBlocks
        assertEquals(
            blockCount,
            allBlocks.size,
            "Expected to see $blockCount blocks, not ${allBlocks.size} ${allBlocks.map { it.blockType }.toSet()}."
        )
        assertEquals(allBlocks.size, data.size)
        data.forEachIndexed { index, row ->
            val block = allBlocks[index]
            context.lastNewBlockId = block.id
            row["blockType"]?.let {
                assertEquals(it, block.blockType)
            }
            row["integrity"]?.let {
                assertEquals(it.toFloat(), block.integrity)
            }
            row["maxIntegrity"]?.let {
                assertEquals(it.toFloat(), block.maxIntegrity)
            }
            row["buildIntegrity"]?.let {
                assertEquals(it.toFloat(), block.buildIntegrity)
            }
        }
    }

    @Then("Block with id {string} has {float} max integrity, {float} integrity and {float} build integrity.")
    fun block_with_id_blk_has_max_integrity_integrity_and_build_integrity(
        id: String, maxIntegrity: Float, integrity: Float, buildIntegrity: Float
    ) {
        val observation = observations.last()
        val block = observation.grids.flatMap { it.blocks }.find { it.id == id } ?: error("block $id not found")
        assertEquals(maxIntegrity, block.maxIntegrity)
        assertEquals(buildIntegrity, block.buildIntegrity)
        assertEquals(integrity, block.integrity)
    }
}