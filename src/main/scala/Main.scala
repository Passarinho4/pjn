import java.util.Scanner

import scala.collection.mutable
import scala.io.Source

object Main extends App {

  private val result = mutable.HashMap[(String, String), Long]().withDefaultValue(0)

  private val stream: List[String] = Source.fromFile("pap-processed.txt").getLines().toList
  stream.sliding(12, 1)
      .foreach(list => {
        for (e <- list.tail) {
          result.update((list.head, e), result(list.head, e) + 1)
          if(result.size % 100000 == 0) {
            println(result.size)
          }
        }
      })

  val d = result.filter(_._2 > 2)

  val scanner = new Scanner(System.in)
  while(true) {
    println("Type word in basic form:")
    val word = scanner.nextLine()
    val map = d.filter(_._1._1 == word).toList.sortBy(-_._2)
    if(map.nonEmpty) {
      map.take(10).foreach(res => println(s"${res._1} -> ${res._2}"))
    } else {
      println("Nie ma")
    }
  }

}
