import scala.collection.mutable

class Vod {

  // [vodId, similarity]
  val vods = mutable.Map.empty[String, Int]

  def add(target: String, similarity: Int) = {
    vods(target) = similarity
  }

  def exist(target: String): Boolean = {
    vods.get(target).isDefined
  }

  def sortedList(n: Int): List[(String, Int)] = {
    vods.toList.sortWith(_._2 > _._2).take(n)
  }
}
