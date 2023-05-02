packagecom.ocdsb.mapletracker;

importandroid.Manifest;
importandroid.content.Context;
importandroid.content.Intent;
importandroid.content.pm.PackageManager;
importandroid.location.LocationManager;
importandroid.os.Bundle;
importandroid.os.StrictMode;
importandroid.provider.Settings;

importandroidx.annotation.NonNull;
importandroidx.appcompat.app.AppCompatActivity;
importandroidx.core.app.ActivityCompat;
importandroidx.navigation.NavController;
importandroidx.navigation.Navigation;
importandroidx.navigation.ui.AppBarConfiguration;
importandroidx.navigation.ui.NavigationUI;

importcom.ocdsb.mapletracker.databinding.ActivityMainBinding;

publicclassMainActivityextendsAppCompatActivity{
@Override
protectedvoidonCreate(BundlesavedInstanceState){
super.onCreate(savedInstanceState);
StrictMode.ThreadPolicypolicy=newStrictMode.ThreadPolicy.Builder().permitAll().build();
StrictMode.setThreadPolicy(policy);

ActivityMainBindingbinding=ActivityMainBinding.inflate(getLayoutInflater());
setContentView(binding.getRoot());

LocationManagerservice=(LocationManager)getSystemService(Context.LOCATION_SERVICE);
booleanenabled=service.isProviderEnabled(LocationManager.GPS_PROVIDER);
if(!enabled){
Intentintent=newIntent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
startActivity(intent);
}

if(ActivityCompat.checkSelfPermission(this,android.Manifest.permission.ACCESS_FINE_LOCATION)!=PackageManager.PERMISSION_GRANTED&&ActivityCompat.checkSelfPermission(this,Manifest.permission.ACCESS_COARSE_LOCATION)!=PackageManager.PERMISSION_GRANTED){
//publicvoidonRequestPermissionsResult(intrequestCode,String[]permissions,
//int[]grantResults)
//tohandlethecasewheretheusergrantsthepermission.Seethedocumentation
//forActivityCompat#requestPermissionsformoredetails.
String[]permissions=newString[2];
permissions[0]=Manifest.permission.ACCESS_FINE_LOCATION;
permissions[1]=Manifest.permission.ACCESS_COARSE_LOCATION;
ActivityCompat.requestPermissions(this,permissions,0);
}else{
System.out.println("LocationAPImanagerupdating...");
Config.locationAPI.updateLocationManager(service);
}

//PassingeachmenuIDasasetofIdsbecauseeach
//menushouldbeconsideredastopleveldestinations.
AppBarConfigurationappBarConfiguration=newAppBarConfiguration.Builder(
R.id.navigation_home,R.id.navigation_management,R.id.navigation_statistics)
.build();
NavControllernavController=Navigation.findNavController(this,R.id.nav_host_fragment_activity_main);
NavigationUI.setupActionBarWithNavController(this,navController,appBarConfiguration);
NavigationUI.setupWithNavController(binding.navView,navController);
}

@Override
publicvoidonRequestPermissionsResult(intrequestCode,@NonNullString[]permissions,@NonNullint[]grantResults){
super.onRequestPermissionsResult(requestCode,permissions,grantResults);
if(ActivityCompat.checkSelfPermission(this,android.Manifest.permission.ACCESS_FINE_LOCATION)!=PackageManager.PERMISSION_GRANTED
&&ActivityCompat.checkSelfPermission(this,Manifest.permission.ACCESS_COARSE_LOCATION)!=PackageManager.PERMISSION_GRANTED){
String[]requestedPermissions=newString[2];
permissions[0]=Manifest.permission.ACCESS_FINE_LOCATION;
permissions[1]=Manifest.permission.ACCESS_COARSE_LOCATION;
ActivityCompat.requestPermissions(this,requestedPermissions,1);
}else{
LocationManagerservice=(LocationManager)getSystemService(Context.LOCATION_SERVICE);
Config.locationAPI.updateLocationManager(service);
}
}
}