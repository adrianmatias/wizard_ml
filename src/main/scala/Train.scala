import org.apache.spark.ml.recommendation.ALS
import org.apache.spark.sql.SparkSession

object Train {

  def main(args: Array[String]): Unit = {

    val colUser = "profile"
    val colItem = "card"
    val colRating = "rating"

    implicit val sparkSession: SparkSession = SparkSession
      .builder
      .appName(this.getClass.getSimpleName)
      .master("local[*]")
      .getOrCreate()

    val data = DataProcessing.readMatches()
    data.show()

    val als = new ALS()
      .setMaxIter(5)
      .setRegParam(0.01)
      .setUserCol(colUser)
      .setItemCol(colItem)
      .setRatingCol(colRating)
    val model = als.fit(data)

    model.itemFactors.show()
    model.userFactors.show()
    model
      .recommendForAllUsers(2)
      .show()

  }
}