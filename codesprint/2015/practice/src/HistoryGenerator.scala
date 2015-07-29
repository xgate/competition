
case class History(userId: Int,
                   buyDt: String,
                   glssId: String,
                   totUseAmount: Double)

object History {

  def apply(tokens: Array[String]) = new History(tokens(0).toInt, tokens(1), tokens(2), tokens(3).toDouble)
}

object HistoryGenerator {

  val fileName = "purchaseRecord.tsv"
  var maxCost = 0.0

  def generate(): List[History] = {
    val lines = scala.io.Source.fromFile(fileName, "utf8").getLines()
    val histories = lines map {
      s =>
        val tokens = s.split('\t')
        val h = History(tokens)
        maxCost = Math.max(maxCost, h.totUseAmount)
        h
    }
    histories.toList
  }
}
