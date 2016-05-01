package Utilis

object Geocoder {

	def getRequest(url:String):String={
		var result = scala.io.Source.fromURL(url).mkString
		result
	}

	def createUrl (num:Int,street:String, city:String, county:String, postCode:String):String={
		val openstreets="http://nominatim.openstreetmap.org/search?q="
		var address = (num+" "+street+", "+city+", "+county+", "+postCode).replace(' ','+')
		var url = openstreets+address+"&format=json&polgon=1&addressdetails=1&limit=1&countrycodes=gb"
		println(url)
		url
	}

	def main (args:Array[String]){
		val url = createUrl (4,"ST NICHOLAS CHURCH STREET","WARWICK","WARWICKSHIRE","CV34 4JD")
	}
}
