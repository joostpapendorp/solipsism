package nl.papendorp.solipsism.grid

import org.scalacheck.Gen.choose
import org.scalacheck.{Arbitrary, Gen}

trait HexPointGeneration
{
	def slightlyLessLargeNumbers: Gen[ Int ] = choose( Int.MinValue / 6, Int.MaxValue / 6 )

	def hexPoints: Gen[ HexPoint ] = for {
		x <- slightlyLessLargeNumbers
		y <- slightlyLessLargeNumbers
	} yield HexPoint( x, y )

	implicit def arbitraryPoint: Arbitrary[ HexPoint ] = Arbitrary( hexPoints )
}
