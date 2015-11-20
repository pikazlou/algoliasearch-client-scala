package algolia

import algolia.AlgoliaDsl._
import algolia.responses.{Indexes, Search}
import org.scalatest.concurrent.PatienceConfiguration.Timeout
import org.scalatest.time.{Seconds, Span}
import org.json4s._
import org.json4s.native.JsonMethods._

import scala.concurrent.Future

class IntegrationTest extends AlgoliaTest {

  val client = new AlgoliaClient(applicationId, apiKey)

  describe("indexes") {

    it("should get a response") {
      val indices: Future[Indexes] = client.execute {
        indexes
      }

      whenReady(indices) { result =>
        result.nbPages should equal(1)
        result.items should have size 1
        result.items.head.name should be("test")
      }
    }
  }

  describe("search") {

//    case class Test(name: String,
//                   age: Int,
//                   alien: Boolean)

    it("should search with a query") {
      val s: Future[Search] = client.execute {
        search into Index("test") query "a"
      }

      whenReady(s) { result =>
        result.hits should have length 1
        result.hits.head.values should be(Map("name" -> "algolia", "age" -> 10, "alien" -> false))
      }

    }
  }

}
