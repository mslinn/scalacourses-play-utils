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

import play.api.i18n.{DefaultMessagesApi, Lang, Langs, Messages, MessagesImpl}
import play.api.mvc.RequestHeader

package object i18n {
  def bestLang(implicit
               request: RequestHeader,
               langs: Langs
              ): Lang = {
    val perfectLang: Seq[Lang] = request.acceptLanguages intersect langs.availables
    perfectLang.headOption.getOrElse {
      val requestLangs: Seq[String] = request.acceptLanguages.map(_.language).distinct
      val availableLangs: Seq[String] = langs.availables.map(_.language).distinct
      val commonLangs: Seq[String] = requestLangs intersect availableLangs
      commonLangs.headOption.map(Lang.apply).getOrElse {
        langs.availables.headOption.getOrElse(Lang.defaultLang)
      }
    }
  }

  /**  Usage: ```implicit val messages = userMessages
    * Messages("key")``` */
  def userMessages(implicit
                   request: RequestHeader,
                   langs: Langs
                  ): Messages = {
    lazy val defaultMessages = new DefaultMessagesApi(langs=langs)
    MessagesImpl(bestLang, defaultMessages)
  }
}
