package com.micronautics

import org.joda.time.{Minutes, Seconds, Hours, Days}
import play.api.libs.json._
import org.scalatestplus.play._
import com.micronautics.playUtils.JsonFormats

class QuickTest extends PlaySpec with OneServerPerSuite with JsonFormats {
  def daySeconds(d: Int) = d * 60 * 60 * 24
  def hourSeconds(h: Int) = h * 60 * 60
  def minuteSeconds(m: Int) = m * 60
  def seconds(s: Int) = s

  "Tuple2[Int, String]" should {
    "Convert to JSON" in {
      Json.toJson(Days.days(9)) mustEqual JsNumber(daySeconds(9))
      Json.toJson(Hours.hours(8)) mustEqual JsNumber(hourSeconds(8))
      Json.toJson(Minutes.minutes(7)) mustEqual JsNumber(minuteSeconds(7))
      Json.toJson(Seconds.seconds(6)) mustEqual JsNumber(seconds(6))

      Json.toJson("Five" -> 5.0) mustEqual Json.arr("Five", 5.0)
      Json.toJson(Map(1L -> 2)) mustEqual Json.arr(List(JsNumber(1L), JsNumber(2)))
      Json.toJson(Map(1 -> List(2L, 3L))) mustEqual Json.arr(List(JsNumber(1), Json.arr(JsNumber(2), JsNumber(3))))
    }

    "Convert from JSON" in {
      val jsValueMap: JsValue = Json.parse("""{"key1":1,"key2":2}""")
      val actual1: JsResult[Map[String, Int]] = Json.fromJson[Map[String, Int]](jsValueMap)
      actual1.get mustEqual Map("key1" -> 1, "key2" -> 2)

      val actual2: JsResult[Map[String, Long]] = Json.fromJson[Map[String, Long]](jsValueMap)
      actual2.get mustEqual Map("key1" -> 1L, "key2" -> 2L)

      Json.fromJson[Days](   Json.parse(daySeconds(9).toString)).get    mustEqual Days.days(9)
      Json.fromJson[Hours](  Json.parse(hourSeconds(8).toString)).get   mustEqual Hours.hours(8)
      Json.fromJson[Minutes](Json.parse(minuteSeconds(7).toString)).get mustEqual Minutes.minutes(7)
      Json.fromJson[Seconds](Json.parse(seconds(6).toString)).get       mustEqual Seconds.seconds(6)
    }
  }
}
