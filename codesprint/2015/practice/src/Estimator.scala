import scala.collection.mutable

class Estimator {
  val infoTpFeature = new InfoTpFeature
  val categoryFeature = new CategoryFeature
  val directorFeature = new DirectorFeature
  val actorFeature = new ActorFeature
  val genreFeature = new GenreFeature
  val countryFeature = new CountryFeature
  val levelFeature = new LevelFeature

  def mkString: String = {
    "[infoTpFeature]: \n" + infoTpFeature.mkString +
    "[categoryFeature] \n" + categoryFeature.mkString +
    "[directorFeature] \n" + directorFeature.mkString +
    "[actorFeature] \n" + actorFeature.mkString +
    "[genreFeature] \n" + genreFeature.mkString +
    "[countryFeature] \n" + countryFeature.mkString +
    "[levelFeature] \n" + levelFeature.mkString
  }

  def learn(item: Item, tip: Double = 0.0) = {
    infoTpFeature.add(item.infoTpId, tip)
    categoryFeature.add(item.category, tip)
    levelFeature.add(item.lvlCd, tip)

    addActors(item.actors, tip)
    addDirectors(item.directors, tip)
    addCountries(item.cntryNmList, tip)
    addGenres(item.genreNmList, tip)
  }

  def addActors(actors: Array[String], tip: Double) = {
    actors.foreach(a => actorFeature.add(a, tip))
  }
  def addDirectors(dirs: Array[String], tip: Double) = {
    dirs.foreach(d => actorFeature.add(d, tip))
  }
  def addGenres(gs: Array[String], tip: Double) = {
    gs.foreach(g => genreFeature.add(g, tip))
  }
  def addCountries(cs: Array[String], tip: Double) = {
    cs.foreach(c => countryFeature.add(c, tip))
  }

  def estimate(item: Item): Double = {
    var score = 0.0

    score += infoTpFeature.getScore(item.infoTpId)
    score += categoryFeature.getScore(item.category)
    score += levelFeature.getScore(item.lvlCd)

    item.actors foreach {
      a => score += actorFeature.getScore(a)
    }
    item.cntryNmList foreach {
      c => score += countryFeature.getScore(c)
    }
    item.genreNmList foreach {
      g => score += genreFeature.getScore(g)
    }
    item.directors foreach {
      d => score += directorFeature.getScore(d)
    }

    score
  }
}

abstract class Feature(weight: Double) {

  val map = mutable.Map.empty[String, Double]

  def add(name: String, tip: Double = 0.0): Unit = {
    if (name.nonEmpty && name != "-") {
      map.get(name) match {
        case None =>
          map += (name -> (1.0 + tip))
        case Some(x) =>
          map(name) = x + (1.0 + tip)
      }
    }
  }

  def getScore(name: String): Double = {
    map.get(name) match {
      case None => 0.0
      case Some(v) => v * weight
    }
  }

  def mkString: String = {
    var str = ""
    map foreach {
      tuple =>
        str += s"[key:${tuple._1},value:${tuple._2}]\n"
    }
    str
  }
}

class InfoTpFeature extends Feature(0.0)

class CategoryFeature extends Feature(0.0)

class DirectorFeature extends Feature(1.0)

class ActorFeature extends Feature(1.0)

class GenreFeature extends Feature(1.0)

class CountryFeature extends Feature(1.0)

class LevelFeature extends Feature(0.0)
