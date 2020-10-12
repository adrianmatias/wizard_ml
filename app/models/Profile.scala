package models

import javax.inject.Inject
import ml.MongoConf
import play.modules.reactivemongo.ReactiveMongoApi
import reactivemongo.api.bson.BSONDocument
import reactivemongo.api.bson.collection.BSONCollection
import reactivemongo.api.commands.WriteResult

import scala.concurrent.{ExecutionContext, Future}

case class Profile(
                    id: Long,
                    name: String,
                    description: Map[String, Int]
                  ) {
  lazy val isValid: Boolean = name.nonEmpty & description.nonEmpty
}

object Profile {

  import play.api.libs.json._

  implicit val profileFormat: OFormat[Profile] = Json.format[Profile]
}

class ProfileRepository @Inject()(
                                implicit ec: ExecutionContext,
                                reactiveMongoApi: ReactiveMongoApi) {

  import reactivemongo.play.json.compat
  import compat.json2bson._

  private def profilesCollection: Future[BSONCollection] =
    reactiveMongoApi.database.map(_.collection(MongoConf.collectionProfiles))

  def getAll: Future[Seq[Profile]] =
    profilesCollection.flatMap(_.find(BSONDocument.empty).
      cursor[Profile]().collect[Seq](100))

  def getProfile(id: Long): Future[Option[Profile]] =
    profilesCollection.flatMap(_.find(BSONDocument(
      "id" -> id
    )).one[Profile])

  def addProfile(profile: Profile): Future[WriteResult] =
    profilesCollection.flatMap(_.insert.one(
      profile))

  def updateProfile(profile: Profile): Future[Option[Match]] = {
    val updateModifier = BSONDocument(
      f"$$set" -> BSONDocument(
        "description" -> profile.description)
    )

    profilesCollection.flatMap(_.findAndUpdate(
      selector = BSONDocument(
        "id" -> profile.id
      ),
      update = updateModifier,
      fetchNewObject = true).map(_.result[Match])
    )
  }

  def deleteProfile(id: Long): Future[Option[Profile]] =
    profilesCollection.flatMap(_.findAndRemove(
      selector = BSONDocument(
        "id" -> id
      )).map(_.result[Profile]))
}