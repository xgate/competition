import java.io.{File, PrintWriter, Writer}

trait Algorithm {

  println("********** start generation ******************")
  val histories = HistoryGenerator.generate()
  val items = ItemGenerator.generate()
  println("********** end generation ******************")

  def learn()

  def write(): Unit = {
    val resultFile = "predict.csv"
    val writer = new PrintWriter(new File(resultFile))
    write(writer)
    writer.close()
  }

  def write(writer: Writer)
}
