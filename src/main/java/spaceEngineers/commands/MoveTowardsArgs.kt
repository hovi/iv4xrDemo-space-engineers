package spaceEngineers.commands

import eu.iv4xr.framework.spatial.Vec3

class MoveTowardsArgs @JvmOverloads
constructor(
    val object1: Vec3,
    val object2: Boolean
)