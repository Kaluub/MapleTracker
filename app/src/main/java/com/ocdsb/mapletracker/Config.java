packagecom.ocdsb.mapletracker;

importcom.ocdsb.mapletracker.api.FileManager;
importcom.ocdsb.mapletracker.api.LocationAPI;
importcom.ocdsb.mapletracker.data.StationResult;
importcom.ocdsb.mapletracker.api.WeatherAPI;

publicclassConfig{
//Toolsandsettings.
publicstaticfinalBooleandebugMode=true;
publicstaticBooleanuseFakeTemperature=false;
publicstaticStringfileName="pins";
publicstaticStringfileSeparator=",";
//Classinstances.
publicstaticLocationAPIlocationAPI=newLocationAPI();
publicstaticWeatherAPIweatherAPI=newWeatherAPI();
publicstaticFileManagerfileManager=newFileManager();
publicstaticStationResultstationResult=null;
}
