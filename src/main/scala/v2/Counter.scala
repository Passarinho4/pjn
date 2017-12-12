package v2

import java.io.{File, PrintWriter}
import java.util.Scanner

import scala.collection.mutable
import scala.io.Source

object Counter extends App {

  val rangeRadius = 6

  val resultFile = new File("pap-indexed.txt")

  case class UnorderedTuple[T](a: T, b: T) {
    override def equals(obj: scala.Any): Boolean = obj match {
      case UnorderedTuple(a1, b1) => (a == a1 && b == b1) || (a == b1 && b == a1)
      case _ => false
    }
    override def hashCode(): Int = a.hashCode() * b.hashCode()

    def contains(o:T): Boolean = a == o || b == o

  }

  def parse(lines: Stream[String]): Stream[Stream[String]] = {
    lines.map(line => line.split(" ").toStream)
  }

  val words = parse(Source.fromFile(new File("pap-processed-stream.txt")).getLines().toStream)

  var result = mutable.HashMap[UnorderedTuple[String], Long]()
    .withDefaultValue(0)

  for {line <- words
       wordId <- line.indices} {
    line.slice(wordId+1, wordId + rangeRadius).foreach(w => {
      val key = UnorderedTuple(line(wordId), w)
      result.update(key, result(key) + 1)
    })
  }

  val filteredResults = result
    .filter(_._2 > 3)
    .toList
    .sortBy(-_._2)

  val printer = new PrintWriter(resultFile)
  for(r <- filteredResults) {
    printer.println(s"${r._1.a},${r._1.b},${r._2}")
  }

  println(s"Indexed result saved in file ${resultFile.getName}")

}
