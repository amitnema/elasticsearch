package org.apn.elasticsearch.migration.client

import org.scalatest.BeforeAndAfterAllConfigMap
import org.scalatest.ConfigMap
import org.scalatest.FunSuite
import org.apn.elasticsearch.migration.ArgumentsParser
import scala.io.Source
import org.json.JSONObject

/**
 * @author amit.nema
 */
class EngineMigrationTest extends FunSuite with BeforeAndAfterAllConfigMap {

  //  private val RequiredArgKey = ArgumentsParser.INPUT_FILE

  override def beforeAll(configMap: ConfigMap) = {
    //    require(
    //      configMap.isDefinedAt(RequiredArgKey),
    //      "must place a input file in the configMap under the key: " + RequiredArgKey)
  }

  override def afterAll(configMap: ConfigMap) {
    // After
  }

  test("EngineMigration.run") {
    val inputFile = "target/test-classes/test-data/books-schema.xml";
    val expectedShards = 2;
    val args = Array("-" + ArgumentsParser.INPUT_FILE, inputFile, "-" + ArgumentsParser.SHARDS, expectedShards.toString())
    EngineMigration.run(args)

    //Asserts
    val actualShards = new JSONObject(Source.fromFile(inputFile + ".json").mkString).getJSONObject("settings").getJSONObject("index").getInt("number_of_shards")
    assert(expectedShards === actualShards)
  }
}