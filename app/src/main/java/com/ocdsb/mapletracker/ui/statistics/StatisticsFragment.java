package com.ocdsb.mapletracker.ui.statistics;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.ocdsb.mapletracker.api.MapAPI;
import com.ocdsb.mapletracker.data.TreePin;
import com.ocdsb.mapletracker.databinding.FragmentStatisticsBinding;

import java.util.ArrayList;

public class StatisticsFragment extends Fragment {

    private FragmentStatisticsBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentStatisticsBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        MapAPI mapAPI = new MapAPI();
        mapAPI.loadPins(requireContext());
        ArrayList<String> statistics = new ArrayList<>();

        int treeCount = mapAPI.treePins.toArray().length;
        double totalSapCollected = 0;
        double resettableSapCollected = 0;
        int totalEdits = 0;
        int resettableEdits = 0;

        for (TreePin tree: mapAPI.treePins) {
            totalSapCollected += tree.sapLitresCollectedTotal;
            resettableSapCollected += tree.sapLitresCollectedResettable;
            totalEdits += tree.editsTotal;
            resettableEdits += tree.editsResettable;
        }

        statistics.add("Tree count: " + treeCount);
        statistics.add("Total sap collected: " + totalSapCollected);
        statistics.add("Average sap per tree: " + totalSapCollected/treeCount);
        statistics.add("Sap collected this season: " + resettableSapCollected);
        statistics.add("Average tree yield this season: " + resettableSapCollected/treeCount);
        statistics.add("Average total maple syrup yield: " + totalSapCollected/treeCount/40);
        statistics.add("Estimated total maple syrup yield: " + totalSapCollected/40);
        statistics.add("Estimated season maple yield: " + resettableSapCollected/40);
        statistics.add("Estimated maple syrup per tree: " + resettableSapCollected/treeCount/40);
        statistics.add("Taps logged in total: " + totalEdits);
        statistics.add("Taps logged this season: " + resettableEdits);

        final TextView textView = binding.textStatistics;
        textView.setText(String.join("\n• ", statistics));

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}