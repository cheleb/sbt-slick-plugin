package net.orcades.db

import net.orcades.db.RDBMSMetadataExtractor._
import java.nio.file.Files

import sbt._

import java.sql.Connection

case class Entity(name: String, tableName: String, members: List[Member], dao: String)

case class Member(name: String, dataType: String, pk: Boolean, nullable: Boolean, autoInc: Boolean) {
  def option = nullable || autoInc
}

object EntitiesMapper extends App {

  def buildAll(implicit conn: Connection, outputDir: File) {
    println("Build all")

    allTables.foreach(buildEntity)
  }

  def buildEntity(table: Table)(implicit outputDir: File) {
    implicit val builder = new StringBuilder()
    val outputFile = outputDir / (table.name + ".scala")
    val exists = Files.exists(outputFile.toPath)

    def caseMember(member: Column): String = {
      val name = member.name
      if (member.nullable || member.autoInc)
        name + ": Option[" + member.dataType + "]"
      else
        name + ": " + member.dataType
    }

    val caseMembers = table.columns.map(caseMember(_)).mkString(",")
    val name = entityName(table.name)
    val caseClass = "case class " + name + "(" + caseMembers + ")"

    if (exists) {
      IO.readLines(outputFile).takeWhile(line => !line.startsWith("//GENERATED")).foreach(line => builder ++= line + "\n")
    } else {
      builder ++= """package models

import scala.slick.driver.BasicDriver.simple._
import Database.threadLocalSession

                  """
      if (table.columns.exists(_.dataType == "Timestamp")) {
        builder ++= """
import java.sql.Timestamp
import java.util.Calendar

                    """
      }

      builder ++= caseClass + "\n\n"

      val daoName = getDaoName(table.name)
      builder ++= "object " + daoName + " extends " + daoName + "\n\n"

    }
    builder ++= "//GENERATED - " + table.name + "\n"
    builder ++= "//" + caseClass + "\n\n"
    buildDAOClass(table)
    builder += '\n'
    builder ++= "// END - " + table.name + "\n"

    Files.createDirectories(outputFile.toPath.getParent)

    IO.write(outputFile, builder.toString)

  }

  def buildDAOClass(table: Table)(implicit builder: StringBuilder) {
    val daoName = getDaoName(table.name)


    def buildColumnMapping(column: Column) {

      val mapping = if (column.pk)
        "def " + column.name + " = column[" + column.dataType + "](\042" + column.name + "\042, O.NotNull ,O.PrimaryKey, O.AutoInc)"
      else
        "def " + column.name + " = column[" + column.dataType + "](\042" + column.name + "\042)"

      builder ++= "    " + mapping + "\n"
    }

    builder ++= "class " + daoName + " extends Table[" + table.name + "](\042" + table.tableName + "\042) with Cruded[" + table.name + "] {\n" //  $tableMembers\n  def * = $star <> ($name, ${name}.unapply _)\n")

    table.columns.foreach(buildColumnMapping)

    builder += '\n'

    val star = table.columns.map(m => if (m.option) m.name + ".?" else m.name).mkString(" ~ ")
    val autoInc = table.columns.filterNot(p => p.pk).map(m => if (m.option) m.name + ".?" else m.name).mkString(" ~ ") + " returning " + table.columns.filter(p => p.pk).map(p => p.name).mkString(", ")

    val name = entityName(table.name)

    table.fks.foreach(foreignKey)

    builder += '\n'

    builder ++= "    def * = " + star + " <> (" + name + ", " + name + ".unapply _)\n"

    builder ++= "    def autoInc = " + autoInc + "\n"

    builder ++= "    def insert(o: " + name + ") = autoInc.insert( " + table.columns.filterNot(p => p.pk).map(m => "o." + m.name).mkString(", ") + ")\n"

    builder ++= "    def all() = Query(" + daoName + ").list\n"

    builder ++= "}\n"

    def foreignKey(fk: FK)(implicit builder: StringBuilder) {
      val fkName = if (fk.keys.size == 1 && fk.keys.head._1.endsWith("_id"))
        fk.keys.head._1.substring(0, fk.keys.head._1.lastIndexOf("_id"))
      else
        fk.name

      builder ++= "    def " + fkName + " = foreignKey(\"" + fk.name + "\", " + fk.keys.head._1 + ", " + ucFirst(getDaoName(fk.keys.head._2._1)) + ")(_." + fk.keys.head._2._2 + ")\n"
    }
  }

  def entityName(tableName: String) = {
    tableName.substring(0, 1).toUpperCase + tableName.substring(1)
  }

  def ucFirst(tableName: String) = {
    tableName.substring(0, 1).toUpperCase + tableName.substring(1)
  }


  private def getDaoName(tableName: String): String = {
    if (tableName.charAt(tableName.length() - 1) == 'y')
      tableName.substring(0, tableName.length() - 2) + "ies"
    else
      tableName + "s"
  }
}