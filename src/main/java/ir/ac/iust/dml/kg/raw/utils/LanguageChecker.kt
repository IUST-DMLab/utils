package ir.ac.iust.dml.kg.raw.utils

object LanguageChecker {
  val englishRegex = Regex("[\\w \\d'-:;.,]+")
  fun isEnglish(text: String) = englishRegex.matches(text)
  fun isPersian(text: String) = !englishRegex.matches(text)

  fun detectLanguage(text: String?) =
      if (text == null) null
      else {
        if (isEnglish(text)) "en" else "fa"
      }

  fun multiLanguages(text: String): Boolean {
    if (englishRegex.matches(text)) return false
    for (ch in text) if ((ch in 'A'..'Z') || (ch in 'a'..'z')) return true
    return false
  }
}