
case class Item(glssId: String,
                serisId: String,
                infoTpId: String,
                category: String,
                title: String,
                synopsis: String,
                productionYear: String,
                openDt: String,
                serisTm: String,
                directors: Array[String],
                actors: Array[String],
                genreNmList: Array[String],
                cntryNmList: Array[String],
                lvlCd: String,
                crttm: String) {

  def mkString: String = {
    fmt("glssId", glssId) +
      fmt("serisId", serisId) +
      fmt("infoTpId", infoTpId) +
      fmt("category", category) +
      fmt("title", title) +
      fmt("synopsis", synopsis) +
      fmt("productionYear", productionYear) +
      fmt("openDt", openDt) +
      fmt("serisTm", serisTm) +
      fmt("directors", directors.mkString(",")) +
      fmt("actors", actors.mkString(",")) +
      fmt("genres", genreNmList.mkString(",")) +
      fmt("contries", cntryNmList.mkString(",")) +
      fmt("lvlCd", lvlCd) +
      fmt("crttm", crttm)
  }

  def fmt(name: String, value: String): String = {
    name + "=" + value + ","
  }
}

object Item {

  def apply(tokens: Array[String]) =
    new Item(tokens(0),
      tokens(1),
      tokens(2),
      tokens(3),
      tokens(4),
      tokens(5),
      tokens(6),
      tokens(7),
      tokens(8),
      tokens(9).split(","),
      tokens(10).split(","),
      tokens(11).split(","),
      tokens(12).split(","),
      tokens(13),
      tokens(14))
}

object ItemGenerator {
  def generate(): Map[String, Item] = {
    val fileName = "itemInfo.tsv"
    val lines = scala.io.Source.fromFile(fileName, "utf8").getLines()

    val items = lines map {
      s =>
        val tokens = s.split("\t")
        val item = Item(tokens)
        item.glssId -> item
    }
    items.toMap
  }
}
