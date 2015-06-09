import java.io._

import scala.util.parsing.combinator.RegexParsers
import org.json4s._

object Bencode extends RegexParsers {

  sealed trait BElem {
    def toJson: JValue
  }

  case class BInt(value: Int) extends BElem {
    def toJson: JValue = JInt(value)
  }

  case class BString(value: String) extends BElem {
    def toJson: JValue = JString(value)
  }

  case class BList(elems: Seq[BElem]) extends BElem {
    def toJson: JValue = JArray(elems.map(_.toJson).toList)
  }

  case class BDict(elems: Map[String, BElem]) extends BElem {
    def toJson: JValue = JObject(elems.map { case (key, value) => JField(key, value.toJson) }.toList)
  }


  /** Non-zero digits */
  def nonZero: Parser[Int] = """([1-9]+)""".r ^^ (_.toInt)

  /** Digits, no trailing zeroes, possibly 0 */
  def digits: Parser[Int] = """(0|[1-9][0-9]*)""".r ^^ (_.toInt)


  /** Bencoded integer
    *
    * From wikipedia :
    *
    * An integer is encoded as i<integer encoded in base ten ASCII>e. Leading
    * zeros are not allowed (although the number zero is still represented as
    * "0"). Negative values are encoded by prefixing the number with a minus
    * sign. The number 42 would thus be encoded as i42e, 0 as i0e, and -42 as
    * i-42e. Negative zero is not permitted.
    *
    */
  def integer: Parser[BInt] = "i" ~> (("-" ~> nonZero) ^^ (-_) | digits) <~ "e" ^^ (BInt(_))


  /** Bencoded byte string
    *
    * From wikipedia :
    *
    * A byte string (a sequence of bytes, not necessarily characters) is
    * encoded as <length>:<contents>. The length is encoded in base 10, like
    * integers, but must be non-negative (zero is allowed); the contents are
    * just the bytes that make up the string. The string "spam" would be
    * encoded as 4:spam. The specification does not deal with encoding of
    * characters outside the ASCII set; to mitigate this, some BitTorrent
    * applications explicitly communicate the encoding (most commonly UTF-8)
    * in various non-standard ways. This is identical to how netstrings work,
    * except that netstrings additionally append a comma suffix after the
    * byte sequence.
    *
    */
  def string: Parser[BString] = {

    def dotAll: Parser[Char] = elem("any char", _ => true)

    (digits <~ ":") >> { x => repN(x, dotAll) ^^ (_.mkString) } ^^ (BString(_))
  }


  /** Bencoded list
    *
    * From wikipedia :
    *
    * A list of values is encoded as l<contents>e . The contents consist of
    * the bencoded elements of the list, in order, concatenated. A list
    * consisting of the string "spam" and the number 42 would be encoded as:
    * l4:spami42ee. Note the absence of separators between elements.
    */
  def list: Parser[BList] = "l" ~> rep(elem) <~ "e" ^^ (BList(_))


  /** Bencoded dictionary
    *
    * From wikipedia :
    *
    * A dictionary is encoded as d<contents>e. The elements of the dictionary
    * are encoded each key immediately followed by its value. All keys must
    * be byte strings and must appear in lexicographical order. A dictionary
    * that associates the values 42 and "spam" with the keys "foo" and "bar",
    * respectively (in other words, {"bar": "spam", "foo": 42}}), would be
    * encoded as follows: d3:bar4:spam3:fooi42ee. (This might be easier to
    * read by inserting some spaces: d 3:bar 4:spam 3:foo i42e e.)
    */
  def map: Parser[BDict] = {

    def entry: Parser[(String, BElem)] = string ~ elem ^^ { case key ~ value => key.value -> value }

    "d" ~> rep(entry) <~ "e" ^^ (elems => BDict(Map(elems: _*)))
  }


  def elem: Parser[BElem] = integer | string | list | map

  def getReader(file: String) = {
    val fis = new FileInputStream(file)
    new InputStreamReader(fis, "US-ASCII");
  }

  def parseFile(file: String): BElem =
    parseAll(elem, getReader(file)) match {
      case Success(result, _) => result
      case failure: NoSuccess => BString(failure.msg)
    }


}

