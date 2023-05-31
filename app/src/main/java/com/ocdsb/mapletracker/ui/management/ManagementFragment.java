package com.ocdsb.mapletracker.ui.management;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import com.google.android.material.snackbar.Snackbar;
import com.ocdsb.mapletracker.Config;
import com.ocdsb.mapletracker.R;
import com.ocdsb.mapletracker.api.MapAPI;
import com.ocdsb.mapletracker.data.TreePin;
import com.ocdsb.mapletracker.databinding.FragmentManagementBinding;

import java.util.concurrent.ThreadLocalRandom;

public class ManagementFragment extends Fragment {
    int LAUNCH_NEW_TREE_ACTIVITY = 1;
    int LAUNCH_EDIT_TREE_ACTIVITY = 2;
    private FragmentManagementBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        Intent I = new Intent(requireContext(),NewTreeActivity.class);
        Intent J = new Intent(requireContext(),EditTreeActivity.class);
        binding = FragmentManagementBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        //Gets a result from the activity when it is closed
        binding.newTreeButton.setOnClickListener(view -> startActivityForResult(I,LAUNCH_NEW_TREE_ACTIVITY));
        binding.editTreeButton.setOnClickListener(view -> startActivityForResult(J, LAUNCH_EDIT_TREE_ACTIVITY));

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
    //Handles the result from the new tree or edit tree activity
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //If the user was in the new tree activity ands saved their data
        if (requestCode == LAUNCH_NEW_TREE_ACTIVITY && resultCode == NewTreeActivity.RESULT_OK){
            Snackbar saveSnackBar = Snackbar.make(requireActivity().getWindow().getDecorView().getRootView(), R.string.tree_saved, Snackbar.LENGTH_SHORT);
            View saveSnackBarView = saveSnackBar.getView();
            saveSnackBarView.setTranslationY(-(convertDpToPixel(48,requireContext())));
            saveSnackBar.show();
            //If the user was in the edit tree activity and successfully made changes
        } else if (requestCode == LAUNCH_EDIT_TREE_ACTIVITY && resultCode == EditTreeActivity.RESULT_OK) {
            Snackbar saveSnackbar = Snackbar.make(requireActivity().getWindow().getDecorView().getRootView(),R.string.changes_saved, Snackbar.LENGTH_SHORT);
            View saveSnackBarView = saveSnackbar.getView();
            saveSnackBarView.setTranslationY(-(convertDpToPixel(48, requireContext())));
            saveSnackbar.show();
        }
    }
    //Method converts display pixels to actual pixels
    public static float convertDpToPixel(float dp, Context context){
        return dp * ((float) context.getResources().getDisplayMetrics().densityDpi / DisplayMetrics.DENSITY_DEFAULT);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
