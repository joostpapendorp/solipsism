package nl.papendorp.solipsism.grid


import nl.papendorp.solipsism.grid.Direction._
import nl.papendorp.solipsism.grid.HexCoordinate.Origin
import nl.papendorp.solipsism.grid.Rotation.{Clockwise, CounterClockwise}
import org.scalacheck.Arbitrary._
import org.scalacheck.Gen.choose
import org.scalacheck.Prop.forAll
import org.scalacheck.{Arbitrary, Gen}
import org.scalatest.prop.Checkers
import org.scalatest.{Matchers, WordSpec}

import scala.math.abs

class HexCoordinateTest
	extends WordSpec
		with Matchers
		with Checkers
{
	def slightlyLessLargeNumbers: Gen[ Int ] = choose( Int.MinValue / 6, Int.MaxValue / 6 )

	def coordinateGenerator: Gen[ HexCoordinate ] = for {
		x <- slightlyLessLargeNumbers
		y <- slightlyLessLargeNumbers
	} yield HexCoordinate( x, y )

	def rotationGenerator: Gen[ Rotation ] = Gen.oneOf( Clockwise, CounterClockwise )

	implicit def anyRotation: Arbitrary[ Rotation ] = Arbitrary( rotationGenerator )

	implicit def arbitraryCoordinate: Arbitrary[ HexCoordinate ] = Arbitrary( coordinateGenerator )

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
				check( forAll{ retainsCoordiates } )
			}

			"normalize z-position" in {
				val normalizesZ = ( x: Int, y: Int ) => {
					val coordinate = HexCoordinate( x = x, y = y )
					coordinate.x + coordinate.y + coordinate.z === 0
				}
				check( forAll{ normalizesZ } )
			}

			"print its positions" in {
				(HexCoordinate( -1, 1 ) toString) should be( "(-1, 1, 0)" )
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
			check( forAll( ( coordinate: HexCoordinate ) => coordinate + Origin === coordinate ) )
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
			check( forAll{ hasPositiveSize } )
		}

		"be at minimum the size of the largest position" in {
			val matchesPosition: (HexCoordinate => Int) => (HexCoordinate) => Boolean = ( position ) => ( coordinate ) =>
				coordinate.size >= abs( position( coordinate ) )

			check( forAll( matchesPosition( _.x ) ) && forAll( matchesPosition( _.y ) ) && forAll( matchesPosition( _.z ) ) )
		}

		"be equivalent to half the sum of position sizes" in {
			check( forAll( ( coordinate: HexCoordinate ) => {
				val halfSumOfPositionSizes = (List( coordinate.x, coordinate.y, coordinate.z ) map abs sum) / 2
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
				case (coordinate, rotation, steps) => coordinate.rotate60( rotation, steps ).rotate60( rotation.reverse, steps ) === coordinate
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
				case (coordinate, rotation, steps) => coordinate.rotate60( rotation, steps ) === coordinate.rotate60( rotation.reverse, -steps )
			}

			check( forAll( rotations )( mirrorsOppositeDirection ) )
		}
	}

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
			val circle = HexCoordinate( 0, 1 ).circle
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
