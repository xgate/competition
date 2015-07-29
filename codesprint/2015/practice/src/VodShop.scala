import scala.collection.mutable

/**
 * vod & 구매자에 대한 정보를 가지고 있다.
 */
class VodShop(val vods: Map[String, Item]) {

  // [vodId, [userIds]]
  val book = mutable.Map.empty[String, mutable.Set[Int]]
  // [vodId, [Vod]]
  val relation = mutable.Map.empty[String, Vod]

  def sold(item: String, who: Int) = {
    book.get(item) match {
      case None =>
        val users = mutable.Set.empty[Int]
        users += who
        book += (item -> users)
      case Some(set) =>
        set += who
        book(item) = set
    }
  }

  def customers(item: String): List[Int] = {
    book.get(item) match {
      case None => List.empty
      case Some(users) => users.toList
    }
  }

  def setRelation(v1: String, v2: String) = {
    val score = compare(v1, v2)
    join(v1, v2, score)
    join(v2, v1, score)
  }

  def compare(v1: String, v2: String): Int = {
    val s1 = book.get(v1).get
    val s2 = book.get(v2).get
    s1.&(s2).size + getSimilarity(v1, v2)
  }

  def getSimilarity(id1: String, id2: String): Int = {
    val v1 = vods.get(id1).get
    val v2 = vods.get(id2).get
    var score = 0

    score += v1.actors.intersect(v2.actors).size
    score += v1.directors.intersect(v2.actors).size
    score += v1.genreNmList.intersect(v2.genreNmList).size

    score
  }

  def join(from: String, to: String, similarity: Int) = {
    relation.get(from) match {
      case None =>
        val vod = new Vod
        vod.add(to, similarity)
        relation += (from -> vod)
      case Some(n) =>
        n.add(to, similarity)
        relation(from) = n
    }
  }
}
