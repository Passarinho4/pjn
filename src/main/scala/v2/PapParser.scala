package v2

import morfologik.stemming.WordData
import morfologik.stemming.polish.PolishStemmer

import scala.annotation.tailrec

object PapParser {

  private val charsToIgnore = Array('.', ',', '\"', '\'')
  private val stemmer = new PolishStemmer

  /**
    * Parses lines from PAP notes and returns stream of notes.
    */
  def parse(s: Stream[String]): Stream[String] = {
    var stream = s

    @tailrec
    def helper(nextId: String, buffer: StringBuilder, counter: Int, stream: Stream[String]): (String, Int) = {
      if (stream.isEmpty || stream.head == nextId) {
        (buffer.toString(), counter)
      } else {
        helper(nextId, buffer.append(stream.head), counter + 1, stream.tail)
      }
    }

    var noteLines = 1
    Stream.from(2)
      .map(i => {
        stream = stream.drop(noteLines)
        val result = helper(s"#${"%06d".format(i)}", new StringBuilder, 0, stream)
        noteLines = result._2 + 1
        result._1})
      .takeWhile(_ != "")
  }

  def mapToWords(s: String): Stream[String] = {
    s.split(' ')
      .toStream
      .filter(_.length > 3)
      .map(word =>
        String.valueOf(word.filter(!charsToIgnore.contains(_))))
  }

  def mapToLem(s: String): Option[String] = {
    def getWordData(s: String): Option[WordData] = {
      val data = stemmer.lookup(s)
      if(data.isEmpty) {
        None
      } else {
        Some(data.get(0))
      }
    }
    getWordData(s).map(_.getStem.toString.toLowerCase())
  }

}
