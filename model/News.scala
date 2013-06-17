package model

case class News(id: Option[Long],comments: Boolean,content: Option[String],date: Option[Timestamp],title: Option[String])

object Newss extends Newss

//GENERATED - News
//case class News(id: Option[Long],comments: Boolean,content: Option[String],date: Option[Timestamp],title: Option[String])

class Newss extends Table[News]("News") with Cruded[News] {
    def id = column[Long]("id", O.NotNull ,O.PrimaryKey, O.AutoInc)
    def comments = column[Boolean]("comments")
    def content = column[String]("content")
    def date = column[Timestamp]("date")
    def title = column[String]("title")

    def * = id.? ~ comments ~ content.? ~ date.? ~ title.? <> (News, News.unapply _)
    def autoInc = comments ~ content.? ~ date.? ~ title.? returning id
    def insert(o: News ) = autoInc.insert( o.comments, o.content, o.date, o.title)
    def all() = Query(Newss).list
}

// END - News
