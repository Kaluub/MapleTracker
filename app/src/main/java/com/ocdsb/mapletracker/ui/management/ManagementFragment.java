package com.ocdsb.mapletracker.ui.management;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
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
    private FragmentManagementBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        // set the intent so that the correct class is opened
        Intent I = new Intent(requireContext(),NewTreeActivity.class);
        Intent J = new Intent(requireContext(),EditTreeActivity.class);
        // Object for getting the result from an activity
        ActivityResultLauncher<Intent> activityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                // If the user was in the new tree activity.
                if (result.getResultCode() == NewTreeActivity.RESULT_OK) {
                    Snackbar saveSnackBar = Snackbar.make(requireActivity().getWindow().getDecorView().getRootView(), R.string.tree_saved, Snackbar.LENGTH_SHORT);
                    View saveSnackBarView = saveSnackBar.getView();
                    saveSnackBarView.setTranslationY(-(convertDpToPixel(48,requireContext())));
                    saveSnackBar.show();
                    // if the user was in the edit tree activity
                } else if (result.getResultCode() == EditTreeActivity.RESULT_FIRST_USER) {
                    Snackbar saveSnackBar = Snackbar.make(requireActivity().getWindow().getDecorView().getRootView(),R.string.changes_saved, Snackbar.LENGTH_SHORT);
                    View saveSnackBarView = saveSnackBar.getView();
                    saveSnackBarView.setTranslationY(-(convertDpToPixel(48, requireContext())));
                    saveSnackBar.show();
                }
            });
        binding = FragmentManagementBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        //Gets a result from the activity when it is closed
        binding.newTreeButton.setOnClickListener(view -> activityResultLauncher.launch(I));
        binding.editTreeButton.setOnClickListener(view -> activityResultLauncher.launch(J));

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
