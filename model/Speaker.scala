package model

case class Speaker(id: Option[Long],activity: Option[String],compan: Option[String],description: Option[String],fullname: Option[String],jugmember: Option[Boolean],memberfct: Option[String],photourl: Option[String],url: Option[String],email: Option[String],personalurl: Option[String])

object Speakers extends Speakers

//GENERATED - Speaker
//case class Speaker(id: Option[Long],activity: Option[String],compan: Option[String],description: Option[String],fullname: Option[String],jugmember: Option[Boolean],memberfct: Option[String],photourl: Option[String],url: Option[String],email: Option[String],personalurl: Option[String])

class Speakers extends Table[Speaker]("Speaker") with Cruded[Speaker] {
    def id = column[Long]("id", O.NotNull ,O.PrimaryKey, O.AutoInc)
    def activity = column[String]("activity")
    def compan = column[String]("compan")
    def description = column[String]("description")
    def fullname = column[String]("fullname")
    def jugmember = column[Boolean]("jugmember")
    def memberfct = column[String]("memberfct")
    def photourl = column[String]("photourl")
    def url = column[String]("url")
    def email = column[String]("email")
    def personalurl = column[String]("personalurl")

    def * = id.? ~ activity.? ~ compan.? ~ description.? ~ fullname.? ~ jugmember.? ~ memberfct.? ~ photourl.? ~ url.? ~ email.? ~ personalurl.? <> (Speaker, Speaker.unapply _)
    def autoInc = activity.? ~ compan.? ~ description.? ~ fullname.? ~ jugmember.? ~ memberfct.? ~ photourl.? ~ url.? ~ email.? ~ personalurl.? returning id
    def insert(o: Speaker ) = autoInc.insert( o.activity, o.compan, o.description, o.fullname, o.jugmember, o.memberfct, o.photourl, o.url, o.email, o.personalurl)
    def all() = Query(Speakers).list
}

// END - Speaker
