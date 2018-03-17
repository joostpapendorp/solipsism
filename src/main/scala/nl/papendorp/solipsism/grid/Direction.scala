package nl.papendorp.solipsism.grid

import scala.collection.breakOut

sealed trait Direction
{
}

/*
 * a Direction represents a single step along an axis, keeping that position constant.
 * "Up" and "Down" are a bit arbitrary, because each step increases the first of the other two axes, while decrementing
 * the second. Each "Up" inverts its "Down" counterpart.
 * Taken all together, they form a circle around Origin.
 * For example: XUp walks along the x-axis, thus keeping x constant. It increments y and decrements z. It inverts XDown.
 */
object Direction extends Enumeration
{
	// in order of clockwise rotation:
	val XUp,       // (  0, +y, -z )
	YUp,           // ( +x,  0, -z )
	ZUp,           // ( +x, -y,  0 )
	XDown,         // (  0, -y, +z )
	YDown,         // ( -x,  0, +z )
	ZDown = Value  // ( -x, +y,  0 )

	lazy val directions: Map[ Direction.Value, HexPoint ] = {
		(Direction.values zip (0, 1).circle) ( breakOut )
	}
}