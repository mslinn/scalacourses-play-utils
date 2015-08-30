package views.html

import java.io.File
import play.api.Play
import play.api.Play.{current, resourceAsStream}
import play.api.i18n._
import play.twirl.api.Html
import scala.io.Codec
import scala.io.Source.{fromFile, fromInputStream}

object includeFileOrResource {
  def forFile(path: String): Option[String] = {
    val maybeFile = Play.getExistingFile(path)
    val maybeContents: Option[String] = maybeFile.map { fromFile(_).mkString }
    maybeContents.orElse {
      maybeFile.map { file => // Read the file's content and wrap it in HTML or return an error message
        val file2: File = if (file.isDirectory) new File(file, "index.html") else file
        val contents: String = fromFile(file2).mkString
        contents
      }
    }
  }


  /** First search the file system for a matching file, then search Application resources for a matching file
   * @param rootDir defaults to looking in the `public` dir */
  def apply(path: String, rootDir: String = "public")(implicit lang: Lang): Html = {
    // Split filePath at name and suffix to insert the language in between them
    val (fileName, suffix) = path.splitAt(path.lastIndexOf("."))

    // Retrieve the file with the current language, or as a fallback, without any suffix
    val result: String = forFile(s"$rootDir/${fileName}_${lang.language}$suffix").getOrElse {
      forFile(s"$rootDir/$path").orElse {
        resourceAsStream(s"$rootDir/$path").map(fromInputStream(_)(Codec.UTF8).mkString)
      }.getOrElse("File Not Found")
    }
    Html(result)
  }
}
