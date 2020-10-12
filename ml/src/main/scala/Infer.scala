package ml

import domain.Recommendation
import org.apache.spark.ml.recommendation.ALSModel
import org.apache.spark.sql.{DataFrame, SparkSession}

object Infer {

  def main(args: Array[String]): Unit = {
    this.apply()
  }

  def apply(): Unit = {

    implicit val sparkSession: SparkSession = SparkSessionMongo.build(MongoConf.collectionMatchs)

    val model = Modelling.loadModel()

    getRelevantCards(model, profileIds = Seq(1, 2))
      .foreach(println)
  }

  def getRelevantCards(
                        model: ALSModel,
                        profileIds: Seq[Long])
                      (implicit sparkSession: SparkSession): Array[Recommendation] = {
    val recommendations = getRelevantCardsMaybe(model, profileIds)
    if (recommendations.isEmpty) {
      profileIds.map(id => Recommendation(profile = id.toInt)).toArray
    }
    else
      recommendations

  }

  def getRelevantCardsMaybe(
                             model: ALSModel,
                             profileIds: Seq[Long])(implicit sparkSession: SparkSession): Array[Recommendation] = {
    import sparkSession.implicits._
    model
      .recommendForUserSubset(
        getProfileDF(profileIds),
        numItems = Wizard.nRecommendedCards
      )
      .as[Recommendation]
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
