package model

case class User(id: Option[Long],email: Option[String])

object Users extends Users

//GENERATED - User
//case class User(id: Option[Long],email: Option[String])

class Users extends Table[User]("User") with Cruded[User] {
    def id = column[Long]("id", O.NotNull ,O.PrimaryKey, O.AutoInc)
    def email = column[String]("email")

    def * = id.? ~ email.? <> (User, User.unapply _)
    def autoInc = email.? returning id
    def insert(o: User ) = autoInc.insert( o.email)
    def all() = Query(Users).list
}

// END - User
