package domain

case class Match(
                  datetime: String,
                  card: Long,
                  profile: Long,
                  rating: Long
                ) {
  lazy val isValid: Boolean = 0 <= rating & rating <= 5
}
