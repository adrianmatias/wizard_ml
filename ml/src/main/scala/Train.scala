package ml

import domain.{Card, Match, Profile}
import org.apache.spark.sql.SparkSession


object Train {

  def main(args: Array[String]): Unit = {
    this.apply()
  }

  def apply(): Unit = {

    implicit val sparkSession: SparkSession = SparkSessionMongo.build(MongoConf.collectionMatches)

    import sparkSession.implicits._

    val matches = DataProcessing
      .readCollection(MongoConf.collectionMatches)
      .as[Match]

    val cards = DataProcessing
      .readCollection(MongoConf.collectionCards)
      .as[Card]

    val profiles = DataProcessing
      .readCollection(MongoConf.collectionProfiles)
      .as[Profile]

    matches.show()
    cards.show()
    profiles.show()

    val model = Modelling.buildModel().fit(matches)

    model.itemFactors.show()
    model.userFactors.show()
    model
      .recommendForAllUsers(Wizard.nRecommendedCards)
      .show()

    Modelling.saveModel(model, path = Wizard.modelPath)
  }
}