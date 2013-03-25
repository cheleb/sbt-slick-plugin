package models

import scala.slick.session.Database
import scala.slick.driver.BasicDriver.Implicit._
import Database.threadLocalSession

import scala.slick.session.Session

import java.sql.ResultSet
import java.sql.Types
import scala.collection.mutable.MutableList

case class Entity(name: String, tableName: String, members: List[Member], dao: String)
case class Member(name: String, dataType: String, pk: Boolean, nullable: Boolean, autoInc: Boolean) {
  def option = nullable || autoInc
}

object EntitiesMapper extends App {
  Database.forURL("jdbc:postgresql:jug", "test", "test", driver = "org.postgresql.Driver") withSession {
//    println("Speakers:")
//    val v = Users.all
//    println(v)
//
    dump()

  }

  def dump()(implicit session: Session) = {

    val metadata = session.metaData
    val tables = metadata.getTables(session.conn.getCatalog(), "public", null, Array("TABLE"))
    val entities = MutableList[Entity]()
    while (tables.next()) {

      val entity = dumpTable(tables.getString("table_name"))
      entity map { e => entities += e }

      // dumpResultSet(tables)
    }
    entities.foreach(writeEntity(_))

    def entityDaoName(entityName: String) = {

      if (entityName.charAt(entityName.length() - 1) == 'y')
        entityName.substring(0, entityName.length() - 2) + "ies"
      else
        entityName + "s"
    }

    def writeEntity(entity: Entity) = {
      val name = entity.name
      val daoName = entityDaoName(name)

      def caseMember(member: Member): String = {
        val name = member.name
        if (member.nullable || member.autoInc)
          s"$name: Option[${member.dataType}]"
        else
          s"$name: ${member.dataType}"
      }

      val caseMembers = entity.members.map(caseMember(_)).mkString(",")
      println(s"case class $name($caseMembers)")

      def tableMember(member: Member): String = {
        val name = member.name
        if(member.pk)
          s"def $name = column[${member.dataType}](\042$name\042, O.PrimaryKey)"
        else
        s"def $name = column[${member.dataType}](\042$name\042)"
      }

      val tableMembers = entity.members.map(tableMember(_)).mkString("\n  ")
      val star = entity.members.map(m => if( m.option) m.name + ".?" else m.name).mkString(" ~ ")
      println
      println(s"object $daoName extends Table[$name](\042${entity.tableName}\042){\n  $tableMembers\n  def * = $star <> ($name, ${name}.unapply _)\n")
      
      println(s"  def all() = Query($daoName).list")
      
      println("}")

    }

    def entityName(tableName: String) = {
      tableName.substring(0, 1).toUpperCase() + tableName.substring(1)
    }

    def columnType(t: Int) = {
      t match {
        case Types.INTEGER => "Int"
        case Types.BIGINT => "Long"
        case Types.VARCHAR => "String"
        case Types.CLOB => "String"
        case Types.BIT => "Boolean"
        case Types.BOOLEAN => "Boolean"
        case Types.TIMESTAMP => "Timestamp"
        case Types.DATE => "Calendar"
        case Types.CHAR => "Char"
      }
    }

    def dumpTable(tableName: String): Option[Entity] = {

      def getPks() : Set[String] = {
        val rs = metadata.getPrimaryKeys(session.conn.getCatalog(), "public", tableName);
        val set = MutableList[String]()
        while(rs.next()) {
          set += rs.getString("COLUMN_NAME")
        }
        set.toSet
      }
      
      val members = MutableList[Member]()
      
      val pks = getPks()
      
      val columns = metadata.getColumns(session.conn.getCatalog(), "public", tableName, null);
      while (columns.next()) {
        val columnName = columns.getString("COLUMN_NAME")
        members += Member(columnName, columnType(columns.getInt("DATA_TYPE")), pks.contains(columnName), columns.getBoolean("NULLABLE"), columns.getString("IS_AUTOINCREMENT")=="YES")
      }

      Some(Entity(entityName(tableName), tableName, members.toList, entityDaoName(tableName)))

    }

    def dumpResultSet(rs: ResultSet) = {

      for (i <- 1 to rs.getMetaData().getColumnCount()) {

        val nomColonne = rs.getMetaData().getColumnName(i);
        val valeurColonne = rs.getObject(i);
        System.out.println(nomColonne + " = " + valeurColonne);

      }
    }
  }
}