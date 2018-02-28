package nl.papendorp.solipsism.grid

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
object Direction
{

	// in order of clockwise rotation:
	case object XUp extends Direction //   (  0, +y, -z )

	case object YUp extends Direction //   ( +x,  0, -z )

	case object ZUp extends Direction //   ( +x, -y,  0 )

	case object XDown extends Direction // (  0, -y, +z )

	case object YDown extends Direction // ( -x,  0, +z )

	case object ZDown extends Direction // ( -x, +y,  0 )

	lazy val directions: Map[ Direction, HexPoint ] = {
		val names = Seq( XUp, YUp, ZUp, XDown, YDown, ZDown )
		(names zip (0, 1).circle).toMap
	}
}