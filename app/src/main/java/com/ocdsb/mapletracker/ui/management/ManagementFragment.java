package com.ocdsb.mapletracker.ui.management;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.google.android.material.button.MaterialButton;
import com.ocdsb.mapletracker.R;
import com.ocdsb.mapletracker.databinding.FragmentManagementBinding;

public class ManagementFragment extends Fragment {
    private FragmentManagementBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentManagementBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        MaterialButton button = binding.newTreeButton;
        button.setOnClickListener(v -> {

            NavHostFragment.findNavController(ManagementFragment.this).navigate(R.id.navigation_new_tree);
            Log.d("BUTTONS", "User tapped the New Tree Button");
        });
        MaterialButton button2 = binding.editTreeButton;
        button2.setOnClickListener(v -> {

            NavHostFragment.findNavController(ManagementFragment.this).navigate(R.id.navigation_edit_tree);
            Log.d("BUTTONS", "User tapped the Edit Tree Button");
        });

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
