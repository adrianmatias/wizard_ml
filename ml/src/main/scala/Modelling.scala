package ml

import Wizard._
import org.apache.spark.ml.recommendation.{ALS, ALSModel}


object Modelling {

  val filename = "recommendation_model"
  val pathFilename = s"${Wizard.modelPath}/$filename"

  def buildModel(): ALS =
    new ALS()
      .setMaxIter(modelMaxIter)
      .setRegParam(modelRegParam)
      .setUserCol(colProfile)
      .setItemCol(colCard)
      .setRatingCol(colRating)

  def saveModel(aLSModel: ALSModel): Unit =
    aLSModel
      .write
      .overwrite()
      .save(pathFilename)

  def loadModel(): ALSModel =
    ALSModel
      .load(pathFilename)
}
