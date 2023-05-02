packagecom.ocdsb.mapletracker.ui.home;

importandroid.os.Bundle;
importandroid.view.LayoutInflater;
importandroid.view.View;
importandroid.view.ViewGroup;
importandroid.widget.Button;
importandroid.widget.TextView;

importandroidx.annotation.NonNull;
importandroidx.fragment.app.Fragment;

importcom.google.android.material.snackbar.Snackbar;
importcom.ocdsb.mapletracker.Config;
importcom.ocdsb.mapletracker.R;
importcom.ocdsb.mapletracker.data.StationResult;
importcom.ocdsb.mapletracker.databinding.FragmentHomeBinding;
importcom.ocdsb.mapletracker.interfaces.StationResultCallback;

importjava.util.Random;

publicclassHomeFragmentextendsFragment{

privateFragmentHomeBindingbinding;
privatefinalRandomrng=newRandom();

publicViewonCreateView(@NonNullLayoutInflaterinflater,
ViewGroupcontainer,BundlesavedInstanceState){

binding=FragmentHomeBinding.inflate(inflater,container,false);

Viewroot=binding.getRoot();

if(Config.debugMode){
finalButtondebugButton=binding.debug;
debugButton.setVisibility(View.VISIBLE);
debugButton.setOnClickListener(view->{
Config.useFakeTemperature=!Config.useFakeTemperature;
Config.stationResult=null;
this.updateWeatherElements();
Snackbar.make(
view,
String.format("Faketemperaturemodeisnowsetto%b.",Config.useFakeTemperature),
Snackbar.LENGTH_SHORT
).show();
});
}

returnroot;
}

@Override
publicvoidonStart(){
super.onStart();
this.updateWeatherElements();
}

publicvoidupdateFromStationResults(StationResultstationResults){
//Preventcrashingifuserswitchesthefragmenttooearly.
if(binding==null)return;
finalTextViewtemperatureText=binding.temperature;
finalTextViewsplashText=binding.splash;
//Formatthetemperaturestringproperly.
temperatureText.setText(String.format(getResources().getString(R.string.temperature_replace),stationResults.temperature));
if(stationResults.low<0&&stationResults.high>0){
//Theweatherisgoodformapletapping.Useagoodsplashtext.
String[]splashGood=getResources().getStringArray(R.array.splash_good);
intsplashIndex=rng.nextInt(splashGood.length);
splashText.setText(splashGood[splashIndex]);
}else{
//Theweatherisnotgoodformapletapping.Useabadsplashtext.
String[]splashBad=getResources().getStringArray(R.array.splash_bad);
intsplashIndex=rng.nextInt(splashBad.length);
splashText.setText(splashBad[splashIndex]);
}
}

publicvoidupdateWeatherElements(){
binding.temperature.setText(R.string.temperature_default);
binding.splash.setText(R.string.splash_default);
this.fetchStationResults(stationResults->requireActivity().runOnUiThread(()->updateFromStationResults(stationResults)));
}

privatevoidfetchStationResults(StationResultCallbackcallback){
//Useanewthreadtorunthefunctioninthebackground.
newThread(()->{
try{
String[]stationDetails=Config.weatherAPI.getClosestStationDetails();
callback.onResult(Config.weatherAPI.getStation(stationDetails[0],stationDetails[1]));
}catch(Exceptione){
System.out.println("Exceptionwhilefetchingtheweather.");
}
}).start();
}

@Override
publicvoidonDestroyView(){
super.onDestroyView();
binding=null;
}
}