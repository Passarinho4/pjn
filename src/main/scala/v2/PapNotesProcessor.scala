package v2

import java.io.{File, PrintWriter}

import scala.io.Source

/**
  * I takes PAP notes in their format and returns file with stems of words.
  * Each note in new line, separated by space.
  * It removes words shorter than 3, dots and some special chars.
  * It also uses PolishStemmer to get Stem from the file.
  */

object PapNotesProcessor extends App {

  val resultFile = new File("pap-processed-stream.txt")
  val printer = new PrintWriter(resultFile)

  val notesStream = PapParser.parse(Source.fromFile(new File("pap.txt")).getLines().toStream)
    .map(PapParser.mapToWords)
    .map(note => note.map(PapParser.mapToLem)
      .collect{case Some(s) => s})

  var counter = 0
  for (note <- notesStream) {
    for(word <- note) {
      printer.print(word + " ")
      if(counter % 10000 == 0) {
        println(counter)
      }
      counter = counter + 1
    }
    printer.print(System.lineSeparator())
  }
}
