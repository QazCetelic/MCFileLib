package util

import java.nio.file.Path

object Util {
    //Adds a "/" of it doesn't end with one, useful when working with files
    internal fun mayAppendSlash(string: String) = if (string.endsWith("/")) string else "$string/"

    operator fun Path.div(string: String): Path = this.resolve(string)
}