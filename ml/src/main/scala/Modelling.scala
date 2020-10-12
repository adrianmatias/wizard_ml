package ml

import ml.Wizard._
import ml.combust.bundle.BundleFile
import ml.combust.bundle.dsl.Bundle
import ml.combust.mleap.runtime.MleapSupport.MleapBundleFileOps
import ml.combust.mleap.runtime.frame.Transformer
import ml.combust.mleap.spark.SparkSupport.SparkTransformerOps
import ml.domain.Match
import org.apache.spark.ml.bundle.SparkBundleContext
import org.apache.spark.ml.recommendation.{ALS, ALSModel}
import org.apache.spark.sql.Dataset
import resource.managed


object Modelling {

  val filename = "recommendation_model"

  val pathFilenameSpark = s"${Wizard.modelPath}/$filename"
  val pathFilenameMLeap = s"jar:file:$pathFilenameSpark-json.zip"

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
      .save(pathFilenameSpark)

  def loadModel(): ALSModel =
    ALSModel
      .load(pathFilenameSpark)

  def saveModelMLeap(alsModel: ALSModel, dataset: Dataset[Match]): Unit = {
    val sbc = SparkBundleContext().withDataset(alsModel.transform(dataset))
    for (bf <- managed(BundleFile(pathFilenameMLeap))) {
      alsModel.writeBundle.save(bf)(sbc).get
    }
  }

  def loadModelMLeap(pathFilename: String): Bundle[Transformer] = {
    (for (bundleFile <- managed(BundleFile(pathFilename))) yield {
      bundleFile.loadMleapBundle().get
    }).opt.get
  }

}
