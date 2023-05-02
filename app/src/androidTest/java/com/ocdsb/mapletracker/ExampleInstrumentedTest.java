packagecom.ocdsb.mapletracker;

importandroid.content.Context;

importandroidx.test.platform.app.InstrumentationRegistry;
importandroidx.test.ext.junit.runners.AndroidJUnit4;

importorg.junit.Test;
importorg.junit.runner.RunWith;

importstaticorg.junit.Assert.*;

/**
*Instrumentedtest,whichwillexecuteonanAndroiddevice.
*
*@see<ahref="http://d.android.com/tools/testing">Testingdocumentation</a>
*/
@RunWith(AndroidJUnit4.class)
publicclassExampleInstrumentedTest{
@Test
publicvoiduseAppContext(){
//Contextoftheappundertest.
ContextappContext=InstrumentationRegistry.getInstrumentation().getTargetContext();
assertEquals("com.ocdsb.mapletracker",appContext.getPackageName());
}
}