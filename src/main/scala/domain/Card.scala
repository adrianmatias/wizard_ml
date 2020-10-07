package domain

case class Card(
                 name: String,
                 number: String,
                 arcana: String,
                 suit: String,
                 keywords: Seq[String],
                 // meanings: Map[String, Seq[String]]  //TODO: define meanings field
               ) {
  lazy val isValid: Boolean = name.nonEmpty & keywords.nonEmpty
}
