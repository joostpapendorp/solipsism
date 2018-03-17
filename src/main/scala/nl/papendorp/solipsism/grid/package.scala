package nl.papendorp.solipsism

import scala.language.implicitConversions

package object grid
{
	implicit def fromTuple( t: (Int, Int) ): HexPoint = HexPoint( x = t._1, y = t._2 )

	implicit def intoTuple( point: HexPoint ): (Int, Int, Int) = (point.x, point.y, point.z)

	implicit def intoSeq( point: HexPoint ): IndexedSeq[ Int ] = IndexedSeq( point.x, point.y, point.z )
}
