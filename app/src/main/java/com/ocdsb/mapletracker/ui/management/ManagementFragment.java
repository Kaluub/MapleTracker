package com.ocdsb.mapletracker.ui.management;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;


import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.snackbar.Snackbar;
import com.ocdsb.mapletracker.Config;
import com.ocdsb.mapletracker.R;
import com.ocdsb.mapletracker.api.MapAPI;
import com.ocdsb.mapletracker.data.TreePin;
import com.ocdsb.mapletracker.databinding.FragmentManagementBinding;

import java.util.concurrent.ThreadLocalRandom;

public class ManagementFragment extends Fragment {
    private FragmentManagementBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentManagementBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        MaterialButton newTreeButton = binding.newTreeButton;
        newTreeButton.setOnClickListener(v -> {
            NavHostFragment.findNavController(this).navigate(R.id.navigation_new_tree);
            Log.d("BUTTONS", "User tapped the New Tree Button");
        });

        MaterialButton editTreeButton = binding.editTreeButton;
        editTreeButton.setOnClickListener(v -> {
            NavHostFragment.findNavController(this).navigate(R.id.navigation_edit_tree);
            Log.d("BUTTONS", "User tapped the Edit Tree Button");
        });

        if (Config.debugMode) {
            Button debugButton = binding.debug;
            debugButton.setVisibility(View.VISIBLE);
            debugButton.setOnClickListener(v -> {
                MapAPI mapAPI = new MapAPI();
                ThreadLocalRandom random = ThreadLocalRandom.current();
                int i = 0;
                while (i < 500) {
                    TreePin tree = new TreePin();
                    tree.longitude = random.nextDouble(-180.0, 180.0);
                    tree.latitude = random.nextDouble(-85.0, 85.0);
                    tree.sapLitresCollectedTotal = random.nextDouble(10.0, 100.0);
                    tree.sapLitresCollectedResettable = random.nextDouble(tree.sapLitresCollectedTotal);
                    tree.editsTotal = random.nextInt(5, 50);
                    tree.editsResettable = random.nextInt(tree.editsTotal);
                    tree.name = "Random tree " + i;
                    mapAPI.treePins.add(tree);
                    i += 1;
                }
                mapAPI.savePins(requireContext());
                Snackbar.make(v, "Created a bunch of testing trees.", Snackbar.LENGTH_SHORT).show();
            });
        }

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
