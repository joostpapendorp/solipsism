package nl.papendorp.solipsism.grid

import nl.papendorp.solipsism.TestConfiguration
import nl.papendorp.solipsism.grid.Direction._

class DirectionSuite
	extends TestConfiguration
{
	"All directions" should {
		val allDirections = Seq( XUp, YUp, ZUp, XDown, YDown, ZDown )

		"move along their axes" in {
			allDirections foreach {
				case direction@(XUp | XDown) => directions( direction ).x should be( 0 )
				case direction@(YUp | YDown) => directions( direction ).y should be( 0 )
				case direction@(ZUp | ZDown) => directions( direction ).z should be( 0 )
				case _ => fail
			}
		}

		"invert to their counterparts" in {
			-directions( XUp ) should be( directions( XDown ) )
			-directions( YUp ) should be( directions( YDown ) )
			-directions( ZUp ) should be( directions( ZDown ) )
		}

		"form a circle together" in {
			val circle = HexPoint( 0, 1 ).circle
			val actualDirections = allDirections map directions

			circle zip actualDirections foreach {
				case (expected, actual) => expected should be( actual )
				case _ => fail
			}
		}

		"have 6 distinct values" in {
			allDirections.toSet.size should be( 6 )
		}

		"have size 1" in {
			allDirections foreach (directions( _ ).size should be( 1 ))
		}
	}
}
