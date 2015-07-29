import java.io.Writer

import scala.collection.mutable

/**
 * feature별로 공통점이 있는지 체크해서 순위를 매기는 알고리즘
 */
class FeatureBasedAlgorithm extends Algorithm {

  val map = mutable.Map.empty[Int, Estimator]

  override def learn(): Unit = {
    histories foreach {
      h =>
        val item = items.get(h.glssId).get
        map.get(h.userId) match {
          case None =>
            val s = new Estimator
            s.learn(item, 0.0)
            map += (h.userId -> s)
          case Some(s) =>
            s.learn(item, 0.0)
            map(h.userId) = s
        }
    }
  }

  override def write(writer: Writer): Unit = {
    map.toList.sortWith(_._1.toInt < _._1.toInt) foreach {
      tuple =>
        val (userId, suggest) = tuple
        items.filter(i => i._2.infoTpId == "FL").map {
          tuple =>
            val (itemId, item) = tuple
            (userId, itemId, suggest.estimate(item))
        }.toList.filter(t => t._3 > 0.0).sortWith(_._3 > _._3).take(100).foreach {
          item => writer.write("%s,%s\n".format(item._1, item._2))
        }
    }
  }
}
