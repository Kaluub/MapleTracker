packagecom.ocdsb.mapletracker.api;


importandroid.content.Context;
importcom.ocdsb.mapletracker.Config;
importcom.ocdsb.mapletracker.data.TreePin;

importorg.osmdroid.api.IMapController;
importorg.osmdroid.events.MapEventsReceiver;
importorg.osmdroid.tileprovider.tilesource.TileSourceFactory;
importorg.osmdroid.util.GeoPoint;
importorg.osmdroid.views.CustomZoomButtonsController;
importorg.osmdroid.views.MapView;
importorg.osmdroid.views.overlay.Marker;

importjava.util.ArrayList;

publicclassMapAPIimplementsMapEventsReceiver{
privateMapViewmap=null;
publicArrayList<GeoPoint>lookup=newArrayList<>();
publicArrayList<TreePin>treePins=newArrayList<>();

publicMapViewbuildMap(MapViewm,Contextc){
map=m;
map.setTileSource(TileSourceFactory.MAPNIK);
//Givingtheusertheabilitytozoomthemap
map.getZoomController().setVisibility(CustomZoomButtonsController.Visibility.ALWAYS);
map.setMultiTouchControls(true);
//Changingthedefaultmaplocationandzoom
IMapControllermapController=map.getController();
mapController.setZoom(10.0);
//mapController.setCenter(newGeoPoint(Config.locationAPI.latitude,Config.locationAPI.longitude));
loadPins();
//MapViewmMapView=newMapView(inflater.getContext());
//allowusertoaddpins--thisshouldprobablybemovedtothefragmentwhereitisused
/*MapEventsReceivermReceive=newMapEventsReceiver(){
@Override
publicbooleansingleTapConfirmedHelper(GeoPointp){
if(lookup.contains(p)){
System.out.println(p+"alreadyhasapin");
returnfalse;
}
MarkerstartMarker=newMarker(map);
startMarker.setPosition(p);
startMarker.setAnchor(Marker.ANCHOR_CENTER,Marker.ANCHOR_CENTER);
map.getOverlays().add(startMarker);
lookup.add(p);
returntrue;
}

@Override
publicbooleanlongPressHelper(GeoPointp){
returnfalse;
}
};*/


//MapEventsOverlayOverlayEvents=newMapEventsOverlay(c,mReceive);
//map.getOverlays().add(OverlayEvents);


//map.setTileSource(TileSourceFactory.MAPNIK);
returnmap;
}


@Override
publicbooleansingleTapConfirmedHelper(GeoPointp){
IMapControllermapController=map.getController();
mapController.animateTo(p);
MarkerstartMarker=newMarker(map);
startMarker.setPosition(p);
startMarker.setAnchor(Marker.ANCHOR_CENTER,Marker.ANCHOR_CENTER);
map.getOverlays().add(startMarker);
savePins();
returntrue;
}

publicvoidloadPins(){
Stringstore=Config.fileManager.readFile(map.getContext(),Config.fileName);
if(store==null){
System.out.println("Pinsisnull");
return;
}
for(StringtreeData:store.split("\n")){
if(treeData.length()<=0){
continue;
}
try{
TreePinpin=TreePin.getFromFileLine(treeData);
treePins.add(pin);
GeoPointgeoPoint=newGeoPoint(pin.longitude,pin.latitude);
lookup.add(geoPoint);
Markermarker=newMarker(map);
marker.setPosition(geoPoint);
map.getOverlays().add(marker);
}catch(Exceptione){
//Thetreecouldnotberead.
}
}
}

publicvoidsavePins(){
System.out.println("StorePinsMethodhasbeencalled");
StringBuilderstore=newStringBuilder();
for(TreePinpin:this.treePins){
store.append("\n").append(pin.saveToLine());
}
System.out.println(store.toString());
Config.fileManager.saveFile(map.getContext(),Config.fileName,store.toString());
}

@Override
publicbooleanlongPressHelper(GeoPointp){
returnfalse;
}
}
