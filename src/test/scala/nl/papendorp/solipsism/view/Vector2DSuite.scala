package nl.papendorp.solipsism.view

import nl.papendorp.solipsism.TestConfiguration
import nl.papendorp.solipsism.view.Vector2D.Origin
import org.scalacheck.Arbitrary.arbitrary
import org.scalacheck.Prop.forAll
import org.scalacheck.{Arbitrary, Gen}
import org.scalactic.TolerantNumerics

class Vector2DSuite extends TestConfiguration
{
	def pointGenerator: Gen[ Vector2D ] = for {
		x <- arbitrary[ Int ]
		y <- arbitrary[ Int ]
	} yield Vector2D( x, y )

	implicit val anyVector: Arbitrary[ Vector2D ] = Arbitrary( pointGenerator )
	implicit val doubleEquality = TolerantNumerics.tolerantDoubleEquality( 0.01 )


	"A Vector2D" should {
		"default to origin" in {
			Vector2D() == Origin
		}

		"convert from tuple" in {
			check( forAll( ( x: Double, y: Double ) => {
				val v: Vector2D = (x, y)
				v.x == x && v.y == y
			} ) )
		}

		"convert to tuple" in {
			check( forAll( ( v: Vector2D ) => {
				val (x, y): (Double, Double) = v
				v.x == x && v.y == y
			} ) )
		}

		"map properly" in {
			check( forAll{ ( v: Vector2D ) => {
				val mapped = for( x <- v ) yield x * 3
				mapped == List( v.x * 3, v.y * 3 )
			}
			} )
		}
	}

	"Adding two vectors" should {
		"be commutative" in {
			check( forAll( ( left: Vector2D, right: Vector2D ) => {
				left + right == right + left
			} ) )
		}

		"be associative" in {
			check( forAll( ( a: Vector2D, b: Vector2D, c: Vector2D ) => {
				(a + b) + c == a + (b + c)
			} ) )
		}

		"have a zero element" in {
			check( forAll( ( v: Vector2D ) => {
				v + Vector2D( 0.0, 0.0 ) == v
			} ) )
		}

		"be distributive" in {
			check( forAll( ( left: Vector2D, right: Vector2D ) => {
				left + right === Vector2D( left.x + right.x, left.y + right.y )
			} ) )
		}
	}

	"Multiplying a vector by a scalar" should {
		"be commutative" in {
			check( forAll( ( scalar: Int, vector: Vector2D ) => {
				scalar * vector == vector * scalar
			} ) )
		}

		"be distributive" in {
			check( forAll( ( scalar: Int, vector: Vector2D ) => {
				scalar * vector == Vector2D( scalar * vector.x, scalar * vector.y )
			} ) )
		}

		"have a zero element" in {
			check( forAll( ( vector: Vector2D ) => {
				1 * vector == vector
			} ) )
		}
	}

	"Substracting a vector from a vector" should {
		"be distributive" in {
			check( forAll( ( left: Vector2D, right: Vector2D ) => {
				left - right == Vector2D( left.x - right.x, left.y - right.y )
			} ) )
		}

		"be anti-commutative" in {
			check( forAll( ( left: Vector2D, right: Vector2D ) => {
				left - right == -(right - left)
			} ) )
		}

		"invert adding" in {
			check( forAll( ( left: Vector2D, right: Vector2D ) => left + right - right === left ) )
		}

		"have a zero element" in {
			check( forAll( ( vector: Vector2D ) => {
				vector - (0.0, 0.0) == vector
			} ) )
		}
	}
}