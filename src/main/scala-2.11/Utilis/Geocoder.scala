package Utilis

import scala.xml._

case class Location(place:String,licence:String,osm:String,osmID:String,bb:List[String],lat:String,lon:String)
object Geocoder {

	def getRequest(url:String)= XML.loadString (scala.io.Source.fromURL(url).mkString )


	def createUrl (num:Int,street:String, city:String, county:String, postCode:String):String={
		val openstreets="http://nominatim.openstreetmap.org/search?q="
		val address = (num+" "+street+", "+city+", "+county+", "+postCode).replace(' ','+')
		val url = openstreets+address+"&format=xml&polgon=1&addressdetails=1&limit=1&countrycodes=gb"
		url
	}


	def getGeocode (num:Int,street:String, city:String, county:String, postCode:String)={
		val url = createUrl(num,street,city,county,postCode)
		val xml = (getRequest(url))
		val lon = xml \ "place" \ "@lon"
		val lat = xml \ "place" \ "@lat"
		(lon.toString().toDouble,lat.toString().toDouble)
	}

	def main (args:Array[String]){
		val url = getGeocode (4,"ST NICHOLAS CHURCH STREET","WARWICK","WARWICKSHIRE","CV34 4JD")
		println (url)
	}
}
