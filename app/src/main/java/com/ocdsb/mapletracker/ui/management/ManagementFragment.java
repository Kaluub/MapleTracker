packagecom.ocdsb.mapletracker.ui.management;

importandroid.os.Bundle;
importandroid.util.Log;
importandroid.view.LayoutInflater;
importandroid.view.View;
importandroid.view.ViewGroup;


importandroidx.annotation.NonNull;
importandroidx.fragment.app.Fragment;
importandroidx.navigation.fragment.NavHostFragment;

importcom.google.android.material.button.MaterialButton;
importcom.ocdsb.mapletracker.R;
importcom.ocdsb.mapletracker.databinding.FragmentManagementBinding;

publicclassManagementFragmentextendsFragment{
privateFragmentManagementBindingbinding;

publicViewonCreateView(@NonNullLayoutInflaterinflater,
ViewGroupcontainer,BundlesavedInstanceState){
binding=FragmentManagementBinding.inflate(inflater,container,false);
Viewroot=binding.getRoot();

MaterialButtonbutton=binding.newTreeButton;
button.setOnClickListener(v->{

NavHostFragment.findNavController(ManagementFragment.this).navigate(R.id.navigation_new_tree);
Log.d("BUTTONS","UsertappedtheNewTreeButton");
});
MaterialButtonbutton2=binding.editTreeButton;
button2.setOnClickListener(v->{

NavHostFragment.findNavController(ManagementFragment.this).navigate(R.id.navigation_edit_tree);
Log.d("BUTTONS","UsertappedtheEditTreeButton");
});

returnroot;
}

@Override
publicvoidonDestroyView(){
super.onDestroyView();
binding=null;
}
}
