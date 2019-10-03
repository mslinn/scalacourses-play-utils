package com.micronautics.playUtils

import java.time.{Duration, Period}
import java.time.LocalDate
import play.api.libs.json._

object JsonFormats {
  protected lazy val secondsInMinute: Long = 60
  protected lazy val secondsInHour: Long = secondsInHour * 60
  protected lazy val secondsInDay: Long = secondsInHour * 24

  /** @ sanitizeDuration("1")
    * res12: Seconds = PT1S
    *
    * @ sanitizeDuration("1:2:3")
    * res31: Seconds = PT3723S */
  protected[playUtils] def sanitizeDuration(secondsStr: String): Duration = {
    val c1 = secondsStr.indexOf(":")
    if (c1>=0) {
      val c2 = secondsStr.lastIndexOf(":")
      Duration
        .ofSeconds(secondsStr.substring(c2+1).toLong)
        .plusMinutes(secondsStr.substring(c1+1, c2).toLong)
        .plusHours(secondsStr.substring(0, c1).toLong)
    } else
      try {
        Duration.ofSeconds(secondsStr.toLong)
      } catch {
        case e: Exception =>
          println(e.getMessage)
          Duration.ofSeconds(0L)
      }
  }
}

trait JsonFormats {
  import JsonFormats._

  /** Converts from seconds */
  implicit object DurationReads extends Reads[Duration] {
    def reads(json: JsValue): JsResult[Duration] = json match {
      case JsNumber(string) => try {
        JsSuccess[Duration](sanitizeDuration(string.toString))
      } catch {
        case _: Exception =>
          JsError(JsPath() -> JsonValidationError("error.expected.java.time.period"))
      }

      case JsString(string) => try {
        JsSuccess[Duration](sanitizeDuration(string))
      } catch {
        case _: Exception =>
          JsError(JsPath() -> JsonValidationError("error.expected.java.time.period"))
      }

      case _ => JsError(JsPath() ->
        JsonValidationError("error.expected.java.time.period"))
    }
  }

  /** Converts to seconds */
  implicit object DurationWrites extends Writes[Duration] {
    def writes(duration: Duration) = JsNumber(BigDecimal(duration.getSeconds))
  }

  /** Converts from seconds */
  implicit object PeriodReads extends Reads[Period] {
    def reads(json: JsValue): JsResult[Period] = json match {
      case JsString(string) => JsSuccess[Period](Period.parse(string))
      case _ => JsError(JsPath() -> JsonValidationError("error.expected.java.time.period"))
    }
  }

  /** Converts to seconds */
  implicit object PeriodWrites extends Writes[Period] {
    def writes(period: Period) = JsNumber(BigDecimal(period.getDays) * secondsInDay)
  }

  implicit val localDateFormat: Format[LocalDate] = new Format[LocalDate] {
    override def reads(json: JsValue): JsResult[LocalDate] =
      json.validate[String].map(LocalDate.parse)

    override def writes(o: LocalDate): JsValue = Json.toJson(o.toString)
  }

  implicit def tuple2Reads[A, B](implicit aReads: Reads[A], bReads: Reads[B]): Reads[(A, B)] = Reads[(A, B)] {
    case JsArray(arr) if arr.size == 2 => for {
      a <- aReads.reads(arr.head)
      b <- bReads.reads(arr(1))
    } yield (a, b)
    case _ => JsError(JsPath() -> JsonValidationError("Expected array of two elements"))
  }

  implicit def tuple2Writes[A, B](implicit aWrites: Writes[A], bWrites: Writes[B]): Writes[(A, B)] =
    (tuple: (A, B)) => JsArray(Seq(aWrites.writes(tuple._1), bWrites.writes(tuple._2)))

  implicit val mapLongIntReads: Reads[Map[Long, Int]] =
    (json: JsValue) => json.validate[Map[String, Int]].map(_.map {
      case (key, value) => key.toLong -> value
    })

  implicit val mapLongListIntReads: Reads[Map[Long, List[Int]]] = {
    json: JsValue => json.validate[Map[String, List[Int]]].map(_.map {
      case (key, value) => key.toLong -> value
    })
  }

  implicit val mapStringIntWrites: Writes[Map[String, Int]] =
    (mapStringInt: Map[String, Int]) => {
      Json.arr(mapStringInt.map {
        case (key, value) => Json.obj(key -> value)
      }.toSeq)
    }

  implicit val mapStringLongWrites: Writes[Map[String, Long]] =
    (mapStringLong: Map[String, Long]) => {
      Json.arr(mapStringLong.map {
        case (key, value) => Json.obj(key -> value)
      }.toSeq)
    }
}
