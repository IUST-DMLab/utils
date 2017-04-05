package ir.ac.iust.dml.kg.raw.utils

object LanguageChecker {
   val englishRegex = Regex("[\\w \\d':;.,]+")
   fun isEnglish(text: String) = englishRegex.matches(text)
}