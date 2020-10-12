package ml.domain

import ml.Wizard

case class Recommendation(
                          profile: Int,
                          recommendations: Seq[(Int, Double)] = Seq((Wizard.defaultCardId, 0.0))
                        ) {
  lazy val isValid: Boolean = recommendations.nonEmpty
  lazy val isDefault: Boolean =  recommendations == Seq((Wizard.defaultCardId, 0.0))
}