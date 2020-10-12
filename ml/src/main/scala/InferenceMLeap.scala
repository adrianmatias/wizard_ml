package ml

import ml.combust.bundle.dsl.Bundle
import ml.combust.mleap.core.types.{ScalarType, StructField, StructType}
import ml.combust.mleap.runtime.frame.{DefaultLeapFrame, Row, Transformer}
import ml.domain.{Match, MatchPrediction}

class InferenceMLeap(pathFilename: String) {

  val matchSchema: StructType = StructType(
    StructField("datetime", ScalarType.String),
    StructField("card", ScalarType.Long),
    StructField("profile", ScalarType.Long),
    StructField("rating", ScalarType.Long)
  ).get

  val modelMLeap: Bundle[Transformer] = Modelling.loadModelMLeap(pathFilename)

  def getTopCardId(profileId: Long): Long = {
    getRelevantCardsMaybe(profileId).headOption match {
      case Some(matchPrediction) => matchPrediction.card
      case None => Wizard.defaultCardId
    }
  }

  private def infer(matchs: Seq[Match]): DefaultLeapFrame = {
    val dfMleap = DefaultLeapFrame(matchSchema, matchs.map(_.toMLeap))
    modelMLeap.root.transform(dfMleap).get
  }

  private def getMatch(row: Row): MatchPrediction = {
    MatchPrediction(
      datetime = row.getString(0),
      card = row.getLong(1),
      profile = row.getLong(2),
      rating = row.getLong(index = 3),
      prediction = row.getFloat(4).toDouble
    )
  }

  private def getRelevantCardsMaybe(profileId: Long): Seq[MatchPrediction] = {
    val matchs = (0 to Wizard.nCards)
      .map(card => Match(
        profile = profileId,
        card = card.toLong,
      ))
    infer(matchs)
      .collect()
      .map(getMatch)
      .sortBy(-_.prediction)
      .take(Wizard.nRecommendedCards)
  }
}
