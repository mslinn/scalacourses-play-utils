package com.micronautics.playUtils

import org.joda.time.{Days, Hours, Minutes, Seconds}
import play.api.libs.json._
import play.api.data.validation._

trait JsonFormats {
  /** Converts from seconds */
  implicit object DaysReads extends Reads[Days] {
    def reads(json: JsValue) = json match {
      case JsNumber(number) => JsSuccess(Days.days(number.toInt / 60 / 60 / 24))
      case JsString(string) => JsSuccess(Days.days(string.toInt / 60 / 60 / 24))
      case _ => JsError(Seq(JsPath() -> Seq(ValidationError("error.expected.joda.time.days"))))
    }
  }

  /** Converts to seconds */
  implicit object DaysWrites extends Writes[Days] {
    def writes(days: Days) = JsNumber(days.multipliedBy(60 * 60 * 24).getDays)
  }

  /** Converts from seconds */
  implicit object HoursReads extends Reads[Hours] {
    def reads(json: JsValue) = json match {
      case JsNumber(number) => JsSuccess(Hours.hours(number.toInt / 60 / 60))
      case JsString(string) => JsSuccess(Hours.hours(string.toInt / 60 / 60))
      case _ => JsError(Seq(JsPath() -> Seq(ValidationError("error.expected.joda.time.hours"))))
    }
  }

  /** Converts to seconds */
  implicit object HoursWrites extends Writes[Hours] {
    def writes(hours: Hours) = JsNumber(hours.multipliedBy(60 * 60).getHours)
  }

  /** Converts from seconds */
  implicit object MinutesReads extends Reads[Minutes] {
    def reads(json: JsValue) = json match {
      case JsNumber(number) => JsSuccess(Minutes.minutes(number.toInt / 60))
      case JsString(string) => JsSuccess(Minutes.minutes(string.toInt / 60))
      case _ => JsError(Seq(JsPath() -> Seq(ValidationError("error.expected.joda.time.minutes"))))
    }
  }

  /** Converts to seconds */
  implicit object MinutesWrites extends Writes[Minutes] {
    def writes(minutes: Minutes) = JsNumber(minutes.multipliedBy(60).getMinutes)
  }

  implicit object SecondsReads extends Reads[Seconds] {
    def reads(json: JsValue) = json match {
      case JsNumber(number) => JsSuccess(Seconds.seconds(number.toInt))
      case JsString(string) => JsSuccess(Seconds.seconds(string.toInt))
      case _ => JsError(Seq(JsPath() -> Seq(ValidationError("error.expected.joda.time.Minutes"))))
    }
  }

  implicit object SecondsWrites extends Writes[Seconds] {
    def writes(seconds: Seconds) = JsNumber(seconds.getSeconds)
  }

  implicit def tuple2Reads[A, B](implicit aReads: Reads[A], bReads: Reads[B]): Reads[(A, B)] = Reads[(A, B)] {
    case JsArray(arr) if arr.size == 2 => for {
      a <- aReads.reads(arr.head)
      b <- bReads.reads(arr(1))
    } yield (a, b)
    case _ => JsError(Seq(JsPath() -> Seq(ValidationError("Expected array of two elements"))))
  }

  implicit def tuple2Writes[A, B](implicit aWrites: Writes[A], bWrites: Writes[B]): Writes[(A, B)] = new Writes[(A, B)] {
    def writes(tuple: (A, B)) = JsArray(Seq(aWrites.writes(tuple._1), bWrites.writes(tuple._2)))
  }

  implicit val mapLongIntReads: Reads[Map[Long, Int]] =
    new Reads[Map[Long, Int]] {
      def reads(json: JsValue) =
        json.validate[Map[String, Int]].map(_.map {
          case (key, value) => key.toLong -> value
        })
    }

  implicit val mapLongListIntReads: Reads[Map[Long, List[Int]]] = {
    new Reads[Map[Long, List[Int]]] {
      def reads(json: JsValue) =
        json.validate[Map[String, List[Int]]].map(_.map {
          case (key, value) => key.toLong -> value
        })
    }
  }

  implicit val mapStringIntWrites = new Writes[Map[String, Int]] {
    def writes(mapStringInt: Map[String, Int]) = {
      Json.arr(mapStringInt.map {
        case (key, value) => Json.obj(key -> value)
      }.toSeq)
    }
  }

  implicit val mapStringLongWrites = new Writes[Map[String, Long]] {
    def writes(mapStringLong: Map[String, Long]) = {
      Json.arr(mapStringLong.map {
        case (key, value) => Json.obj(key -> value)
      }.toSeq)
    }
  }
}

