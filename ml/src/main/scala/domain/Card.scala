package ml.domain

case class Card(
                 id: Long,
                 name: String,
                 number: String,
                 arcana: String,
                 suit: String,
                 keywords: Seq[String],
                 // meanings: Map[String, Seq[String]]  //TODO: define meanings field
                 Archetype: String,
                 Numerology: String,
                 Elemental: String
               ) {
  lazy val isValid: Boolean = name.nonEmpty & keywords.nonEmpty
}
