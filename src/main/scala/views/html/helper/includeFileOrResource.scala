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

package views.html.helper

import java.io.File
import javax.inject.Inject
import play.api.i18n._
import play.twirl.api.Html
import scala.io.Codec
import scala.io.Source.{fromFile, fromInputStream}

class includeFileOrResource @Inject() (implicit env: play.api.Environment) {
  def forFile(path: String): Option[String] = {
    val maybeFile = env.getExistingFile(path)
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
        env.resourceAsStream(s"$rootDir/$path").map(fromInputStream(_)(Codec.UTF8).mkString)
      }.getOrElse("File Not Found")
    }
    Html(result)
  }
}
