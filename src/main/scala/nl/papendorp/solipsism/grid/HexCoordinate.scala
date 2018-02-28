package nl.papendorp.solipsism.grid

import nl.papendorp.solipsism.grid.Rotation.Clockwise

import scala.math.abs

object HexCoordinate
{
	val Origin = HexCoordinate()

	def apply( x: Int = 0, y: Int = 0 ) = new HexCoordinate( x, y )

	def unapply( coordinate: HexCoordinate ): Option[ (Int, Int, Int) ] = Option( coordinate )

	implicit def *( scalar: Int, hex: HexCoordinate ): HexCoordinate = hex.*( scalar )
}

sealed class HexCoordinate( val x: Int = 0, val y: Int = 0 )
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

	override def equals( other: scala.Any ): Boolean = other match {
		case that: HexCoordinate => this.x == that.x && this.y == that.y
		case _ => false
	}

	override def hashCode( ): Int = 31 * x + 17 * y

	// max preferred over sum/2 b/c of integer overflow
	private def size( positions: Int* ) = (positions map abs).max
}
