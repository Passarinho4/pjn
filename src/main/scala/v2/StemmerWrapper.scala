package v2

import morfologik.stemming.polish.PolishStemmer

class StemmerWrapper {

  private val stemmer = new PolishStemmer

  def getBasicForm(word: String): Option[String] = {
    val list = stemmer.lookup(word)
    if(list.isEmpty) {
      None
    } else {
      Some(list.get(0).getStem.toString)
    }
  }

}
