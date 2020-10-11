package ml

import domain.Recomendation
import org.apache.spark.ml.recommendation.ALSModel
import org.apache.spark.sql.{DataFrame, SparkSession}

object Infer {

  def main(args: Array[String]): Unit = {
    this.apply()
  }

  def apply(): Unit = {

    implicit val sparkSession: SparkSession = SparkSessionMongo.build(MongoConf.collectionMatchs)

    val model = Modelling.loadModel(Wizard.modelPath)

    getRelevantCards(model, profileIds = Seq(1, 2))
      .foreach(println)
  }

  def getRelevantCards(
                        model: ALSModel,
                        profileIds: Seq[Long])
                      (implicit sparkSession: SparkSession): Array[Recomendation] = {
    import sparkSession.implicits._
    model
      .recommendForUserSubset(
        getProfileDF(profileIds),
        numItems = Wizard.nRecommendedCards
      )
      .as[Recomendation]
      .collect()
  }

  def getProfileDF(profileIds: Seq[Long])
                  (implicit sparkSession: SparkSession): DataFrame = {
    import sparkSession.implicits._
    sparkSession
      .sparkContext
      .parallelize(profileIds)
      .toDF(Wizard.colProfile)
  }
}
