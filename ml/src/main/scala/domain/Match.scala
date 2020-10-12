package ml.domain

import ml.combust.mleap.runtime.frame.Row

case class Match(
                  datetime: String = "",
                  card: Long,
                  profile: Long,
                  rating: Long = 0L
                ) {
  lazy val isValid: Boolean = 0 <= rating & rating <= 5

  def toMLeap: Row = Row(
    datetime,
    card,
    profile,
    rating,
  )
}
