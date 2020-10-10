package ml

import com.mongodb.spark._
import com.mongodb.spark.config.ReadConfig
import org.apache.spark.sql.{DataFrame, SparkSession}

object DataProcessing {

  def readCollection(collection: String)(implicit sparkSession: SparkSession): DataFrame = MongoSpark
    .load(sparkSession, readConfig = ReadConfig(Map(
      "uri" -> MongoConf.getUriShort,
      "collection" -> collection
    ), Some(ReadConfig(sparkSession))))
}

