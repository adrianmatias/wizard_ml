package ml

import org.apache.spark.sql.SparkSession

object SparkSessionMongo {

  def build(mongoCollection: String): SparkSession = {
    val sparkSession: SparkSession = SparkSession
      .builder()
      .master("local[*]")
      .appName(this.getClass.getSimpleName)
      .config("spark.mongodb.input.uri", MongoConf.getUri(mongoCollection))
      .getOrCreate()
    sparkSession.sparkContext.setLogLevel("WARN")
    sparkSession
  }
}
