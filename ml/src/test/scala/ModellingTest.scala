package ml

import org.apache.spark.ml.recommendation.ALSModel
import org.apache.spark.sql.SparkSession
import org.scalatest.flatspec._
import org.scalatest.matchers._


class ModellingTest extends AnyFlatSpec with should.Matchers {

  implicit val sparkSession: SparkSession = SparkSessionMongo.build(MongoConf.collectionMatchs)
  val profileIdNew: Int = Int.MaxValue

  behavior of this.getClass.getSimpleName

  Train()
  val inferMLeap = new InferenceMLeap(pathFilename = Modelling.pathFilenameMLeap)
  val model: ALSModel = Modelling.loadModel()

  "trained model" should "infer valid recommendation" in {

    val profileId = 1
    val recommendation = Infer
      .getRelevantCards(model, profileIds = Seq(profileId))
      .head

    val nCards = 78

    recommendation.profile should be(profileId)
    recommendation.isValid should be(true)
    recommendation.recommendations.head._1 <= nCards should be(true)
  }

  "trained model" should "infer default recommendation for unknown profile" in {

    val recommendation = Infer
      .getRelevantCards(model, profileIds = Seq(profileIdNew))
      .head

    recommendation.profile should be(profileIdNew)
    recommendation.isValid should be(true)
    recommendation.isDefault should be(true)
  }

  "trained model mleap" should "infer valid recommendation" in {

    val cardId = inferMLeap.getTopCardId(profileId = 1)
    cardId should not be Wizard.defaultCardId
  }

  "trained model mleap" should "infer default recommendation for unknown profile" in {

    val cardId = inferMLeap.getTopCardId(profileIdNew)
    cardId should be(Wizard.defaultCardId)
  }
}
