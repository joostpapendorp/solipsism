package nl.papendorp.solipsism.grid


import nl.papendorp.solipsism.TestConfiguration
import nl.papendorp.solipsism.grid.HexPoint._
import org.scalacheck.Arbitrary._
import org.scalacheck.Prop.forAll

import scala.math.abs

class HexPointSuite
	extends TestConfiguration
		with HexPointGeneration
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

	"A point" when {
		"created" should {
			"default to origin" in {
				assert( HexPoint() === Origin )
			}

			"retain given points" in {
				val retainsCoordiates = ( x: Int, y: Int ) => {
					val point = HexPoint( x = x, y = y )
					point.x === x && point.y === y
				}
				check( forAll( retainsCoordiates ) )
			}

			"normalize z-position" in {
				val normalizesZ = ( x: Int, y: Int ) => {
					val point = HexPoint( x = x, y = y )
					point.x + point.y + point.z === 0
				}
				check( forAll( normalizesZ ) )
			}

			"print its positions" in {
				HexPoint( -1, 1 ).toString should be( "(-1, 1, 0)" )
			}
		}

		"inverted" should {
			"have the same size" in {
				check( forAll( ( point: HexPoint ) => point.size === (-point).size ) )
			}
			"be opposite" in {
				check( forAll( ( point: HexPoint ) => point + (-point) === Origin ) )
			}
		}
	}

	"Two points with same x and y" should {
		def pointsWithSamePositions( predicate: (HexPoint, HexPoint) => Boolean )( x: Int, y: Int ) =
			predicate( HexPoint( x, y ), HexPoint( x, y ) )

		"be equal" in {
			check( forAll( pointsWithSamePositions( _ == _ ) _ ) )
		}

		"have same hash" in {
			check( forAll( pointsWithSamePositions( _.hashCode == _.hashCode ) _ ) )
		}

		"have same z" in {
			check( forAll( pointsWithSamePositions( _.z == _.z ) _ ) )
		}
	}

	"Adding two points" should {
		"sum individual positions" in {
			def sumsPosition( position: HexPoint => Int )( augend: HexPoint, addend: HexPoint ): Boolean =
				position( augend + addend ) === (position( augend ) + position( addend ))

			check( forAll( sumsPosition( _.x ) _ ) && forAll( sumsPosition( _.y ) _ ) && forAll( sumsPosition( _.z ) _ ) )
		}

		"be transitive" in {
			check( forAll( ( a: HexPoint, b: HexPoint, c: HexPoint ) => (a + b) + c === a + (b + c) ) )
		}

		"be commutative" in {
			check( forAll( ( left: HexPoint, right: HexPoint ) => left + right === right + left ) )
		}

		"remain identical under zero" in {
			check( forAll( ( point: HexPoint ) => point + Origin === point && Origin + point == point ) )
		}
	}

	"Substracting two points" should {
		"substracts individual positions" in {
			def substractsPosition( position: HexPoint => Int )( minuend: HexPoint, substrahend: HexPoint ): Boolean =
				position( minuend - substrahend ) === (position( minuend ) - position( substrahend ))

			check( forAll( substractsPosition( _.x ) _ ) && forAll( substractsPosition( _.y ) _ ) && forAll( substractsPosition( _.z ) _ ) )
		}

		"be anti-commutative" in {
			check( forAll( ( left: HexPoint, right: HexPoint ) => left - right === -(right - left) ) )
		}

		"invert adding" in {
			check( forAll( ( left: HexPoint, right: HexPoint ) => left + right - right === left ) )
		}

		"remain identical under zero" in {
			check( forAll( ( point: HexPoint ) => point - Origin === point ) )
		}
	}

	"The size of a point" should {
		"be positive" in {
			val hasPositiveSize = ( x: Int, y: Int ) => HexPoint( x = x, y = y ).size >= 0
			check( forAll( hasPositiveSize ) )
		}

		"be at minimum the size of the largest position" in {
			val matchesPosition: (HexPoint => Int) => (HexPoint) => Boolean = ( position ) => ( point ) =>
				point.size >= abs( position( point ) )

			check( forAll( matchesPosition( _.x ) ) && forAll( matchesPosition( _.y ) ) && forAll( matchesPosition( _.z ) ) )
		}

		"be equivalent to half the sum of position sizes" in {
			check( forAll( ( point: HexPoint ) => {
				val halfSumOfPositionSizes = (List( point.x, point.y, point.z ) map abs).sum / 2
				halfSumOfPositionSizes === point.size
				//but sum/2 overflows for |n| > MAX_INT/4, while implementation doesn't
			} ) )
		}
	}

	"The distance between two points" should {
		"equal the size of the difference" in {
			check( forAll( ( left: HexPoint, right: HexPoint ) => left.distanceTo( right ) === (left - right).size ) )
		}

		"be commutative" in {
			check( forAll( ( left: HexPoint, right: HexPoint ) => left.distanceTo( right ) === right.distanceTo( left ) ) )
		}
	}

	"Multiplying a point by a scalar" should {
		"be commutative" in {
			check( forAll( ( point: HexPoint, scalar: Int ) => (point * scalar) == (scalar * point) ) )
		}

		"distribute over its positions" in {
			def multipliesPosition( position: HexPoint => Int )( point: HexPoint, scalar: Int ): Boolean =
				position( point * scalar ) === (position( point ) * scalar)

			check( forAll( multipliesPosition( _.x ) _ ) && forAll( multipliesPosition( _.y ) _ ) && forAll( multipliesPosition( _.z ) _ ) )
		}
	}
}
