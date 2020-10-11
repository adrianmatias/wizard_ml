package ml

import ml.domain.{Match, Profile, Card}
import org.scalatest._
import flatspec._
import matchers._
import org.apache.spark.sql.SparkSession

class DataProcessingTest extends AnyFlatSpec with should.Matchers {

  implicit val sparkSession: SparkSession = SparkSessionMongo.build(MongoConf.collectionMatchs)

  import sparkSession.implicits._

  behavior of this.getClass.getSimpleName

  "readCollection" should "get valid matches" in {
    DataProcessing
      .readCollection(MongoConf.collectionMatchs)
      .as[Match]
      .sample(0.01)
      .collect()
      .forall(_.isValid)
  }

  "readCollection" should "get valid profiles" in {
    DataProcessing
      .readCollection(MongoConf.collectionProfiles)
      .as[Profile]
      .sample(0.01)
      .collect()
      .forall(_.isValid)
  }

  "readCollection" should "get valid cards" in {
    DataProcessing
      .readCollection(MongoConf.collectionCards)
      .as[Card]
      .sample(0.01)
      .collect()
      .forall(_.isValid)
  }
}
