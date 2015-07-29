import scala.collection.mutable

/**
 * vod <-> vod 유사도 관리용 class
 */
class VodMap {

  // [vodId, similarity]
  val similarityMap = mutable.Map.empty[String, Int]

  def add(target: String, similarity: Int) = {
    similarityMap(target) = similarity
  }

  def exist(target: String): Boolean = {
    similarityMap.get(target).isDefined
  }

  def top(n: Int): List[(String, Int)] = {
    similarityMap.toList.sortWith(_._2 > _._2).take(n)
  }
}
