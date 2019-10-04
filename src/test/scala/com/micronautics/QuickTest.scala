/* Copyright 2012-2019 Micronautics Research Corporation.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License. */

package com.micronautics

import java.time.{Duration, LocalDate}
import com.micronautics.playUtils._
import org.scalatestplus.play._
import org.scalatestplus.play.guice.GuiceOneServerPerSuite
import play.api.libs.json.{JsNumber, JsResult, JsString, JsSuccess, JsValue, Json}

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
      Json.toJson(JsString("00:02:01")).as[Duration] mustEqual Duration.ofSeconds(1).plusMinutes(2)
      Json.toJson(JsString("03:02:01")).as[Duration] mustEqual Duration.ofSeconds(1).plusMinutes(2).plusHours(3)
      Json.toJson(JsString("00:02:00")).as[Duration] mustEqual Duration.ofMinutes(2)
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

      Json.fromJson[Duration](Json.parse(daySeconds(9).toString))    mustEqual JsSuccess(Duration.ofDays(9))
      Json.fromJson[Duration](Json.parse(hourSeconds(8).toString))   mustEqual JsSuccess(Duration.ofHours(8))
      Json.fromJson[Duration](Json.parse(minuteSeconds(7).toString)) mustEqual JsSuccess(Duration.ofMinutes(7))
      Json.fromJson[Duration](Json.parse(seconds(6).toString))       mustEqual JsSuccess(Duration.ofSeconds(6))
    }

    "Create Formatters for Java enums" in {
      implicit val myEnumFormat: Object = javaEnumFormat[DiscountEnum]
      Json.toJson(DiscountEnum.FullPrice) === "FullPrice"
      Json.fromJson[DiscountEnum](JsString("FullPrice")) === DiscountEnum.FullPrice
      ()
    }
  }
}
