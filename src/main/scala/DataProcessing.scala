import domain.Match
import org.apache.spark.sql.{Dataset, SparkSession}

object DataProcessing {

  val folder = "/home/mat/iebs/lessons/mongo_ml_restapi/data"
  val filename = "matches.json"

  def readMatches()(implicit sparkSession: SparkSession): Dataset[Match] = {
    import sparkSession.implicits._
    sparkSession
      .read
      .json(s"$folder/$filename")
      .as[Match]
  }
}

