import Wizard._
import org.apache.spark.ml.recommendation.{ALS, ALSModel}


object Modelling {

  val filename = "recommendation_model"

  def buildModel(): ALS =
    new ALS()
      .setMaxIter(modelMaxIter)
      .setRegParam(modelRegParam)
      .setUserCol(colProfile)
      .setItemCol(colCard)
      .setRatingCol(colRating)

  def saveModel(aLSModel: ALSModel, path: String): Unit =
    aLSModel
      .write
      .overwrite()
      .save(s"$path/$filename")

  def loadModel(path: String): ALSModel =
    ALSModel
      .load(s"$path/$filename")
}
