import domain.{Card, Match, Profile}
import org.apache.spark.ml.recommendation.ALS
import org.apache.spark.sql.SparkSession

object Train {

  def main(args: Array[String]): Unit = {

    val colUser = "profile"
    val colItem = "card"
    val colRating = "rating"

    implicit val sparkSession: SparkSession = buildSparkSession()

    import sparkSession.implicits._

    val matches = DataProcessing
      .readCollection(MongoConf.collectionMatches)
      .as[Match]

    val cards = DataProcessing
      .readCollection(MongoConf.collectionCards)
      .as[Card]

    val profiles = DataProcessing
      .readCollection(MongoConf.collectionProfiles)
      .as[Profile]

    matches.show()
    cards.show()
    profiles.show()

    val als = new ALS()
      .setMaxIter(5)
      .setRegParam(0.01)
      .setUserCol(colUser)
      .setItemCol(colItem)
      .setRatingCol(colRating)
    val model = als.fit(matches)

    model.itemFactors.show()
    model.userFactors.show()
    model
      .recommendForAllUsers(2)
      .show()
  }

  def buildSparkSession(): SparkSession = {
    val sparkSession: SparkSession = SparkSession
      .builder()
      .master("local[*]")
      .appName(this.getClass.getSimpleName)
      .config("spark.mongodb.input.uri", MongoConf.getUri(MongoConf.collectionMatches))
      .getOrCreate()
    sparkSession.sparkContext.setLogLevel("WARN")
    sparkSession
  }
}