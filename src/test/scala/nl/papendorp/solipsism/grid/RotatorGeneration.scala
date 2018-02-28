package nl.papendorp.solipsism.grid

import nl.papendorp.solipsism.grid.Rotation.{Clockwise, CounterClockwise}
import org.scalacheck.{Arbitrary, Gen}

trait RotatorGeneration
	extends CoordinateGeneration
{
	def rotationGenerator: Gen[ Rotation ] = Gen.oneOf( Clockwise, CounterClockwise )

	implicit def anyRotation: Arbitrary[ Rotation ] = Arbitrary( rotationGenerator )
}
