package spaceEngineers.commands

import eu.iv4xr.framework.spatial.Vec3

//TODO: name better on both sides (or maybe obsolete completely?)
class MoveTowardsArgs @JvmOverloads
constructor(
    val object1: Vec3,
    val object2: Boolean
)