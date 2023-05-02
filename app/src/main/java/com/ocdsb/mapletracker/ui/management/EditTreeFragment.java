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
importandroid.view.View;
importandroid.view.ViewGroup;
importandroid.widget.ArrayAdapter;
importandroid.widget.EditText;
importandroid.widget.Spinner;

importcom.google.android.material.button.MaterialButton;
importcom.ocdsb.mapletracker.Config;
importcom.ocdsb.mapletracker.R;
importcom.ocdsb.mapletracker.api.MapAPI;
importcom.ocdsb.mapletracker.databinding.FragmentEditTreeBinding;

importorg.osmdroid.config.Configuration;
importorg.osmdroid.util.GeoPoint;
importorg.osmdroid.views.MapView;

importjava.util.ArrayList;
importjava.util.Arrays;

publicclassEditTreeFragmentextendsFragment{
privateFragmentEditTreeBindingbinding;
privatefinalintREQUEST_PERMISSIONS_REQUEST_CODE=1;
privateMapViewmap=null;

@Override
publicViewonCreateView(@NonNullLayoutInflaterinflater,@NullableViewGroupcontainer,
@NullableBundlesavedInstanceState){
binding=FragmentEditTreeBinding.inflate(inflater,container,false);
Viewroot=binding.getRoot();
//Addingthespinnertothefragment
Spinnerspinner=root.findViewById(R.id.tree_spinner);
//CharSequenceArraywhichthespinnerwilldisplaytotheuser
CharSequence[]name={"this","thing","that"};
//Initialisethespinner
ArrayAdapter<CharSequence>adapter=newArrayAdapter<>(requireContext(),android.R.layout.simple_spinner_dropdown_item);
adapter.addAll(name);
//Specifylayoutusedbythespinner
adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//Applyadaptertothespinner
spinner.setAdapter(adapter);
//Addthemaptothefragment
//Buildingthemap
Contextctx=requireActivity().getApplicationContext();
Configuration.getInstance().load(ctx,PreferenceManager.getDefaultSharedPreferences(ctx));
MapAPImapAPI=newMapAPI();
map=root.findViewById(R.id.map);

//RequestPermissionsnecessaryformaptofunction.
String[]Permissions={Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.WRITE_EXTERNAL_STORAGE};
requestPermissionsIfNecessary(Permissions);
map=mapAPI.buildMap(map,getContext());
map.getController().setCenter(newGeoPoint(Config.locationAPI.latitude,Config.locationAPI.longitude));
//Getusertextinput
EditTexteditText=(EditText)root.findViewById(R.id.editName);
System.out.println("Thisisedittext:"+editText);
System.out.println(editText.getText());
MaterialButtonbutton=binding.saveButton;
System.out.println(button);
button.setOnClickListener(v->{
System.out.println("Theradusertappedthecoolbutton");
System.out.println(editText.getText());
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
}