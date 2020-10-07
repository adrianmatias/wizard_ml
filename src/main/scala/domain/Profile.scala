package domain

case class Profile(
                    id: Long,
                    name: String,
                    description: Map[String, Int],
                  ) {
  lazy val isValid: Boolean = description.nonEmpty
}
