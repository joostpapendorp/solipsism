package nl.papendorp.solipsism.grid

import nl.papendorp.solipsism.TestConfiguration
import org.scalacheck.Prop.forAll

class RotationSuite
	extends TestConfiguration
		with RotatorGeneration
{

	"Rotating" should {
		"invert coordinate after three steps" in {
			def threeRotationsInvertsCoordinate( coordinate: HexPoint, rotation: Rotation ): Boolean =
				coordinate.rotate60( rotation, 3 ) == -coordinate

			check( forAll( threeRotationsInvertsCoordinate _ ) )
		}

		"wrap around in six steps" in {
			def endWhereStarted( turn: Turn ): Boolean = turn.point.rotate60( turn.rotation, turn.steps ) == turn.point

			check( forAll( fullCircles )( endWhereStarted ) )
		}

		"reverse to same location" in {
			def reversesToStart( turn: Turn ): Boolean =
			{
				val forwards = turn.point.rotate60( turn.rotation, turn.steps )
				val reverse = forwards.rotate60( turn.rotation.reverse, turn.steps )
				reverse == turn.point
			}

			check( forAll( unboundedTurns )( reversesToStart ) )
		}

		"mirror its reverse rotation" in {
			def mirrorsOppositeDirection( turn: Turn ): Boolean =
			{
				val positiveClockwise = turn.point.rotate60( turn.rotation, turn.steps )
				val negativeCounterClockwise = turn.point.rotate60( turn.rotation.reverse, -turn.steps )
				positiveClockwise == negativeCounterClockwise
			}

			check( forAll( partialCircles )( mirrorsOppositeDirection ) )
		}
	}
}
