package org.apn.elasticsearch.spark.client

import org.apache.spark.sql.catalyst.ScalaReflection
import org.apache.spark.sql.types.StructType
import org.apache.spark.sql.{Encoder, SparkSession}
import org.elasticsearch.spark.rdd.EsSpark

import scala.reflect.runtime.universe.TypeTag
/**
 * @author amit.nema
 *
 */
class ESIndexData[T: Encoder: TypeTag](spark: SparkSession) {
  /**
   * @param path : path to csv file
   * @param delimiter : field separator
   * @param indexType : index & type in the format as indexName/type (e.g. library/books, library/cds)
   */
  def saveCsvToEs(path: String, delimiter: String, indexType: String) {
    //  create rdd
    val typedRdd = spark.read
      .option("inferSchema", true)
      .option("delimiter", delimiter)
      .schema(caseClassToSchema[T])
      .csv(path).as[T].rdd
    EsSpark.saveToEs(typedRdd, indexType)
  }

  def caseClassToSchema[T: TypeTag] = ScalaReflection.schemaFor[T].dataType.asInstanceOf[StructType]
}