package nl.papendorp.solipsism.grid

import nl.papendorp.solipsism.TestConfiguration
import nl.papendorp.solipsism.grid.Direction._

class DirectionSuite
	extends TestConfiguration
{
	"All directions" should {
		"move along their axes" in {
			Direction.values foreach (direction => direction match {
				case XUp | XDown => directions( direction ).x should be( 0 )
				case YUp | YDown => directions( direction ).y should be( 0 )
				case ZUp | ZDown => directions( direction ).z should be( 0 )
				case _ => fail
			})
		}

		"invert to their counterparts" in {
			-directions( XUp ) should be( directions( XDown ) )
			-directions( YUp ) should be( directions( YDown ) )
			-directions( ZUp ) should be( directions( ZDown ) )
		}

		"form a circle together" in {
			val circle = HexPoint( 0, 1 ).circle
			val actualDirections = Direction.values.to[ IndexedSeq ] map directions

			circle zip actualDirections foreach {
				case (expected, actual) => expected should be( actual )
				case _ => fail
			}
		}

		"have 6 distinct values" in {
			Direction.values.size should be( 6 )
		}

		"have size 1" in {
			Direction.values foreach (directions( _ ).size should be( 1 ))
		}
	}
}
