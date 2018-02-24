package nl.papendorp.solipsism.grid

import nl.papendorp.solipsism.grid.Rotation.Clockwise

import scala.math.abs

object HexCoordinate
{
	val Origin = HexCoordinate()

	def apply( x: Int = 0, y: Int = 0 ) = new HexCoordinate( x, y )

	def unapply( coordinate: HexCoordinate ): Option[ (Int, Int) ] = Option( coordinate ) map (c => (c.x, c.y))
}

sealed case class HexCoordinate( x: Int = 0, y: Int = 0 )
{
	lazy val z: Int = 0 - x - y
	lazy val size: Int = size( x, y, z )

	def +( that: HexCoordinate ): HexCoordinate = (this.x + that.x, this.y + that.y)

	def -( that: HexCoordinate ): HexCoordinate = (this.x - that.x, this.y - that.y)

	def *( scalar: Int ): HexCoordinate = (x * scalar, y * scalar)

	def unary_- : HexCoordinate = (-x, -y)

	// optimized from (this-that).size
	def distanceTo( that: HexCoordinate ): Int = size( this.x - that.x, this.y - that.y, this.z - that.z )

	def rotate60( rotation: Rotation = Clockwise, steps: Int = 1 ): HexCoordinate =
	{
		val normalizedSteps = ((steps % 6) + 6) % 6 // last + 6 % 6 accounts for the case of negative steps
		(0 until normalizedSteps).foldLeft( this )( ( c, _ ) => rotation.rotate60( c ) )
	}

	def circle: Stream[ HexCoordinate ] = this #:: Clockwise.rotate60( this ).circle

	override def toString = s"($x, $y, $z)"

	// max preferred over sum/2 b/c of integer overflow
	private def size( positions: Int* ) = (positions map abs).max
}


object Rotation
{

	case object Clockwise extends Rotation
	{
		override lazy val reverse: Rotation = CounterClockwise

		override def rotate60( coordinate: HexCoordinate ) = (-coordinate.z, -coordinate.x)
	}

	case object CounterClockwise extends Rotation
	{
		override lazy val reverse: Rotation = Clockwise

		override def rotate60( coordinate: HexCoordinate ) = (-coordinate.y, -coordinate.z)
	}

}

sealed trait Rotation
{
	def reverse: Rotation

	def rotate60( coordinate: HexCoordinate ): HexCoordinate
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

	lazy val directions: Map[ Direction, HexCoordinate ] = {
		val names = Seq( XUp, YUp, ZUp, XDown, YDown, ZDown )
		(names zip (0, 1).circle).toMap
	}
}

sealed trait Direction
{
}
