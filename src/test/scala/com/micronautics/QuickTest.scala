package com.micronautics

import play.api.libs.json._
import org.scalatestplus.play._
import com.micronautics.playUtils.JsonFormats

class QuickTest extends PlaySpec with OneServerPerSuite with JsonFormats {
  "Tuple2[Int, String]" should {
    "Convert to JSON" in {
      Json.toJson(5 -> "Five") mustEqual Json.arr(5, "Five")
      Json.toJson("Five" -> 5.0) mustEqual Json.arr("Five", 5.0)
      Json.toJson(Map(1L -> 2)) mustEqual Json.arr(List(JsNumber(1L), JsNumber(2)))
      Json.toJson(Map(1 -> List(2L, 3L))) mustEqual Json.arr(List(JsNumber(1), Json.arr(JsNumber(2), JsNumber(3))))
    }

    "Convert from JSON" in {
      val jsValue: JsValue = Json.parse("""{"key1":1,"key2":2}""")
      val actual1: JsResult[Map[String, Int]] = Json.fromJson[Map[String, Int]](jsValue)
      actual1.get mustEqual Map("key1" -> 1, "key2" -> 2)

      val actual2: JsResult[Map[String, Long]] = Json.fromJson[Map[String, Long]](jsValue)
      actual2.get mustEqual Map("key1" -> 1L, "key2" -> 2L)
    }
  }
}
