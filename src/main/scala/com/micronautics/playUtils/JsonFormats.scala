package com.micronautics.playUtils

import play.api.libs.json._
import play.api.data.validation._

trait JsonFormats {
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

  implicit val mapStringIntReads: Reads[Map[Long, Int]] = {
    new Reads[Map[Long, Int]] {
      def reads(json: JsValue) =
        json.validate[Map[String, Int]].map(_.map {
          case (key, value) => key.toLong -> value
        })
    }
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

