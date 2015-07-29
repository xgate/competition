
case class History(userId: Int,
                   buyDate: String,
                   glssId: String,
                   totUseAmount: Int)

object History {

  def apply(tokens: Array[String]) = new History(tokens(0).toInt, tokens(1), tokens(2), tokens(3).toInt)
}

object HistoryGenerator {

  def generate(file: String): List[History] = {
    val lines = scala.io.Source.fromFile(file, "utf8").getLines()
    val histories = lines map {
      s =>
        val tokens = s.split('\t')
        History(tokens)
    }
    histories.toList
  }
}
