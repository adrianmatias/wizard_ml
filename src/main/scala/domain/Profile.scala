package domain

case class Profile(
                    id: Long,
                    name: String,
                    description: Map[String, Int],
                  ) {
  val isValid: Boolean = description.nonEmpty
}
