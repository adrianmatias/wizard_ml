package ml

import org.scalatest.flatspec._
import org.scalatest.matchers._


class MongoConfTest extends AnyFlatSpec with should.Matchers {

  behavior of this.getClass.getSimpleName

  "getUri" should "provide correct uri" in {
    MongoConf.getUri(MongoConf.collectionMatches) should be(s"mongodb://localhost:27017/wizard.matches")
  }

  "getUriShort" should "provide correct uri" in {
    MongoConf.getUriShort should be(s"mongodb://localhost:27017/")
  }

}
