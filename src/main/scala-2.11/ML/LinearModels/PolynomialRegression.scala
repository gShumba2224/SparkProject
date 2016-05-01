package ML.LinearModels

import scala.math
import LA._

class PolynomialRegression {

// observations: rows = observation ; columns = features
  // weights: single row;
  //out: Matrix of weights * observations size
  val getY=(observations:Matrix,weights:Matrix) =>{
    val x = addColumn(observations,List.fill[Double](observations.length)(1.0),0)
    dotProd(x,weights.transpose).transpose
  }

  //estimates: single row
  //trueVal : single row
  //weights : single row
  //out: Double
  val sumSquaresError=(estimates:Matrix,weights:Matrix,trueVal:Matrix)=>{
    val sumErrors = sub(estimates,trueVal).map(row=>row.map
    (value=>math.pow(value,2))).flatten.fold[Double](0){(a,b)=> a+b}
    sumErrors/estimates(0).length
  }

  // observations: rows = observation ; columns = features
  //estimates: single row
  //weights : single row
  //trueVal : single row
  val minimizeError=(observation:Matrix,estimates:Matrix,weights:Matrix,trueVal:Matrix,alpha:Double)=>{
    val errDeriv = sub(estimates,trueVal).flatten.fold[Double](0){(a,b)=>a+b}/observation.length
    val x =Matrix (multRows(1.0+:(observation(0).toArray).toList,errDeriv*alpha))
    sub(weights,x)
  }

  def learn(trainingSet:Matrix,trueVal:Matrix,alpha:Double,maxGen:Int,minError:Double)={
    var weights = Matrix (new Array[Double](trainingSet(0).length+1).map(weight=>
      scala.util.Random.nextDouble()).toList)
    val ogError= sumSquaresError(getY(trainingSet,weights),weights,trueVal)
    var gen = 0
    while (gen < maxGen){
      val estimates = getY(trainingSet,weights)
      val error = sumSquaresError(estimates,weights,trueVal)
      println ("Generation = "+ gen + " Error = "+error + " weights "+weights)
      if (error/ogError > minError) weights=minimizeError(trainingSet,estimates,weights,trueVal,alpha)
      else gen=maxGen+1
      gen +=1
    }
  }
}

object x{
  def main(args: Array[String]) {
    var x = new PolynomialRegression()
    var xs= Matrix (Row(2.0,3.0),Row(4.0,5.0))
    var ws=Matrix(Row(10.0,2.0,5))
    var tru= Matrix(Row(34,48))
    x.learn(xs,tru,0.005,400,0.005)
    println ("ooowwwoooo")
  }
}