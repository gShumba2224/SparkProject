/**
  * Created by gman on 5/1/16.
  */
import org.apache.spark.{SparkConf,SparkContext}

import scala.collection.mutable
import scala.collection.mutable.ListBuffer
import Utilis.Geocoder

object CleanData {

  def transformCategorical (field : String): String = field match{
      // PROPERTY TYPE
    case "propertyD" => "0"
    case "propertyS" => "1"
    case "propertyT" => "2"
    case "propertyF" => "3"
    case "propertyO" => "4"
    case "property0" => "4"
      // OLD / NEW
    case "oldNewN" => "0new"
    case "oldNewY" => "1old"
      //DURATION
    case "durationF" => "0dur"
    case "durationL" => "1dur"
      // TRANSACTION TYPE
    case "transactionA" => "0trans"
    case "transactionB" => "1trans"

  }

  def main(args: Array[String]) {

    val conf = new SparkConf().setAppName("Clean Data")
    val sc = new SparkContext(conf)
    val textFile = "/home/gman/Documents/IntelijWorkspace/SparkStreaming/src/main/resources/Datasets/SourceData/TrainingSet_pp-2015.csv"
    val txtRDD =sc.textFile(textFile)

    val stringToArray = txtRDD.map(line=>line.split(",").toList)
    val removeParenthesis = stringToArray.map(fields=>{
      val buffer = fields.toBuffer
      for(i<-0 until buffer.length){
        var newVal =buffer(i).replace('"',' ')
        if(newVal==" "||newVal=="  ")newVal="NULL" else newVal=newVal.trim
        buffer(i)=newVal
      }
      buffer.toList
    })

    val fixCategorical = removeParenthesis.filter(fields=>if(fields.length==16)true; else false).map(fields=>{
      val buffer = fields.toBuffer
      buffer(4)=transformCategorical("property"+buffer(4).toUpperCase.charAt(0).toString)
      buffer(5)=transformCategorical("oldNew"+buffer(5).toUpperCase.charAt(0).toString)
      buffer(6)=transformCategorical("duration"+buffer(6).toUpperCase.charAt(0).toString)
     // buffer(14)=transformCategorical("transaction"+buffer(14).toUpperCase.charAt(0).toString)
      buffer.toList
    })

    val geoCodeAddress = fixCategorical.map(fields=>{
      var geoCode = Geocoder.getGeocode(fields(11),fields(12),fields(13))
      if (geoCode._1.length==0 || geoCode._2.length==0) "NULL" :: "NULL" ::fields
      else geoCode._1 :: geoCode._2 :: fields
    })

    val deleteFields = fixCategorical.map(fields=>{
    })

    geoCodeAddress.collect().foreach(println)
  }
}


