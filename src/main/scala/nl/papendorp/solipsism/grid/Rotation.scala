package nl.papendorp.solipsism.grid

sealed trait Rotation
{
	def reverse: Rotation

	def rotate60( point: HexPoint ): HexPoint
}

object Rotation
{

	case object Clockwise extends Rotation
	{
		override lazy val reverse: Rotation = CounterClockwise

		override def rotate60( point: HexPoint ) = (-point.z, -point.x)
	}

	case object CounterClockwise extends Rotation
	{
		override lazy val reverse: Rotation = Clockwise

		override def rotate60( point: HexPoint ) = (-point.y, -point.z)
	}

}