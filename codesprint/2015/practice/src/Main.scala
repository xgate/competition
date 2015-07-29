/**
 * Main Class
 */
object Main extends App {

  val algorithm = new ItemFeatureBasedAlgorithm

  println("start learning....")
  algorithm.learn()
  println("end learning....")

  println("start write...")
  algorithm.write()
  println("end write...")
}
