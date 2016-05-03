import java.util.Date

/**
  * Created by gman on 4/27/16.
  */
package object LA {
  type Row = List[Double]
  type Matrix=List[Row]
  val unEvenDimensions=(message:String) => new IllegalArgumentException (message)

  def Row(values:Double*)=values.toList
  def Matrix(rows:Row*)={
    rows.foreach(row=>{
      if (row.length != rows(0).length) {
        throw unEvenDimensions("ERROR CREATING MATRIX: some rows contain more values than others")
      }
    })
    rows.toList
  }

  //Row Operations
  def addRows (row1:Row,row2:Row):Row ={
    isEqualSize(row1,row2,"add")
    row1.zip(row2).map(pair=>{pair._1+pair._2})
  }

  def addRows (row1:Row,scalar:Double):Row = row1.map(value=>value+scalar)

  def minusRows (row1:Row,row2:Row):Row = {
    isEqualSize(row1,row2,"minus")
    row1.zip(row2).map(pair=>{pair._1-pair._2})
  }

  def minusRows (row1:Row,scalar:Double):Row = row1.map(value=>value-scalar)

  def minusRows (scalar:Double,row1:Row):Row = row1.map(value=>scalar-value)

  def multRows (row1:Row,row2:Row):Row = {
    isEqualSize(row1,row2," multip ")
    row1.zip(row2).map(pair=>{pair._1*pair._2})
  }

  def multRows (row1:Row,scalar:Double):Row = row1.map(value=>scalar*value)

  def dotProdRows(row1:Row,row2:Row):Double={
    isEqualSize(row1,row2," dotProd ")
    row1.zip(row2).map(pair=>{pair._1*pair._2}).fold[Double](0){(val1,val2)=>val1+val2}
  }

  def isEqualSize (row1:Row,row2:Row,opType:String)= {
    if(row1.length != row2.length) {
      throw unEvenDimensions ("ERROR UNEVEN ROWS: ROW 1=> "+row1 + " is incompartable for "+opType+" with ROW2 => "+row2)
    }
  }


  //**************************Matrix Operations************************

  def dotProd(matrix1:Matrix,matrix2: Matrix): Matrix = {

    var rowNUm= -1
    (new Array[Row](matrix1.length)).map(row=>{
      rowNUm+=1
      var colNum= -1
      new Array[Double](matrix2(0).length).map(value=>{
        colNum+=1
        dotProdRows(matrix1(rowNUm),getColumn(matrix2,colNum))
      }).toList
    }).toList
  }

  def add (matrix1: Matrix,matrix2: Matrix):Matrix= matrixMathOp(matrix1,matrix2,1.0,"+")
  def add (matrix1: Matrix,scalar: Double):Matrix= matrixMathOp(matrix1,null,scalar,"+")

  def sub (matrix1: Matrix,matrix2: Matrix):Matrix= matrixMathOp(matrix1,matrix2,1.0,"-")
  def sub (matrix1: Matrix,scalar: Double):Matrix= matrixMathOp(matrix1,null,scalar,"-")
  def sub (scalar: Double,matrix1: Matrix):Matrix= matrixMathOp(matrix1,null,scalar,"-2")

  def prod (matrix1: Matrix,matrix2: Matrix):Matrix= matrixMathOp(matrix1,matrix2,1.0,"*")
  def prod (matrix1: Matrix,scalar: Double):Matrix= matrixMathOp(matrix1,null,scalar,"*")

  //process all rows in matrix
  def matrixMathOp (matrix1:Matrix,matrix2: Matrix, scalar:Double,op:String):Matrix={
    var count = -1
    var result = new Array[Row](matrix1.length)
    matrix1.foreach(row=>{
      count+=1
      if (matrix2!= null) result(count)=matchMatrixOp(op,row,matrix2(count))
      else result(count)=matchScalarMatrixOp(op,row,scalar)
    })
    result.toList
  }

  def matchMatrixOp(op:String,row1:Row,row2:Row):Row = op match {
    case "+"=> addRows(row1,row2)
    case "-"=> minusRows(row1,row2)
    case "*"=> multRows(row1,row2)
  }

  def matchScalarMatrixOp(op:String,row1:Row,scalar:Double):Row = op match {
    case "+"=> addRows(row1,scalar)
    case "-"=> minusRows(row1,scalar)
    case "-2"=> minusRows(scalar,row1)
    case "*"=> multRows(row1,scalar)
  }


  def transpose(matrix:Matrix): Matrix={
    var newMatrix = new Array[Row](matrix(0).length)
    for (i<-0 until newMatrix.length){
      newMatrix(i)=getColumn(matrix,i)
    }
    newMatrix.toList
  }


  def getColumn(matrix:Matrix,index:Int): Row={
    var column = new Array[Double](matrix.length)
    var count = 0
    while (count < matrix.length){
      column(count)=matrix(count)(index)
      count+=1
    }
    column.toList
  }

  def addColumn(matrix:Matrix,column:Row,index:Int): Matrix={
    var currentRow= -1
    val result = (new Array[Row](matrix.length)).map(row=>{
      currentRow+=1
      val newRow = (matrix(currentRow).slice(0,index):+column(currentRow)):::matrix(currentRow).slice(index,matrix.length+1)
      newRow
    }).toList
    result
  }
}

package object Data{

  case class Geocode (latitude:String,longitude:String)
}

