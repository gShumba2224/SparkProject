package ML.RegressionTrees

/**
  * Created by gman on 5/1/16.
  */

import LA._
class Node (splitCond:(Row)=>Boolean,predict:(Row)=>Double,id:Int){

  private var leftChild:Node = null
  private var rightChild:Node = null

  def setChildren (left:Node,right:Node)={
    this.leftChild=left
    this.rightChild=right
  }

  val getLeftChild = this.leftChild
  val getRightChild = this.rightChild

  def splitData (data:Row):Option[Double]={
    println (id+" _____ "+data)
    if (leftChild!=null && rightChild != null){
      if (splitCond(data)==true)leftChild.splitData(data)
      else rightChild.splitData(data)
    }else Some(predict(data))
  }
}

object Node{
  def apply (splitCond:(Row)=>Boolean,predict:(Row)=>Double,id:Int):Node={
    new Node (splitCond,predict,id)
  }

}
