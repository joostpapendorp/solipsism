package nl.papendorp.solipsism

import nl.papendorp.solipsism.grid.HexPoint

import scala.language.implicitConversions

package object view
{
	implicit def tupleAsVector( tuple: (Double, Double) ): Vector2D = Vector2D( tuple._1, tuple._2 )

	implicit def vectorAsTuple( vector: Vector2D ): (Double, Double) = (vector.x, vector.y)

	implicit def hexAsVector( hex: HexPoint ): Vector2D = Vector2D( hex.x, hex.y )
}
