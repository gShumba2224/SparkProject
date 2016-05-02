/**
  * Created by gman on 5/1/16.
  */
import org.apache.spark.{SparkConf,SparkContext}
object CleanData {

  def main(args: Array[String]) {
//    val conf = new SparkConf().setAppName("Clean Data")
//    val sc = new SparkContext(conf)
//    val textFile = "/home/gman/Documents/IntelijWorkspace/SparkStreaming/src/main/resources/Datasets/SourceData/TrainingSet_pp-2015.csv"
//    val txtRDD =sc.textFile(textFile)
//    val x = txtRDD.map(y=>y.split(","))
//    println (x.take(10).toString)
    Array("wow","lol").toBuffer.foreach(println)
  }
}
