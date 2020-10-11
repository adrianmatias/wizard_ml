package models

import javax.inject.Inject
import ml.MongoConf
import play.modules.reactivemongo.ReactiveMongoApi
import reactivemongo.api.bson.BSONDocument
import reactivemongo.api.bson.collection.BSONCollection
import reactivemongo.api.commands.WriteResult

import scala.concurrent.{ExecutionContext, Future}

case class Card(
                 id: Long,
                 name: String,
                 number: String,
                 arcana: String,
                 suit: String,
                 keywords: Seq[String],
                 meanings: Map[String, Seq[String]],
                 Archetype: String,
                 Numerology: String,
                 Elemental: String
               ) {
  lazy val isValid: Boolean = name.nonEmpty & keywords.nonEmpty
}

object Card {

  import play.api.libs.json._

  implicit val cardFormat: OFormat[Card] = Json.format[Card]
}

class CardRepository @Inject()(
                                implicit ec: ExecutionContext,
                                reactiveMongoApi: ReactiveMongoApi) {

  import reactivemongo.play.json.compat
  import compat.json2bson._

  private def cardsCollection: Future[BSONCollection] =
    reactiveMongoApi.database.map(_.collection(MongoConf.collectionCards))

  def getAll: Future[Seq[Card]] =
    cardsCollection.flatMap(_.find(BSONDocument.empty).
      cursor[Card]().collect[Seq](100))

  def getCard(id: Long): Future[Option[Card]] =
    cardsCollection.flatMap(_.find(BSONDocument(
      "id" -> id
    )).one[Card])

  def addCard(card: Card): Future[WriteResult] =
    cardsCollection.flatMap(_.insert.one(
      card))

  def updateCard(card: Card): Future[Option[Match]] = {
    val updateModifier = BSONDocument(
      f"$$set" -> BSONDocument(
        "keywords" -> card.keywords)
    )

    cardsCollection.flatMap(_.findAndUpdate(
      selector = BSONDocument(
        "id" -> card.id
      ),
      update = updateModifier,
      fetchNewObject = true).map(_.result[Match])
    )
  }

  def deleteCard(id: Long): Future[Option[Card]] =
    cardsCollection.flatMap(_.findAndRemove(
      selector = BSONDocument(
        "id" -> id
      )).map(_.result[Card]))
}