packagecom.ocdsb.mapletracker.api;

importcom.google.gson.JsonArray;
importcom.google.gson.JsonElement;
importcom.google.gson.JsonObject;
importcom.ocdsb.mapletracker.Config;
importcom.ocdsb.mapletracker.data.StationResult;

importorg.w3c.dom.Document;

importjava.util.Date;

publicclassWeatherAPI{
Parserparser;

publicWeatherAPI(){
parser=newParser();
}

publicdoublegetDistance(JsonObjectproperties){
doubleelementLatitude=properties.get("Latitude").getAsDouble();
doubleelementLongitude=properties.get("Longitude").getAsDouble();
returnMath.sqrt(Math.pow(Config.locationAPI.latitude-elementLatitude,2)+Math.pow(Config.locationAPI.longitude-elementLongitude,2));
}

publicString[]getClosestStationDetails(){
//TODO:ConsiderusingAmericanweatherstationsaswell.See:https://api.weather.gov/stations
JsonObjectcanadianStations=parser.getJSONFromURL("https://collaboration.cmc.ec.gc.ca/cmc/cmos/public_doc/msc-data/citypage-weather/site_list_en.geojson");
JsonArrayfeaturesArray=canadianStations.getAsJsonArray("features");
//DefaultstoOttawaincaseyou'renotonEarth.
StringbestFeatureId="s0000430";
StringbestProvinceCode="ON";
//Thefurthestpossibledistanceshouldonlybe180,sothisshouldalwaysbeabiggerdistance.
doubleclosestDistance=100000;

for(JsonElementelement:featuresArray){
JsonObjectproperties=element.getAsJsonObject().get("properties").getAsJsonObject();
doubledistance=getDistance(properties);
if(distance<closestDistance){
System.out.println("Newcloserdistance!"+bestFeatureId+"("+bestProvinceCode+")is"+closestDistance);
bestFeatureId=properties.get("Codes").getAsString();
bestProvinceCode=properties.get("ProvinceCodes").getAsString();
closestDistance=distance;
}
}

String[]result=newString[2];
result[0]=bestFeatureId;
result[1]=bestProvinceCode;
returnresult;
}

publicStationResultgetStation(StringstationID,StringprovinceCode){
//CheckiftheStationResultcachedinstanceislessthan1minuteold.
if(Config.stationResult!=null&&
newDate().getTime()-Config.stationResult.createdAt.getTime()<60000)
returnConfig.stationResult;

//UsesthestationIDtogettheweatherfromthatstation.
Documentdoc=parser.getXMLFromURL(String.format("https://dd.weather.gc.ca/citypage_weather/xml/%s/%s_e.xml",provinceCode,stationID));
if(doc==null){
//IfthereisnoavailableDocument,returnnull.
returnnull;
}

//UsestheindexfromtheXMLdocumenttogetthese.
Stringtemperature=doc
.getFirstChild()
.getChildNodes()
.item(11)
.getChildNodes()
.item(11)
.getChildNodes()
.item(0)
.getNodeValue();

Stringhigh=doc
.getFirstChild()
.getChildNodes()
.item(13)
.getChildNodes()
.item(5)
.getChildNodes()
.item(3)
.getChildNodes()
.item(0)
.getNodeValue();

Stringlow=doc
.getFirstChild()
.getChildNodes()
.item(13)
.getChildNodes()
.item(5)
.getChildNodes()
.item(5)
.getChildNodes()
.item(0)
.getNodeValue();

StationResultstationResult=newStationResult();
stationResult.temperature=Double.parseDouble(temperature);
stationResult.high=Double.parseDouble(high);
stationResult.low=Double.parseDouble(low);
stationResult.stationID=stationID;
stationResult.provinceCode=provinceCode;

if(Config.useFakeTemperature){
//Usefakedatatosimulateprimetappingconditions.
stationResult.temperature=-0.0;
stationResult.high=10.0;
stationResult.low=-10.0;
}

Config.stationResult=stationResult;

returnstationResult;
}
}