package ml

case object MongoConf {

  val host = "localhost"
  val port = 27017
  val db = "wizard"
  val collectionCards = "cards"
  val collectionProfiles = "profiles"
  val collectionMatchs = "matchs"

  def getUri(collection: String): String = s"$getUriShort$db.$collection"

  def getUriShort: String = s"mongodb://$host:$port/"
}
