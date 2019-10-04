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

