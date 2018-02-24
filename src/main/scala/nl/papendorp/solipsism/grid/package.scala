package nl.papendorp.solipsism

import scala.language.implicitConversions

package object grid
{
	implicit def fromTuple( t: (Int, Int) ): HexCoordinate = HexCoordinate( x = t._1, y = t._2 )

	implicit def intoTuple( coordinate: HexCoordinate ): (Int, Int, Int) = (coordinate.x, coordinate.y, coordinate.z)
}
