package nl.papendorp.solipsism.grid

import nl.papendorp.solipsism.grid.Rotation.Clockwise

import scala.math.abs

object HexPoint
{
	val Origin = HexPoint()

	def apply( x: Int = 0, y: Int = 0 ) = new HexPoint( x, y )

	def unapply( point: HexPoint ): Option[ (Int, Int, Int) ] = Option( point )

	implicit class HexPointScalar( val self: Int ) extends AnyVal
	{
		def *( hex: HexPoint ): HexPoint = hex.*( self )
	}

}

sealed class HexPoint( val x: Int = 0, val y: Int = 0 )
{
	lazy val z: Int = 0 - x - y
	lazy val size: Int = size( x, y, z )

	def +( that: HexPoint ): HexPoint = (this.x + that.x, this.y + that.y)

	def -( that: HexPoint ): HexPoint = (this.x - that.x, this.y - that.y)

	def *( scalar: Int ): HexPoint = (x * scalar, y * scalar)

	def unary_- : HexPoint = (-x, -y)

	// optimized from (this-that).size
	def distanceTo( that: HexPoint ): Int = size( this.x - that.x, this.y - that.y, this.z - that.z )

	def rotate60( rotation: Rotation = Clockwise, steps: Int = 1 ): HexPoint =
	{
		val normalizedSteps = ((steps % 6) + 6) % 6 // last + 6 % 6 accounts for the case of negative steps
		(0 until normalizedSteps).foldLeft( this )( ( c, _ ) => rotation.rotate60( c ) )
	}

	def circle: Stream[ HexPoint ] = this #:: Clockwise.rotate60( this ).circle

	override def toString = s"($x, $y, $z)"

	override def equals( other: scala.Any ): Boolean = other match {
		case that: HexPoint => this.x == that.x && this.y == that.y
		case _ => false
	}

	override def hashCode( ): Int = 31 * x + 17 * y

	// max preferred over sum/2 b/c of integer overflow
	private def size( positions: Int* ) = (positions map abs).max
}
