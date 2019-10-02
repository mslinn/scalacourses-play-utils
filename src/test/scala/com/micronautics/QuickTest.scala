package com.micronautics

import java.time.{Duration, LocalDate}
import com.micronautics.playUtils._
import org.scalatestplus.play._
import org.scalatestplus.play.guice.GuiceOneServerPerSuite
import play.api.libs.json.{JsNumber, JsResult, JsString, JsValue, Json}

class QuickTest extends PlaySpec with GuiceOneServerPerSuite with JsonFormats {
  def daySeconds(d: Int): BigDecimal    = BigDecimal(d * 60 * 60 * 24)
  def hourSeconds(h: Int): BigDecimal   = BigDecimal(h * 60 * 60)
  def minuteSeconds(m: Int): BigDecimal = BigDecimal(m * 60)
  def seconds(s: Int): BigDecimal       = BigDecimal(s)

  "MyJsonFormats" should {
    "serialize and deserialize LocalDates" in {
      Json.toJson(LocalDate.of(2016, 7, 9)) mustEqual JsString("2016-07-09")
      JsString("2016-07-09").as[LocalDate] mustEqual LocalDate.of(2016, 7, 9)
    }
  }

  "Tuple2[Int, String]" should {
    "Convert to JSON" in {
      Json.toJson(Duration.ofDays(9).toMillis / 1000) mustEqual JsNumber(daySeconds(9))
      Json.toJson(Duration.ofHours(8).toMillis / 1000) mustEqual JsNumber(hourSeconds(8))
      Json.toJson(Duration.ofMinutes(7).toMillis / 1000) mustEqual JsNumber(minuteSeconds(7))
      Json.toJson(Duration.ofSeconds(6).toMillis / 1000) mustEqual JsNumber(seconds(6))

      val s =  Json.toJson(JsString("00:00:01")).as[Duration]
      val m =  Json.toJson(JsString("00:01:00")).as[Duration]
      val h =  Json.toJson(JsString("01:00:00")).as[Duration]
      s.toString mustEqual "PT1S"
      m.toString mustEqual "PT1M"
      h.toString mustEqual "PT1H"
      Json.toJson(JsString("00:00:01")).as[Duration] mustEqual Duration.ofSeconds(1)
      Json.toJson(JsString("00:01:01")).as[Duration] mustEqual Duration.ofSeconds(61)
      Json.toJson(JsString("00:01:00")).as[Duration] mustEqual Duration.ofMinutes(1)
      Json.toJson(JsString("01:00:00")).as[Duration] mustEqual Duration.ofHours(1)

      Json.toJson("Five" -> 5.0) mustEqual Json.toJson(List(JsString("Five"), JsNumber(5.0)))
      Json.toJson(Map(1L -> 2)) mustEqual Json.arr(List(JsNumber(1L), JsNumber(2)))
      Json.toJson(Map(1 -> List(2L, 3L))) mustEqual Json.arr(List(JsNumber(1), Json.arr(JsNumber(2), JsNumber(3))))
    }

    "Convert from JSON" in {
      val jsValueMap: JsValue = Json.parse("""{"key1":1,"key2":2}""")
      val actual1: JsResult[Map[String, Int]] = Json.fromJson[Map[String, Int]](jsValueMap)
      actual1.get mustEqual Map("key1" -> 1, "key2" -> 2)

      val actual2: JsResult[Map[String, Long]] = Json.fromJson[Map[String, Long]](jsValueMap)
      actual2.get mustEqual Map("key1" -> 1L, "key2" -> 2L)
/*
      Json.fromJson[Days](   Json.parse(daySeconds(9).toString)).get    mustEqual Days.days(9)
      Json.fromJson[Hours](  Json.parse(hourSeconds(8).toString)).get   mustEqual Hours.hours(8)
      Json.fromJson[Minutes](Json.parse(minuteSeconds(7).toString)).get mustEqual Minutes.minutes(7)
      Json.fromJson[Seconds](Json.parse(seconds(6).toString)).get       mustEqual Seconds.seconds(6)
*/
    }

    "Create Formatters for Java enums" in {
      implicit val myEnumFormat: Object = javaEnumFormat[DiscountEnum]
      Json.toJson(DiscountEnum.FullPrice) === "FullPrice"
      Json.fromJson[DiscountEnum](JsString("FullPrice")) === DiscountEnum.FullPrice
      ()
    }
  }
}
