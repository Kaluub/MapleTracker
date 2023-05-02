packagecom.ocdsb.mapletracker.api;

importcom.google.gson.JsonObject;
importcom.google.gson.JsonParser;

importorg.w3c.dom.Document;
importorg.xml.sax.InputSource;
importorg.xml.sax.SAXException;

importjava.io.BufferedReader;
importjava.io.IOException;
importjava.io.InputStreamReader;
importjava.io.StringReader;
importjava.net.HttpURLConnection;
importjava.net.URL;

importjavax.xml.parsers.DocumentBuilder;
importjavax.xml.parsers.DocumentBuilderFactory;
importjavax.xml.parsers.ParserConfigurationException;

publicclassParser{
publicParser(){}

publicDocumentgetXMLFromURL(Stringaddress){
//ParsesanXMLfileonlinetoaDocument.
try{
//Opentheconnection.
URLurl=newURL(address);
HttpURLConnectioncon=(HttpURLConnection)url.openConnection();
con.setRequestMethod("GET");

//Read.
BufferedReaderreader=newBufferedReader(
newInputStreamReader(con.getInputStream())
);

StringBuildercontent=newStringBuilder();
while(true){
StringinputLine=reader.readLine();
if(inputLine==null)
break;
content.append(inputLine);
}
reader.close();

DocumentBuilderFactoryfactory=DocumentBuilderFactory.newInstance();
DocumentBuilderbuilder=factory.newDocumentBuilder();

//ParseandreturnaDocument.
returnbuilder.parse(
newInputSource(
newStringReader(String.valueOf(content))
)
);
}catch(IOException|SAXException|ParserConfigurationExceptione){
//Incaseofanyexceptions,wehavenothingtoreturn.
returnnull;
}
}

publicJsonObjectgetJSONFromURL(Stringaddress){
//ParsesaJSONdocumenttoaJsonObjectinstance.
try{
//Opentheconnection.
URLurl=newURL(address);
HttpURLConnectioncon=(HttpURLConnection)url.openConnection();
con.setRequestMethod("GET");

//Read.
BufferedReaderreader=newBufferedReader(
newInputStreamReader(con.getInputStream())
);

//ParseJSON.
returnJsonParser.parseReader(reader).getAsJsonObject();
}catch(IOExceptione){
//Incaseofanyexceptions,wehavenothingtoreturn.
returnnull;
}
}
}
