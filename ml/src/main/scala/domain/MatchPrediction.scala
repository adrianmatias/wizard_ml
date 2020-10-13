package ml.domain

case class MatchPrediction(
                            datetime: String = "",
                            card: Long,
                            profile: Long,
                            rating: Long = 0L,
                            prediction: Double
                          )
