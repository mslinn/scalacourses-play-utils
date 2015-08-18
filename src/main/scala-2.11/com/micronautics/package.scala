package com.micronautics

import play.api.Environment
import play.api.i18n.{Messages, DefaultLangs, Lang, DefaultMessagesApi}
import play.api.mvc.RequestHeader

package object i18n {
  def bestLang(implicit request: RequestHeader) = {
    import play.api.Play.current
    val perfectLang = request.acceptLanguages intersect Lang.availables
    perfectLang.headOption.getOrElse {
      val commonLangCodes = request.acceptLanguages.map(_.language).distinct intersect Lang.availables.map(_.language).distinct
      commonLangCodes.headOption.map(Lang.apply).getOrElse {
        Lang.availables.headOption.getOrElse(Lang.defaultLang)
      }
    }
  }

  /**  Usage: ```implicit val messages = userMessages
    * Messages("key")``` */
  def userMessages(implicit request: RequestHeader) = {
    val config = play.api.Play.current.configuration
    lazy val defaultMessages = new DefaultMessagesApi(Environment.simple(), config, new DefaultLangs(config))
    Messages(bestLang, defaultMessages)
  }
}
