package nl.papendorp.solipsism.grid


import nl.papendorp.solipsism.TestConfiguration
import nl.papendorp.solipsism.grid.HexCoordinate.Origin
import org.scalacheck.Arbitrary._
import org.scalacheck.Prop.forAll

import scala.math.abs

class HexCoordinateSuite
	extends TestConfiguration
		with CoordinateGeneration
{
	"Origin" should {
		"have size 0" in {
			assert( Origin.size === 0 )
		}

		"have all positions 0" in {
			assert( Origin.x === 0 )
			assert( Origin.y === 0 )
			assert( Origin.z === 0 )
		}
	}

	"A coordinate" when {
		"created" should {
			"default to origin" in {
				assert( HexCoordinate() === Origin )
			}

			"retain given coordinates" in {
				val retainsCoordiates = ( x: Int, y: Int ) => {
					val coordinate = HexCoordinate( x = x, y = y )
					coordinate.x === x && coordinate.y === y
				}
				check( forAll( retainsCoordiates ) )
			}

			"normalize z-position" in {
				val normalizesZ = ( x: Int, y: Int ) => {
					val coordinate = HexCoordinate( x = x, y = y )
					coordinate.x + coordinate.y + coordinate.z === 0
				}
				check( forAll( normalizesZ ) )
			}

			"print its positions" in {
				HexCoordinate( -1, 1 ).toString should be( "(-1, 1, 0)" )
			}
		}

		"inverted" should {
			"have the same size" in {
				check( forAll( ( coordinate: HexCoordinate ) => coordinate.size === (-coordinate).size ) )
			}
			"be opposite" in {
				check( forAll( ( coordinate: HexCoordinate ) => coordinate + (-coordinate) === Origin ) )
			}
		}
	}

	"Adding two coordinates" should {
		"sum individual positions" in {
			def sumsPosition( position: HexCoordinate => Int )( augend: HexCoordinate, addend: HexCoordinate ): Boolean =
				position( augend + addend ) === (position( augend ) + position( addend ))

			check( forAll( sumsPosition( _.x ) _ ) && forAll( sumsPosition( _.y ) _ ) && forAll( sumsPosition( _.z ) _ ) )
		}

		"be transitive" in {
			check( forAll( ( a: HexCoordinate, b: HexCoordinate, c: HexCoordinate ) => (a + b) + c === a + (b + c) ) )
		}

		"be commutative" in {
			check( forAll( ( left: HexCoordinate, right: HexCoordinate ) => left + right === right + left ) )
		}

		"remain identical under zero" in {
			check( forAll( ( coordinate: HexCoordinate ) => coordinate + Origin === coordinate && Origin + coordinate == coordinate ) )
		}
	}

	"Substracting two coordinates" should {
		"substracts individual positions" in {
			def substractsPosition( position: HexCoordinate => Int )( minuend: HexCoordinate, substrahend: HexCoordinate ): Boolean =
				position( minuend - substrahend ) === (position( minuend ) - position( substrahend ))

			check( forAll( substractsPosition( _.x ) _ ) && forAll( substractsPosition( _.y ) _ ) && forAll( substractsPosition( _.z ) _ ) )
		}

		"be anti-commutative" in {
			check( forAll( ( left: HexCoordinate, right: HexCoordinate ) => left - right === -(right - left) ) )
		}

		"invert adding" in {
			check( forAll( ( left: HexCoordinate, right: HexCoordinate ) => left + right - right === left ) )
		}

		"remain identical under zero" in {
			check( forAll( ( coordinate: HexCoordinate ) => coordinate - Origin === coordinate ) )
		}
	}

	"The size of a coordinate" should {
		"be positive" in {
			val hasPositiveSize = ( x: Int, y: Int ) => HexCoordinate( x = x, y = y ).size >= 0
			check( forAll( hasPositiveSize ) )
		}

		"be at minimum the size of the largest position" in {
			val matchesPosition: (HexCoordinate => Int) => (HexCoordinate) => Boolean = ( position ) => ( coordinate ) =>
				coordinate.size >= abs( position( coordinate ) )

			check( forAll( matchesPosition( _.x ) ) && forAll( matchesPosition( _.y ) ) && forAll( matchesPosition( _.z ) ) )
		}

		"be equivalent to half the sum of position sizes" in {
			check( forAll( ( coordinate: HexCoordinate ) => {
				val halfSumOfPositionSizes = (List( coordinate.x, coordinate.y, coordinate.z ) map abs).sum / 2
				halfSumOfPositionSizes === coordinate.size
				//but sum/2 overflows for n > MAX_INT/4, while implementation doesn't
			} ) )
		}
	}

	"The distance between two coordinates" should {
		"equal the size of the difference" in {
			check( forAll( ( left: HexCoordinate, right: HexCoordinate ) => left.distanceTo( right ) === (left - right).size ) )
		}

		"be commutative" in {
			check( forAll( ( left: HexCoordinate, right: HexCoordinate ) => left.distanceTo( right ) === right.distanceTo( left ) ) )
		}
	}
}
