package nl.papendorp.solipsism.grid

import org.scalacheck.Gen.choose
import org.scalacheck.{Arbitrary, Gen}

trait CoordinateGeneration
{
	def slightlyLessLargeNumbers: Gen[ Int ] = choose( Int.MinValue / 6, Int.MaxValue / 6 )

	def coordinateGenerator: Gen[ HexCoordinate ] = for {
		x <- slightlyLessLargeNumbers
		y <- slightlyLessLargeNumbers
	} yield HexCoordinate( x, y )

	implicit def arbitraryCoordinate: Arbitrary[ HexCoordinate ] = Arbitrary( coordinateGenerator )
}
