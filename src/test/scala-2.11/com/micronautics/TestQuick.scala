package com.micronautics

import org.joda.time.{Minutes, Seconds, Hours, Days}
import play.api.libs.json._
import org.scalatestplus.play._
import com.micronautics.playUtils._

class TestQuick extends PlaySpec with OneServerPerSuite with JsonFormats {
  def daySeconds(d: Int): BigDecimal    = BigDecimal(d * 60 * 60 * 24)
  def hourSeconds(h: Int): BigDecimal   = BigDecimal(h * 60 * 60)
  def minuteSeconds(m: Int): BigDecimal = BigDecimal(m * 60)
  def seconds(s: Int): BigDecimal       = BigDecimal(s)

  "Tuple2[Int, String]" should {
    "Convert to JSON" in {
      Json.toJson(Days.days(9)) mustEqual JsNumber(daySeconds(9))
      Json.toJson(Hours.hours(8)) mustEqual JsNumber(hourSeconds(8))
      Json.toJson(Minutes.minutes(7)) mustEqual JsNumber(minuteSeconds(7))
      Json.toJson(Seconds.seconds(6)) mustEqual JsNumber(seconds(6))

      Json.toJson(JsString("00:00:01")).as[Seconds] mustEqual Seconds.seconds(1)
      Json.toJson(JsString("00:01:01")).as[Seconds] mustEqual Seconds.seconds(61)
      Json.toJson(JsString("00:01:00")).as[Minutes] mustEqual Minutes.minutes(1)
      Json.toJson(JsString("01:00:00")).as[Hours]   mustEqual Hours.hours(1)

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

    "Create Formatters for Java enums" in {
      implicit val myEnumFormat = javaEnumFormat[DiscountEnum]
      Json.toJson(DiscountEnum.FullPrice) === "FullPrice"
      Json.fromJson[DiscountEnum](JsString("FullPrice")) === DiscountEnum.FullPrice
      ()
    }
  }
}
