package nl.papendorp.solipsism.view

object Vector2D
{
	val Origin = Vector2D()

	implicit class Vector2DScalar( val self: Double ) extends AnyVal
	{
		def *( vector: Vector2D ): Vector2D = vector.*( self )
	}

}

case class Vector2D( x: Double = 0.0, y: Double = 0.0 )
{
	def *( scalar: Double ): Vector2D = Vector2D( x * scalar, y * scalar )

	def +( that: Vector2D ): Vector2D = Vector2D( this.x + that.x, this.y + that.y )

	def -( that: Vector2D ): Vector2D = Vector2D( this.x - that.x, this.y - that.y )

	def unary_- : Vector2D = Vector2D( -x, -y )

	def map[ RESULT ]( f: Double => RESULT ): Seq[ RESULT ] = Seq( f( x ), f( y ) )
}
