package model

case class Event(id: Option[Long],capacity: Int,date: Option[Timestamp],description: Option[String],location: Option[String],map: Option[String],open: Boolean,registrationurl: Option[String],report: Option[String],title: Option[String],partner_id: Option[Long])

object Events extends Events

//GENERATED - Event
//case class Event(id: Option[Long],capacity: Int,date: Option[Timestamp],description: Option[String],location: Option[String],map: Option[String],open: Boolean,registrationurl: Option[String],report: Option[String],title: Option[String],partner_id: Option[Long])

class Events extends Table[Event]("Event") with Cruded[Event] {
    def id = column[Long]("id", O.NotNull ,O.PrimaryKey, O.AutoInc)
    def capacity = column[Int]("capacity")
    def date = column[Timestamp]("date")
    def description = column[String]("description")
    def location = column[String]("location")
    def map = column[String]("map")
    def open = column[Boolean]("open")
    def registrationurl = column[String]("registrationurl")
    def report = column[String]("report")
    def title = column[String]("title")
    def partner_id = column[Long]("partner_id")

    def * = id.? ~ capacity ~ date.? ~ description.? ~ location.? ~ map.? ~ open ~ registrationurl.? ~ report.? ~ title.? ~ partner_id.? <> (Event, Event.unapply _)
    def autoInc = capacity ~ date.? ~ description.? ~ location.? ~ map.? ~ open ~ registrationurl.? ~ report.? ~ title.? ~ partner_id.? returning id
    def insert(o: Event ) = autoInc.insert( o.capacity, o.date, o.description, o.location, o.map, o.open, o.registrationurl, o.report, o.title, o.partner_id)
    def all() = Query(Events).list
}

// END - Event
