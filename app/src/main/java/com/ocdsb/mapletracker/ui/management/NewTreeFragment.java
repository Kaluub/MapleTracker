packagecom.ocdsb.mapletracker.ui.management;

importandroidx.core.app.ActivityCompat;
importandroidx.core.content.ContextCompat;

importandroid.Manifest;
importandroid.content.Context;
importandroid.content.pm.PackageManager;
importandroid.os.Bundle;

importandroidx.annotation.NonNull;
importandroidx.annotation.Nullable;
importandroidx.fragment.app.Fragment;

importandroid.preference.PreferenceManager;
importandroid.view.LayoutInflater;
importandroid.view.MotionEvent;
importandroid.view.View;
importandroid.view.ViewGroup;
importandroid.widget.EditText;

importcom.google.android.material.button.MaterialButton;
importcom.ocdsb.mapletracker.Config;
importcom.ocdsb.mapletracker.R;
importcom.ocdsb.mapletracker.api.MapAPI;
importcom.ocdsb.mapletracker.data.TreePin;
importcom.ocdsb.mapletracker.databinding.FragmentNewTreeBinding;

importorg.osmdroid.api.IMapController;
importorg.osmdroid.config.Configuration;
importorg.osmdroid.events.MapEventsReceiver;
importorg.osmdroid.util.GeoPoint;
importorg.osmdroid.views.MapView;
importorg.osmdroid.views.overlay.Marker;
importorg.osmdroid.views.overlay.Overlay;

importjava.util.ArrayList;
importjava.util.Arrays;

publicclassNewTreeFragmentextendsFragmentimplementsMapEventsReceiver{
privateFragmentNewTreeBindingbinding;
privatefinalintREQUEST_PERMISSIONS_REQUEST_CODE=1;
privateMapViewmap=null;

@Override
publicViewonCreateView(@NonNullLayoutInflaterinflater,@NullableViewGroupcontainer,
@NullableBundlesavedInstanceState){
binding=FragmentNewTreeBinding.inflate(inflater,container,false);
Viewroot=binding.getRoot();

//RequestPermissionsnecessaryformaptofunction.
String[]Permissions={
Manifest.permission.ACCESS_FINE_LOCATION,
Manifest.permission.WRITE_EXTERNAL_STORAGE
};
requestPermissionsIfNecessary(Permissions);

//Buildingthemap
Contextctx=requireActivity().getApplicationContext();
Configuration.getInstance().load(ctx,PreferenceManager.getDefaultSharedPreferences(ctx));
MapAPImapAPI=newMapAPI();
map=root.findViewById(R.id.map);
map=mapAPI.buildMap(map,getContext());
map.getController().setCenter(newGeoPoint(Config.locationAPI.latitude,Config.locationAPI.longitude));

mapAPI.loadPins();
for(TreePinpin:mapAPI.treePins){
MarkertreeMarker=newMarker(map);
treeMarker.setPosition(newGeoPoint(pin.latitude,pin.longitude));
treeMarker.setTextIcon("T");
map.getOverlays().add(treeMarker);
}

Markermarker=newMarker(map);
marker.setPosition((GeoPoint)map.getMapCenter());
map.getOverlays().add(marker);

OverlaymOverlay=newOverlay(){
@Override
publicbooleanonScroll(MotionEventpEvent1,MotionEventpEvent2,floatpDistanceX,floatpDistanceY,MapViewpMapView){
marker.setPosition(newGeoPoint((float)pMapView.getMapCenter().getLatitude(),
(float)pMapView.getMapCenter().getLongitude()));
returnsuper.onScroll(pEvent1,pEvent2,pDistanceX,pDistanceY,pMapView);
}
};
map.getOverlays().add(mOverlay);
//Addusertextinput
EditTextname=root.findViewById(R.id.addName);
EditTextsap=root.findViewById(R.id.add_collected);
//Addsavebutton
MaterialButtonbutton=binding.saveButton2;
button.setOnClickListener(v->{
TreePintreePin=newTreePin();
treePin.name=name.getText().toString();
treePin.sapLitresCollectedTotal=Double.parseDouble(sap.getText().toString());
treePin.sapLitresCollectedResettable=treePin.sapLitresCollectedTotal;
treePin.latitude=map.getMapCenter().getLatitude();
treePin.longitude=map.getMapCenter().getLongitude();
mapAPI.treePins.add(treePin);
mapAPI.savePins();
});
returnroot;
}

@Override
publicvoidonDestroyView(){
super.onDestroyView();
binding=null;
}


@Override
publicvoidonPause(){
super.onPause();
//Thiswillrefreshtheosmdroidconfigurationonresuming.
map.onPause();
}

@Override
publicvoidonRequestPermissionsResult(intrequestCode,@NonNullString[]permissions,int[]grantResults){
ArrayList<String>permissionsToRequest=newArrayList<>(Arrays.asList(permissions).subList(0,grantResults.length));
if(permissionsToRequest.size()>0){
ActivityCompat.requestPermissions(
requireActivity(),
permissionsToRequest.toArray(newString[0]),
REQUEST_PERMISSIONS_REQUEST_CODE);
}
}

privatevoidrequestPermissionsIfNecessary(String[]permissions){
ArrayList<String>permissionsToRequest=newArrayList<>();
for(Stringpermission:permissions){
if(ContextCompat.checkSelfPermission(requireActivity(),permission)
!=PackageManager.PERMISSION_GRANTED){
//Permissionisnotgranted
permissionsToRequest.add(permission);
}
}
if(permissionsToRequest.size()>0){
ActivityCompat.requestPermissions(
requireActivity(),
permissionsToRequest.toArray(newString[0]),
REQUEST_PERMISSIONS_REQUEST_CODE);
}
}
@Override
publicbooleansingleTapConfirmedHelper(GeoPointp){
IMapControllermapController=map.getController();
mapController.animateTo(p);
MarkerstartMarker=newMarker(map);
startMarker.setPosition(p);
startMarker.setAnchor(Marker.ANCHOR_CENTER,Marker.ANCHOR_CENTER);
map.getOverlays().add(startMarker);
System.out.println(p);
returntrue;
}

@Override
publicbooleanlongPressHelper(GeoPointp){
returnfalse;
}
}