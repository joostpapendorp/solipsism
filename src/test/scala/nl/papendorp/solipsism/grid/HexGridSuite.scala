package nl.papendorp.solipsism.grid

import nl.papendorp.solipsism.TestConfiguration
import org.scalacheck.Gen
import org.scalacheck.Prop.forAll

class HexGridSuite
	extends TestConfiguration
		with HexPointGeneration
{
	val grids: Gen[ HexGrid ] = for {
		coordinates <- Gen.listOf( hexPoints )
	} yield HexGrid( coordinates )

	val pointsOnNonEmptyGrids: Gen[ (HexGrid, HexPoint) ] = for {
		grid <- grids
		if grid.coordinates.nonEmpty
		coordinate <- Gen.oneOf( grid.coordinates )
	} yield (grid, coordinate)

	"A grid of hexagons" when {
		"empty" should {
			"Have radius 0" in {
				HexGrid( Nil ).radius === 0
			}
		}

		"non-empty" should {
			"consistently address hexes" in {
				def yieldSameHexForSameCoordinate( coordinateWithinGrid: (HexGrid, HexPoint) ): Boolean = coordinateWithinGrid match {
					case (grid: HexGrid, coordinate: HexPoint) => grid( coordinate ) == grid( coordinate )
					case _ => false
				}

				check( forAll( pointsOnNonEmptyGrids )( yieldSameHexForSameCoordinate ) )
			}

			"contain tiles with coordinates within its radius" in {
				def gridContainsTileWithCoordinate( location: (HexGrid, HexPoint) ): Boolean = location match {
					case (grid, coordinate) => grid( coordinate ) == Seq()
					case _ => false
				}

				check( forAll( pointsOnNonEmptyGrids )( gridContainsTileWithCoordinate ) )
			}

			"encompass its points" in {
				def pointSizeFallsWithinGridRadius( location: (HexGrid, HexPoint) ): Boolean = location match {
					case (grid, coordinate) => grid.radius >= coordinate.size
					case _ => false
				}

				check( forAll( pointsOnNonEmptyGrids )( pointSizeFallsWithinGridRadius ) )
			}
		}
	}
}
