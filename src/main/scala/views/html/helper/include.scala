package views.html.helper

import java.io.File
import play.api.Environment
import play.api.i18n._
import play.twirl.api.Html
import scala.io.Source.fromFile

object include {
  // The default is to look in the public dir, but you can change it if necessary
  def apply(filePath: String, rootDir: String = "public")
           (implicit env: Environment, lang: Lang): Html = {
    // Split filePath at name and suffix to insert the language in between them
    val (fileName, suffix) = filePath.splitAt(filePath.lastIndexOf("."))

    // Retrieve the file with the current language, or as a fallback, without any suffix
    val maybeFile: Option[File] =
      env.getExistingFile(rootDir + "/" + fileName + "_" + lang.language + suffix).
      orElse(env.getExistingFile(rootDir + "/" + filePath))

    // Read the file's content and wrap it in HTML or return an error message
    maybeFile map { file =>
      val file2 = if (file.isDirectory) new File(file, "index.html") else file
      val content = fromFile(file2).mkString
      Html(content)
    } getOrElse Html(s"""<span class="error">Include failed. rootDir='$rootDir'; filePath='$filePath'; lang=${ lang.code }</span>""")
  }
}

