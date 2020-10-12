package ml

import ml.domain.{Card, Match, Profile}
import org.apache.spark.sql.SparkSession


object Train {

  def main(args: Array[String]): Unit = {
    this.apply()
  }

  def apply(): Unit = {

    implicit val sparkSession: SparkSession = SparkSessionMongo.build(MongoConf.collectionMatchs)

    import sparkSession.implicits._

    val matches = DataProcessing
      .readCollection(MongoConf.collectionMatchs)
      .as[Match]

    val cards = DataProcessing
      .readCollection(MongoConf.collectionCards)
      .as[Card]

    val profiles = DataProcessing
      .readCollection(MongoConf.collectionProfiles)
      .as[Profile]

    val model = Modelling.buildModel().fit(matches)

    Modelling.saveModel(model)
  }
}