package domain

case class Recomendation(
                          profile: Int,
                          recommendations: Seq[(Int, Double)]
                        ) {
  lazy val isValid: Boolean = recommendations.nonEmpty
}
