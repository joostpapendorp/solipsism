package nl.papendorp.solipsism.grid

sealed trait Rotation
{
	def reverse: Rotation

	def rotate60( coordinate: HexCoordinate ): HexCoordinate
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