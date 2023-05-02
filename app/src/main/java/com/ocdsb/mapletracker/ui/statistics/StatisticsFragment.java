packagecom.ocdsb.mapletracker.ui.statistics;

importandroid.os.Bundle;
importandroid.view.LayoutInflater;
importandroid.view.View;
importandroid.view.ViewGroup;
importandroid.widget.TextView;

importandroidx.annotation.NonNull;
importandroidx.fragment.app.Fragment;
importandroidx.lifecycle.ViewModelProvider;

importcom.ocdsb.mapletracker.databinding.FragmentStatisticsBinding;

publicclassStatisticsFragmentextendsFragment{

privateFragmentStatisticsBindingbinding;

publicViewonCreateView(@NonNullLayoutInflaterinflater,
ViewGroupcontainer,BundlesavedInstanceState){
StatisticsViewModelstatisticsViewModel=
newViewModelProvider(this).get(StatisticsViewModel.class);

binding=FragmentStatisticsBinding.inflate(inflater,container,false);
Viewroot=binding.getRoot();

finalTextViewtextView=binding.textStatistics;
statisticsViewModel.getText().observe(getViewLifecycleOwner(),textView::setText);
returnroot;
}

@Override
publicvoidonDestroyView(){
super.onDestroyView();
binding=null;
}
}