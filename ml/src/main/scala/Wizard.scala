package ml

import com.typesafe.config.{Config, ConfigFactory}

object Wizard {

  val config: Config = ConfigFactory.load("wizard.conf")

  val colProfile = "profile"
  val colCard = "card"
  val colRating = "rating"

  val nRecommendedCards = 3
  val nCards = 78
  val defaultCardId = 0

  val modelPath: String = config.getString("model.path")
  val modelMaxIter: Int = config.getInt("model.maxIter")
  val modelRegParam: Double = config.getDouble("model.regParam")
}
