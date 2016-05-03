package Utilis
import scala.xml._

object Geocoder {

	private val getRequest=(url: String) =>{
		Thread.sleep(6000)
		XML.loadString(scala.io.Source.fromURL(url).mkString)
	}

	private def createUrl( city: String,district: String, county: String): String = {
		val openstreets = "http://nominatim.openstreetmap.org/search?q="
		val address = ( city + ", " + district + ", " + county).replace(' ', '+')
		val url = openstreets + address + "&format=xml&polgon=1&addressdetails=1&limit=1&countrycodes=gb"
		url
	}


	def getGeocode(city: String,district: String, county: String) = {
		val url = createUrl( city, district, county)
		val xml = (getRequest(url))
		val lon = xml \ "place" \ "@lon"
		val lat = xml \ "place" \ "@lat"
		new Data.Geocode(lat.toString()+"0",lon.toString()+"0")
	}
}

