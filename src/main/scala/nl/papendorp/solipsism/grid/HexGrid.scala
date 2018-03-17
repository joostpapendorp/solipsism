package nl.papendorp.solipsism.grid

object HexGrid
{
	def apply( coordinates: IndexedSeq[ HexPoint ] ): HexGrid = new HexGrid( coordinates )
}

class HexGrid( val coordinates: IndexedSeq[ HexPoint ] )
{
	lazy val radius: Int = coordinates map (_.size) match {
		case IndexedSeq() => 0
		case cs => cs.max
	}

	def apply( point: HexPoint ): Set[ Any ] = Set()
}
