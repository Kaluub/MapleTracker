packagecom.ocdsb.mapletracker.api;

importandroid.location.Location;
importandroid.location.LocationListener;
importandroid.location.LocationManager;

importandroidx.annotation.NonNull;

importjava.util.List;

publicclassLocationAPIimplementsLocationListener{
publicdoublelatitude;
publicdoublelongitude;

publicLocationAPI(){
latitude=0.0;
longitude=0.0;
}

publicvoidupdateLocationManager(LocationManagermanager){
List<String>providers=manager.getAllProviders();
LocationbestLocation=null;
for(Stringprovider:providers){
//Wecandeterminethemostaccuratelocationprovidertoprovidethebestservice.
try{
LocationlastKnownLocation=manager.getLastKnownLocation(provider);
if(lastKnownLocation==null){
continue;
}
if(bestLocation==null||lastKnownLocation.getAccuracy()<bestLocation.getAccuracy()){
latitude=lastKnownLocation.getLatitude();
longitude=lastKnownLocation.getLongitude();
bestLocation=lastKnownLocation;
}
}catch(SecurityExceptione){
System.out.println("Provider"+provider+"doesnotwork(SecurityException).");
}
}
}

@Override
publicvoidonLocationChanged(@NonNullLocationlocation){
//Keepthelocationup-to-date.
System.out.println("Locationupdated.");
latitude=location.getLatitude();
longitude=location.getLongitude();
}

}