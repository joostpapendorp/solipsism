package nl.papendorp.solipsism.grid

import nl.papendorp.solipsism.TestConfiguration
import org.scalacheck.Gen
import org.scalacheck.Prop.forAll

class HexGridSuite
	extends TestConfiguration
		with HexPointGeneration
{

	case class GridCoordinate( grid: HexGrid, coordinate: HexPoint )

	val grids: Gen[ HexGrid ] = for {
		coordinates <- nonEmptyVectorOf( hexPoints )
	} yield HexGrid( coordinates )

	val pointsOnNonEmptyGrids: Gen[ GridCoordinate ] = for {
		grid <- grids
		if grid.coordinates.nonEmpty
		coordinate <- Gen.oneOf( grid.coordinates )
	} yield GridCoordinate( grid, coordinate )

	"A grid of hexagons" when {
		"empty" should {
			"Have radius 0" in {
				HexGrid( Vector() ).radius == 0
			}
		}

		"non-empty" should {
			"consistently address hexes" in {
				check( forAll( pointsOnNonEmptyGrids )( l => l.grid( l.coordinate ) == l.grid( l.coordinate ) ) )
			}

			"contain tiles with coordinates within its radius" in {
				check( forAll( pointsOnNonEmptyGrids )( l => l.grid( l.coordinate ) == Set() ) )
			}

			"encompass its points" in {
				check( forAll( pointsOnNonEmptyGrids )( l => l.grid.radius >= l.coordinate.size ) )
			}
		}
	}
}
