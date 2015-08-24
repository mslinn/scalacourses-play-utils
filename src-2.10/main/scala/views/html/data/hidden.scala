package views.html.data

import play.api.data.Field
import play.twirl.api.Html

object hidden {
  def apply(field: Field): Html =
    Html(s"""<input type="hidden" id="${field.name}" name="${field.name}" value="${field.value.getOrElse("")}">""")
}
