package ml

import org.apache.spark.sql.SparkSession
import org.scalatest.flatspec._
import org.scalatest.matchers._


class ModellingTest extends AnyFlatSpec with should.Matchers {

  implicit val sparkSession: SparkSession = SparkSessionMongo.build(MongoConf.collectionMatches)

  behavior of this.getClass.getSimpleName

  "trained model" should "infer valid recommendations" in {

    Train()

    val model = Modelling.loadModel(Wizard.modelPath)

    val profileId = 1
    val recommendation = Infer
      .getRelevantCards(model, profileIds = Seq(profileId))
      .head

    val nCards = 78

    recommendation.profile should be(profileId)
    recommendation.isValid should be(true)
    recommendation.recommendations.head._1 <= nCards should be(true)
  }
}
