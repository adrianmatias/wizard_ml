package models

import javax.inject.Inject
import ml.MongoConf

import play.modules.reactivemongo.ReactiveMongoApi
import reactivemongo.api.bson.BSONDocument
import reactivemongo.api.bson.collection.BSONCollection
import reactivemongo.api.commands.WriteResult

import scala.concurrent.{ExecutionContext, Future}

case class Match(
                  datetime: String,
                  card: Long,
                  profile: Long,
                  rating: Long
                ) {
  lazy val isValid: Boolean = 0 <= rating & rating <= 5
}  // TODO: deduplicate case class implementation

object Match {

  import play.api.libs.json._

  implicit val matchFormat: OFormat[Match] = Json.format[Match]
}

class MatchRepository @Inject()(
                                 implicit ec: ExecutionContext,
                                 reactiveMongoApi: ReactiveMongoApi) {

  import reactivemongo.play.json.compat
  import compat.json2bson._

  private def matchsCollection: Future[BSONCollection] =
    reactiveMongoApi.database.map(_.collection(MongoConf.collectionMatchs))

  def getAll: Future[Seq[Match]] =
    matchsCollection.flatMap(_.find(BSONDocument.empty).
      cursor[Match]().collect[Seq](100))

  def getMatch(cardId: Long, profileId: Long): Future[Option[Match]] =
    matchsCollection.flatMap(_.find(BSONDocument(
      "card" -> cardId,
      "profile" -> profileId
    )).one[Match])

  def addMatch(matchWiz: Match): Future[WriteResult] =
    matchsCollection.flatMap(_.insert.one(
      matchWiz))

  def updateMatch(matchWiz: Match): Future[Option[Match]] = {
    val updateModifier = BSONDocument(
      f"$$set" -> BSONDocument(
        "rating" -> matchWiz.rating)
    )

    matchsCollection.flatMap(_.findAndUpdate(
      selector = BSONDocument(
        "card" -> matchWiz.card,
        "profile" -> matchWiz.profile,
      ),
      update = updateModifier,
      fetchNewObject = true).map(_.result[Match])
    )
  }

  def deleteMatch(cardId: Long, profileId: Long): Future[Option[Match]] =
    matchsCollection.flatMap(_.findAndRemove(
      selector = BSONDocument(
        "card" -> cardId,
        "profile" -> profileId
      )).map(_.result[Match]))
}