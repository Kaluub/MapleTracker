packagecom.ocdsb.mapletracker.data;

importcom.ocdsb.mapletracker.Config;

importjava.util.Date;

publicclassTreePin{
//Representsatreepin&it'srespectivedata.
publicDatecreatedAt=newDate();
publicDateeditedAt=newDate();
publicStringname;
publicdoublelatitude=0;
publicdoublelongitude=0;
publicdoublesapLitresCollectedTotal=0;
publicdoublesapLitresCollectedResettable=0;
publicinteditsTotal=0;
publicinteditsResettable=0;

publicStringsaveToLine(){
returncreatedAt.getTime()+
Config.fileSeparator+
editedAt.getTime()+
Config.fileSeparator+
name+
Config.fileSeparator+
latitude+
Config.fileSeparator+
longitude+
Config.fileSeparator+
sapLitresCollectedTotal+
Config.fileSeparator+
sapLitresCollectedResettable+
Config.fileSeparator+
editsTotal+
Config.fileSeparator+
editsResettable;
}

publicstaticTreePingetFromFileLine(Stringline){
String[]data=line.split(",");
TreePinpin=newTreePin();
pin.createdAt=data.length>0?newDate(Long.parseLong(data[0])):newDate();
pin.editedAt=data.length>1?newDate(Long.parseLong(data[1])):newDate();
pin.name=data.length>2?data[2]:"Default";
pin.latitude=data.length>3?Double.parseDouble(data[3]):0.0;
pin.longitude=data.length>4?Double.parseDouble(data[4]):0.0;
pin.sapLitresCollectedTotal=data.length>5?Double.parseDouble(data[5]):0.0;
pin.sapLitresCollectedResettable=data.length>6?Double.parseDouble(data[6]):0.0;
pin.editsTotal=data.length>7?Integer.parseInt(data[7]):0;
pin.editsResettable=data.length>8?Integer.parseInt(data[8]):0;
returnpin;
}
}
