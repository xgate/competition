
case class Item(glssId: String,
                serisId: String,
                infoTpId: String,
                category: String,
                productionYear: String,
                openDate: String,
                serisTm: String,
                directors: Array[String],
                actors: Array[String],
                genreList: Array[String],
                countryList: Array[String],
                lvlCd: String,
                crttm: String)

object Item {

  def apply(tokens: Array[String]) =
    new Item(
      tokens(0),
      tokens(1),
      tokens(2),
      tokens(3),
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
  def generate(file: String): Map[String, Item] = {
    val lines = scala.io.Source.fromFile(file, "utf8").getLines()
    val items = lines map {
      s =>
        val tokens = s.split("\t")
        val item = Item(tokens)
        item.glssId -> item
    }
    items.toMap
  }
}
