package ml.domain

case class Recommendation(
                          profile: Int,
                          recommendations: Seq[(Int, Double)] = Seq((0, 0.0))
                        ) {
  lazy val isValid: Boolean = recommendations.nonEmpty
  lazy val isDefault: Boolean =  recommendations == Seq((0, 0.0))
}