import java.io.Writer

import scala.collection.mutable

/**
 * item & feature 간 관계를 이용해서 추천해주는 알고리즘. (0.00996)
 */
class ItemFeatureBasedAlgorithm extends Algorithm {

  // [itemId, [userId]]
  val itemMap = mutable.Map.empty[String, mutable.Set[Int]]
  // [userId, [itemId]]
  val userMap = mutable.Map.empty[Int, mutable.Set[String]]
  // [itemId, Network]
  val relation = mutable.Map.empty[String, Vod]

  override def learn(): Unit = {
    // 구매한 비디오를 저장해둔다.
    histories.filter(h => items.get(h.glssId).get.infoTpId == "FL") foreach {
      h =>
        userMap.get(h.userId) match {
          case None =>
            val set = mutable.Set.empty[String]
            set += h.glssId
            userMap += (h.userId -> set)
          case Some(s) =>
            s += h.glssId
            userMap(h.userId) = s
        }

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

    println("usermap size: " + userMap.size)
    println("itemmap size: " + itemMap.size)

    println("******* make map complete **************")

    itemMap.toList.sortWith(_._1.toLong < _._1.toLong) foreach {
      t1 =>
        val (id1, users1) = t1
        itemMap.filter(p => p._1.toLong > id1.toLong).foreach {
          t2 =>
            val (id2, users2) = t2
            // item 을 구매한 사용자의 일치율이 유사도
            val sim = users1.&(users2).size
            // item 간 공통점이 유사도
            val estimator = new Estimator
            estimator.learn(items.get(id1).get)
            val sim2 = estimator.estimate(items.get(id2).get).toInt

            addRelation(id1, id2, sim + sim2)
            addRelation(id2, id1, sim + sim2)
        }
        println("%s completed...".format(id1))
    }

    println("********* make relation complete *************")
  }

  override def write(writer: Writer): Unit = {
    val videos = mutable.MutableList.empty[(String, Int)]

    userMap.toList.sortWith(_._1 < _._1) foreach {
      u =>
        val (userId, items) = u
        items.foreach {
          i =>
            relation.get(i) match {
              case None =>
              case Some(n) =>
                videos ++= n.sortedList(100)
            }
        }

        videos.sortWith(_._2 > _._2).map(t => t._1).distinct.take(100).foreach {
          v => writer.write("%d,%s\n".format(userId, v))
        }

        videos.clear()
    }
  }

  def addRelation(from: String, to: String, similarity: Int) = {
    relation.get(from) match {
      case None =>
        val net = new Vod
        net.add(to, similarity)
        relation += (to -> net)
      case Some(n) =>
        n.add(to, similarity)
        relation(from) = n
    }
  }
}
