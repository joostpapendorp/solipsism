package nl.papendorp.solipsism

import org.scalacheck.Gen
import org.scalatest.prop.Checkers
import org.scalatest.{Matchers, WordSpec}

trait TestConfiguration
	extends WordSpec
		with Matchers
		with Checkers
{
	def nonEmptyVectorOf[ BUILDABLE ]( g: Gen[ BUILDABLE ] ) = Gen.nonEmptyBuildableOf[ Vector[ BUILDABLE ], BUILDABLE ]( g )
}
