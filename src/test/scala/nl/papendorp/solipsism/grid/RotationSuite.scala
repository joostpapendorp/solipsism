package nl.papendorp.solipsism.grid

import nl.papendorp.solipsism.TestConfiguration
import org.scalacheck.Gen.choose
import org.scalacheck.Prop.forAll
import org.scalacheck.{Arbitrary, Gen}

class RotationSuite
	extends TestConfiguration
		with RotatorGeneration
{

	"Rotating" should {
		"invert coordinate after three steps" in {
			def threeRotationsInvertsCoordinate( coordinate: HexCoordinate, rotation: Rotation ): Boolean =
				coordinate.rotate60( rotation, 3 ) === -coordinate

			check( forAll( threeRotationsInvertsCoordinate _ ) )
		}

		"wrap around in six steps" in {
			val fullCircleRotations: Gen[ (HexCoordinate, Rotation, Int) ] = for {
				coordinate <- coordinateGenerator
				rotation <- rotationGenerator
				steps <- slightlyLessLargeNumbers
			} yield (coordinate, rotation, steps * 6)

			def endWhereStarted( fullCircleRotation: (HexCoordinate, Rotation, Int) ): Boolean = fullCircleRotation match {
				case (coordinate, rotation, steps) => coordinate.rotate60( rotation, steps ) === coordinate
			}

			check( forAll( fullCircleRotations )( endWhereStarted ) )
		}

		"reverse to same location" in {
			val rotations: Gen[ (HexCoordinate, Rotation, Int) ] = for {
				coordinate <- coordinateGenerator
				rotation <- rotationGenerator
				circles <- Arbitrary.arbInt.arbitrary
			} yield (coordinate, rotation, circles)

			def reversesToStart( rotations: (HexCoordinate, Rotation, Int) ): Boolean = rotations match {
				case (coordinate, rotation, steps) => {
					val forwards = coordinate.rotate60( rotation, steps )
					val reverse = forwards.rotate60( rotation.reverse, steps )
					reverse === coordinate
				}
			}

			check( forAll( rotations )( reversesToStart ) )
		}

		"mirror its reverse rotation" in {
			val rotations: Gen[ (HexCoordinate, Rotation, Int) ] = for {
				coordinate <- coordinateGenerator
				rotation <- rotationGenerator
				steps <- choose( 0, 6 )
			} yield (coordinate, rotation, steps)

			def mirrorsOppositeDirection( rotations: (HexCoordinate, Rotation, Int) ): Boolean = rotations match {
				case (coordinate, rotation, steps) => {
					val positiveClockwise = coordinate.rotate60( rotation, steps )
					val negativeCounterClockwise = coordinate.rotate60( rotation.reverse, -steps )
					positiveClockwise === negativeCounterClockwise
				}
			}

			check( forAll( rotations )( mirrorsOppositeDirection ) )
		}
	}
}
