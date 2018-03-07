package nl.papendorp.solipsism.grid

object HexGrid
{
	def apply( coordinates: Seq[ HexPoint ] ): HexGrid = new HexGrid( coordinates )
}

class HexGrid( val coordinates: Seq[ HexPoint ] )
{
	lazy val radius: Int = coordinates map (_.size) match {
		case Nil => 0
		case cs => cs.max
	}

	def apply( point: HexPoint ): Seq[ Any ] = Seq()
}
