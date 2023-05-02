packagecom.ocdsb.mapletracker.api;

importandroid.content.Context;

importjava.io.BufferedReader;
importjava.io.FileInputStream;
importjava.io.FileOutputStream;
importjava.io.IOException;
importjava.io.InputStreamReader;
importjava.nio.charset.StandardCharsets;

publicclassFileManager{
publicFileManager(){}

publicStringreadFile(Contextcontext,StringfileName){
//Readthecontentsofafilename(storedininternalAndroidstorage).
Stringcontents;
try(FileInputStreamfis=context.openFileInput(fileName)){
InputStreamReaderinputStreamReader=
newInputStreamReader(fis,StandardCharsets.UTF_8);
StringBuilderstringBuilder=newStringBuilder();
//Readchunkbychunkfromthefile.
try(BufferedReaderreader=newBufferedReader(inputStreamReader)){
Stringline=reader.readLine();
while(line!=null){
stringBuilder.append(line).append('\n');
line=reader.readLine();
}
}catch(IOExceptione){
System.out.println("IOexceptionwhileREADINGfile");
returnnull;
}finally{
contents=stringBuilder.toString();
}
}catch(IOExceptione){
System.out.println("IOexceptionwhileREADINGfile");
returnnull;
}
returncontents;
}

publicvoidsaveFile(Contextcontext,StringfileName,Stringcontent){
//SaveafiletotheinternalAndroidstorage.
try(FileOutputStreamfos=context.openFileOutput(fileName,Context.MODE_PRIVATE)){
fos.write(content.getBytes(StandardCharsets.UTF_8));
}catch(IOExceptione){
System.out.println("IOexceptionwhileWRITINGfile");
}
}
}
