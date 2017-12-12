package v2

import java.io.File
import java.util.Scanner

import v2.Counter.UnorderedTuple

import scala.collection.mutable
import scala.io.Source

object Searcher extends App {

  val results = mutable.MutableList[(UnorderedTuple[String], Long)]()

  for(l <- Source.fromFile(new File("pap-indexed.txt")).getLines()) {
    val data = l.split(",")
    try {
      results += (UnorderedTuple(data(0), data(1)) -> data(2).toLong)
    } catch {
      case e: Exception => println(s"Problem with parsing line ${data.toList}")
    }
  }

  val scanner = new Scanner(System.in)
  while(true) {
    val word = scanner.next()
    results.filter(_._1.contains(word))
      .take(12)
      .foreach(p => println(s"${p._1.a}, ${p._1.b} => ${p._2}"))
  }
}
