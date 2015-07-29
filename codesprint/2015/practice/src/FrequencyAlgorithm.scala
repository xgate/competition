import java.io.Writer

import scala.collection.mutable

/**
 * 가장 많이 구입했던 것 기준으로 정렬하는 알고리즘
 */
class FrequencyAlgorithm extends Algorithm {

  val map = mutable.Map.empty[Int, mutable.Map[String, Int]]

  override def learn(): Unit = {
    histories.foreach {
      h =>
        // 영화만
        if (items.get(h.glssId).get.infoTpId == "FL") {
          map.get(h.userId) match {
            case None =>
              map += (h.userId -> mutable.Map(h.glssId -> 1))
            case Some(m) =>
              m.get(h.glssId) match {
                case None =>
                  m += (h.glssId -> 1)
                case Some(x) =>
                  m(h.glssId) = x + 1
              }
              map(h.userId) = m
          }
        }
    }
  }

  override def write(writer: Writer): Unit = {
    map.toList.sortWith(_._1.toInt < _._1.toInt) foreach {
      tuple =>
        val m = tuple._2
        val list = m.toList.sortWith(_._2 > _._2).take(100)
        list foreach {
          e => writer.write("%s,%s\n".format(tuple._1, e._1))
        }
    }
  }
}
