package com.test.app

import org.scalatra._
import org.json4s.{DefaultFormats, Formats}
import org.scalatra.json._


import java.io._

import hex.genmodel.GenModel
import hex.genmodel.easy.{EasyPredictModelWrapper, RowData}
import hex.genmodel.easy.prediction
import hex.genmodel.MojoModel
import hex.genmodel.easy.RowData

class InferenceServer extends ScalatraServlet with JacksonJsonSupport {
  protected implicit val jsonFormats: Formats = DefaultFormats

  before() {
    contentType = formats("json")
  }

  get("/ping") {
    Ok()
  }

  post("/invocations") {
      //println (request.body)
      val s= predict(request.body)
      (s)
    }

  error {
    case e: java.nio.file.NoSuchFileException => "Model not found."
    case e: spray.json.JsonParser.ParsingException => "JSON Parsing Exception : "+(e.toString)
    case e => "Unexpected Exception : "+(e.toString)
  }

  val model_path = "/opt/ml/model/"


  def predict( input: String ) : String = {
    // Load Mojo
    print ("inside predict")
    
    val modelfile = new java.io.File("/opt/ml/model").listFiles.filter(_.getName.endsWith(".zip")).mkString(" ")
    print(modelfile)
    val mojo = MojoModel.load(modelfile)
    //new java.io.File(dirName).listFiles.filter(_.getName.endsWith(".txt"))
    val easyModel = new EasyPredictModelWrapper(mojo)

    // Get Mojo Details
    var features = mojo.getNames.toBuffer

    // Creating the row
    val r = new RowData
    r.put("AGE", "68")
    r.put("RACE", "2")
    r.put("DCAPS", "2")
    r.put("VOL", "0")
    r.put("GLEASON", "6")

    // Performing the Prediction
    val prediction = easyModel.predictBinomial(r).classProbabilities
    print("printing prediction")
    return prediction.mkString(" ")

  }

}


