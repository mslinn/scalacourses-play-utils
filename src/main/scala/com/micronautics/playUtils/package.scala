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
  implicit def javaEnumFormat[E <: Enum[E] : ClassTag]: Format[E] = new Format[E] {
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
