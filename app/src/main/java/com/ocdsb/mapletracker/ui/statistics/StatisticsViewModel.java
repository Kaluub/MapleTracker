packagecom.ocdsb.mapletracker.ui.statistics;

importandroidx.lifecycle.LiveData;
importandroidx.lifecycle.MutableLiveData;
importandroidx.lifecycle.ViewModel;

publicclassStatisticsViewModelextendsViewModel{

privatefinalMutableLiveData<String>mText;

publicStatisticsViewModel(){
mText=newMutableLiveData<>();
mText.setValue("Thisisstatisticsfragment");
}

publicLiveData<String>getText(){
returnmText;
}
}