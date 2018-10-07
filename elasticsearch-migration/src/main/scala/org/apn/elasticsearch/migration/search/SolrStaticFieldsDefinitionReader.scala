package org.apn.elasticsearch.migration.search

import java.io.File
import java.io.IOException

import scala.collection.JavaConversions.iterableAsScalaIterable
import scala.io.Source

import org.json.JSONArray
import org.json.JSONObject
import org.json.XML

import org.apn.elasticsearch.migration.typ.Field

class SolrStaticFieldsDefinitionReader(val file: File) {
  private var solrConfFileJson: JSONObject = _
  initialize()

  def initialize(): Unit = {
    val source = Source.fromFile(file)
    val solrConfig: String = source.getLines.mkString

    solrConfFileJson = XML.toJSONObject(solrConfig)
  }

  @throws(classOf[IOException])
  def readFields() = {
    SolrStaticFieldsDefinitionReader.readFields(readSchema())
  }

  @throws(classOf[IOException])
  def readDynamicFields() = {
    SolrStaticFieldsDefinitionReader.readDynamicFields(readSchema())
  }

  def readSchema() = {
    solrConfFileJson.getJSONObject("schema")
  }
}

object SolrStaticFieldsDefinitionReader {
  def readFields(readSchema: JSONObject) = {
    val fieldsJson: JSONArray =
      readSchema.getJSONObject("fields").getJSONArray("field")
    fieldsJson.map(f => { new Field(f.asInstanceOf[JSONObject]) }).toList
  }

  def readDynamicFields(readSchema: JSONObject) = {
    val fieldsJson: JSONArray =
      readSchema.getJSONObject("fields").getJSONArray("dynamicField")
    fieldsJson.map(f => { new Field(f.asInstanceOf[JSONObject]) }).toList
  }
}