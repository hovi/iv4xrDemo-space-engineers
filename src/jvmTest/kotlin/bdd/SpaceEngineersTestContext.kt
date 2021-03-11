package bdd

data class SpaceEngineersTestContext(
    var grinderLocation: InventoryLocation? = null,
    var torchLocation: InventoryLocation? = null,
    var lastNewBlockId: String? = null,
    val blockTypeToInventoryLocation: MutableMap<String, InventoryLocation> = mutableMapOf()
) {

    fun updateInventoryLocation(dataTable: List<Map<String, String>>) {
        blockTypeToInventoryLocation.clear()
        blockTypeToInventoryLocation.putAll(
            dataTable.map {
                it["blockType"]!! to InventoryLocation(slot = it["slot"]!!.toInt(), page = it["page"]!!.toInt())
            }.toMap()
        )
    }

    fun blockInventoryLocation(blockType: String): InventoryLocation {
        return blockTypeToInventoryLocation[blockType] ?: error("Inventory location not set for type $blockType")
    }
}