package nl.papendorp.solipsism.grid

import nl.papendorp.solipsism.grid.Rotation.{Clockwise, CounterClockwise}
import org.scalacheck.Arbitrary.arbitrary
import org.scalacheck.Gen.choose
import org.scalacheck.{Arbitrary, Gen}

trait RotatorGeneration
	extends HexPointGeneration
{

	case class Turn( point: HexPoint, rotation: Rotation, steps: Int )
	{
		def *( interval: Int ): Turn = Turn( point, rotation, steps * interval )
	}

	def rotationGenerator: Gen[ Rotation ] = Gen.oneOf( Clockwise, CounterClockwise )

	implicit def anyRotation: Arbitrary[ Rotation ] = Arbitrary( rotationGenerator )

	val fullCircles: Gen[ Turn ] = turns( slightlyLessLargeNumbers ) map (_ * 6)

	val unboundedTurns: Gen[ Turn ] = turns( arbitrary[ Int ] )

	val partialCircles: Gen[ Turn ] = turns( choose( 0, 6 ) )

	private def turns( stepsGenerator: Gen[ Int ] ) = for {
		coordinate <- hexPoints
		rotation <- rotationGenerator
		steps <- stepsGenerator
	} yield Turn( coordinate, rotation, steps )
}
