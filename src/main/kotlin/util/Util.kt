package main.util

object Util {
    //Adds a "/" of it doesn't end with one, useful when working with files
    internal fun mayAppendSlash(string: String) = if (string.endsWith("/")) string else "$string/"
}