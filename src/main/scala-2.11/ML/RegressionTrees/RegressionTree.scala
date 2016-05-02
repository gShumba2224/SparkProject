package ML.RegressionTrees

/**
  * Created by gman on 5/1/16.
  */

import ML.RegressionTrees.Node._
import LA._
object RegressionTree {

  def main(args: Array[String]) {
    val p=(x:Row)=>5.0

    val v1 = Node ((x:Row)=>{
      if(x(0)>0.5)true
      else false
    },p,1)

    val p1 = Node ((x:Row)=>{
      if(x(0)>0.5)true
      else false
    },p,-1)

    val v2 = Node ((x:Row)=>{
      if(x(1)>0.5)true
      else false
    },p,2)

    val p2 = Node ((x:Row)=>{
      if(x(0)>0.5)true
      else false
    },p,-2)

    val p3 = Node ((x:Row)=>{
      if(x(0)>0.5)true
      else false
    },p,-3)

    v1.setChildren(p1,v2)
    v2.setChildren(p2,p3)

    println (v1.splitData(Row(0.1,0.2)))
  }

}

