import java.io.{File, PrintWriter}

import scala.collection.mutable

object Main extends App {

  // [vodId, [userIds]]
  val itemMap = mutable.Map.empty[String, mutable.Set[Int]]
  // [userId, [vodIds]]
  val userMap = mutable.Map.empty[Int, mutable.Set[String]]
  // [vodId, VodMap]
  val relation = mutable.Map.empty[String, VodMap]

  val items = ItemGenerator.generate("rsc/round2_itemInfo.tsv")
  val histories = HistoryGenerator.generate("rsc/round2_purchaseRecord.tsv")

  learn()
  write()

  def learn(): Unit = {

    filmHistories foreach {
      h =>
        addUser(h)
        addItem(h)
    }

    sortedItems foreach {
      item1 =>
        val (id1, users1) = item1
        largerThan(id1.toLong) foreach {
          item2 =>
            val (id2, users2) = item2
            // item 을 구매한 사용자의 일치율이 유사도
            val score1 = users1.&(users2).size
            // item 간 공통점이 유사도
            val score2 = estimate(items.get(id1).get, items.get(id2).get)

            addRelation(id1, id2, score1 + score2)
            addRelation(id2, id1, score1 + score2)
        }
    }

    def addUser(h: History): Unit = {
      userMap.get(h.userId) match {
        case None =>
          val set = mutable.Set.empty[String]
          set += h.glssId
          userMap += (h.userId -> set)
        case Some(s) =>
          s += h.glssId
          userMap(h.userId) = s
      }
    }

    def addItem(h: History): Unit = {
      itemMap.get(h.glssId) match {
        case None =>
          val set = mutable.Set.empty[Int]
          set += h.userId
          itemMap += (h.glssId -> set)
        case Some(s) =>
          s += h.userId
          itemMap(h.glssId) = s
      }
    }

    def filmHistories: List[History] = {
      histories.filter(h => items.get(h.glssId).get.infoTpId == "FL")
    }

    def largerThan(min: Long): mutable.Map[String, mutable.Set[Int]] = {
      itemMap.filter(p => p._1.toLong > min)
    }

    def sortedItems: List[(String, mutable.Set[Int])] = {
      itemMap.toList.sortWith(_._1.toLong < _._1.toLong)
    }

    def estimate(it1: Item, it2: Item): Int = {
      var score = 0
      score += it1.actors.filter(a => a != "-").intersect(it2.actors).size
      score += it1.directors.filter(d => d != "-").intersect(it2.directors).size
      score += it1.genreList.intersect(it2.genreList).size
      score += it1.countryList.intersect(it2.countryList).size
      score
    }

    def addRelation(from: String, to: String, similarity: Int) = {
      relation.get(from) match {
        case None =>
          val net = new VodMap
          net.add(to, similarity)
          relation += (to -> net)
        case Some(n) =>
          n.add(to, similarity)
          relation(from) = n
      }
    }
  }

  def write(): Unit = {
    val resultFile = "predict.csv"
    val writer = new PrintWriter(new File(resultFile))
    val vods = mutable.MutableList.empty[(String, Int)]

    sortedItems foreach {
      tuple =>
        val (userId, itemSet) = tuple
        itemSet foreach {
          item =>
            relation.get(item) match {
              case None =>
              case Some(v) =>
                vods ++= v.top(100)
            }
        }

        top(100) foreach {
          v => writer.write("%d,%s\n".format(userId, v))
        }

        vods.clear()
    }

    def sortedItems: List[(Int, mutable.Set[String])] = {
      userMap.toList.sortWith(_._1 < _._1)
    }

    def top(n: Int): List[String] = {
      vods.sortWith(_._2 > _._2).map(t => t._1).distinct.take(n).toList
    }

    writer.close()
  }
}
