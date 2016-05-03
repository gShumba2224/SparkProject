/**
  * Created by gman on 5/1/16.
  */

import org.apache.spark.rdd.RDD
import org.apache.spark.sql.SaveMode
import org.apache.spark.sql.types._
import org.apache.spark.{SparkConf,SparkContext}
import java.text.SimpleDateFormat
import Utilis.Geocoder

object CleanData {

  val schema:StructType = StructType(List(
    StructField("Id",StringType,false),
    StructField("Price",DoubleType,false),
    StructField("Transfer_Date",DateType,false),
    StructField("PropertyType",IntegerType,false),
    StructField("OldNew",IntegerType,false),
    StructField("Duration",IntegerType,false),
    StructField("Latitude",DoubleType,false),
    StructField("Longitude",DoubleType,false),
    StructField("Transaction",IntegerType,false)
  ))

  def transformCategorical (field : String): String = field match{
      // PROPERTY TYPE
    case "propertyD" => "0"
    case "propertyS" => "1"
    case "propertyT" => "2"
    case "propertyF" => "3"
    case "propertyO" => "4"
    case "property0" => "4"
      // OLD / NEW
    case "oldNewN" => "0"
    case "oldNewY" => "1"
      //DURATION
    case "durationF" => "0"
    case "durationL" => "1"
      // TRANSACTION TYPE
    case "transactionA" => "0"
    case "transactionB" => "1"

  }

  def clean(rdd:RDD[String]):RDD[org.apache.spark.sql.Row]= {
    val stringToArray = rdd.map(line=>line.split(",").toList)
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
      buffer(14)=transformCategorical("transaction"+buffer(14).toUpperCase.charAt(0).toString)
      buffer.toList
    })

    val geoCodeAddress = fixCategorical.map(fields=>{
      var geoCode = Geocoder.getGeocode(fields(11),fields(12),fields(13))
      if (geoCode.latitude.length==0 || geoCode.longitude.length==0) "NULL" :: "NULL" ::fields
      else geoCode.latitude :: geoCode.longitude :: fields
    })

    val parseFields = geoCodeAddress.map(fields=>{

      //ParseDate
      val formatter = new SimpleDateFormat("yyyy-MM-dd")
      val transferDateLong = formatter.parse(fields(4)).getTime
      val transferDate = new java.sql.Date(transferDateLong)
      //Parse Other
      val price = fields(3).toDouble
      val lat = fields(0).toDouble
      val lon = fields (1).toDouble
      val id = fields (2).substring(1,fields(2).length-1)
      val propType = fields (6).toInt
      val oldNew = fields(7).toInt
      val duration = fields(8).toInt
      val transaction = fields (16).toInt
      org.apache.spark.sql.Row(id,price,transferDate,propType,oldNew,duration,lat,lon,transaction)
    })
    parseFields
  }


  def main(args: Array[String]): Unit = {
    val conf = new SparkConf().setAppName("Clean Data")
    val sc = new SparkContext(conf)
    val sqlContext = new org.apache.spark.sql.SQLContext(sc)

    val inputCSV = "/home/gman/Documents/IntelijWorkspace/SparkStreaming/src/main/resources/Datasets/SourceData/dummy.csv"
    val outputJSON = "/home/gman/Documents/IntelijWorkspace/SparkStreaming/src/main/resources/Datasets/SourceData/dummy.json"
    val txtRDD =sc.textFile(inputCSV)
    val houses = sqlContext.createDataFrame(clean(txtRDD),schema)
    houses.registerTempTable("Houses")
    sqlContext.sql("SELECT * FROM Houses").write.mode(SaveMode.Append).json(outputJSON)
  }
}



