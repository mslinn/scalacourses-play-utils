package com.micronautics

import play.api.{Application, Configuration, Environment}
import play.api.i18n.{DefaultMessagesApi, Lang, Langs, Messages, MessagesImpl}
import play.api.mvc.RequestHeader

package object i18n {
  def bestLang(implicit request: RequestHeader, app: Application, env: Environment, langs: Langs): Lang = {
    val perfectLang = request.acceptLanguages intersect langs.availables
    perfectLang.headOption.getOrElse {
      val commonLangCodes = request.acceptLanguages.map(_.language).distinct intersect langs.availables.map(_.language).distinct
      commonLangCodes.headOption.map(Lang.apply).getOrElse {
        langs.availables.headOption.getOrElse(Lang.defaultLang)
      }
    }
  }

  /**  Usage: ```implicit val messages = userMessages
    * Messages("key")``` */
  def userMessages(implicit request: RequestHeader, app: Application, configuration: Configuration, env: Environment, langs: Langs): Messages = {
    lazy val defaultMessages = new DefaultMessagesApi(langs=langs)
    MessagesImpl(bestLang, defaultMessages)
  }
}
