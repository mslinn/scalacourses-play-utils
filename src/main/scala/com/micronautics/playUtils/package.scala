package com.micronautics

import play.api.libs.json.{Format, JsError, JsResult, JsString, JsSuccess, JsValue}
import scala.reflect.ClassTag

package object playUtils {
  implicit object JsonFormatImplicits extends JsonFormats

  /** Create Play JSON Reads/Writes (Formatter) for any Java Enum.
    * Use like this (the unit tests contain similar code):
    * {{{
    * implicit val myEnumFormat = javaEnumFormat[MyEnum]
    * Json.toJson(DiscountEnum.FullPrice) == "FullPrice"
    * Json.fromJson[DiscountEnum](JsString("FullPrice")) == DiscountEnum.FullPrice
    * }}}
    * @see [http://stackoverflow.com/a/34045056/553865] */
  def javaEnumFormat[E <: Enum[E] : ClassTag] = new Format[E] {
    override def reads(json: JsValue): JsResult[E] = json.validate[String] match {
      case JsSuccess(value, _) => try {
        val clazz = implicitly[ClassTag[E]].runtimeClass.asInstanceOf[Class[E]]
        JsSuccess(Enum.valueOf(clazz, value))
      } catch {
        case _: IllegalArgumentException => JsError("enumeration.unknown.value")
      }
      case JsError(_) => JsError("enumeration.expected.string")
    }

    override def writes(o: E): JsValue = JsString(o.toString)
  }
}
