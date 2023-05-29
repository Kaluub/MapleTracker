package com.ocdsb.mapletracker.ui.management;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import com.google.android.material.snackbar.Snackbar;
import com.ocdsb.mapletracker.Config;
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


        binding.newTreeButton.setOnClickListener(view -> startActivity(new Intent(requireContext(), NewTreeActivity.class)));
        binding.editTreeButton.setOnClickListener(view -> startActivity(new Intent(requireContext(), EditTreeActivity.class)));

        if (Config.debugMode) {
            Button debugButton = binding.debug;
            debugButton.setVisibility(View.VISIBLE);
            debugButton.setOnClickListener(v -> {
                AlertDialog.Builder alert = new AlertDialog.Builder(requireActivity());
                alert.setTitle("Create random trees");

                final int ARRAY_SIZE = 101;
                final int SKIP = 10;

                CharSequence[] names = new CharSequence[ARRAY_SIZE];

                int x = 0;
                while (x < ARRAY_SIZE) {
                    names[x] = String.valueOf(x*SKIP);
                    x += 1;
                }

                alert.setItems(names, (dialogInterface, j) -> {
                    MapAPI mapAPI = new MapAPI();
                    ThreadLocalRandom random = ThreadLocalRandom.current();
                    int i = 0;
                    while (i < j*SKIP) {
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
                    dialogInterface.dismiss();
                    Snackbar.make(v, "Created " + j*SKIP + " testing trees.", Snackbar.LENGTH_SHORT).show();
                });

                alert.show();
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
