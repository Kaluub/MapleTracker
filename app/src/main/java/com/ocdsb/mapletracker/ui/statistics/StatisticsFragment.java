package com.ocdsb.mapletracker.ui.statistics;

import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.fragment.app.Fragment;

import com.ocdsb.mapletracker.Config;
import com.ocdsb.mapletracker.R;
import com.ocdsb.mapletracker.api.MapAPI;
import com.ocdsb.mapletracker.data.TreePin;
import com.ocdsb.mapletracker.databinding.FragmentStatisticsBinding;

import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Marker;

import java.util.ArrayList;

public class StatisticsFragment extends Fragment {

    private FragmentStatisticsBinding binding;
    public String units;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentStatisticsBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        MapView map = binding.map;
        Resources resources = getResources();

        MapAPI mapAPI = new MapAPI();
        map = mapAPI.buildMap(map);
        ArrayList<String> statistics = new ArrayList<>();
        statistics.add(resources.getString(R.string.title_statistics));

        int treeCount = mapAPI.treePins.toArray().length;
        double totalSapCollected = 0;
        double resettableSapCollected = 0;
        int totalEdits = 0;
        int resettableEdits = 0;

        units = getString(R.string.unit_litres);
        if (Config.useGallons) {
            units = getString(R.string.unit_gallons);
        }

        // Get the tree icon.
        Drawable treeIcon = AppCompatResources.getDrawable(requireContext(), R.drawable.baseline_park);

        for (TreePin tree: mapAPI.treePins) {
            totalSapCollected += tree.sapLitresCollectedTotal;
            resettableSapCollected += tree.sapLitresCollectedResettable;
            totalEdits += tree.editsTotal;
            resettableEdits += tree.editsResettable;

            double displayTotalSap = tree.sapLitresCollectedTotal;
            double displayResettableSap = tree.sapLitresCollectedResettable;

            if (Config.useGallons) {
                displayTotalSap /= 3.785;
                displayResettableSap /= 3.785;
            }

            Marker marker = new Marker(map);
            marker.setTitle(String.format(
                    resources.getString(R.string.statistics_tree_title),
                    tree.name,
                    formatUnits(displayTotalSap),
                    formatUnits(displayResettableSap)
            ));
            marker.setIcon(treeIcon);
            marker.setPosition(new GeoPoint(tree.latitude, tree.longitude));
            map.getOverlays().add(marker);
        }

        // Since Americans exist we need to account for their problems
        if (Config.useGallons) {
            totalSapCollected /= 3.785;
            resettableSapCollected /= 3.785;
        }

        // Calculate averages.
        double averageSap = totalSapCollected/treeCount;
        double averageSapResettable = resettableSapCollected/treeCount;
        double averageSyrup = totalSapCollected/treeCount/40;
        double averageSyrupResettable = resettableSapCollected/treeCount/40;

        // Fix the division by zero bug. Technically.
        if (treeCount == 0) {
            averageSap = 0;
            averageSapResettable = 0;
            averageSyrup = 0;
            averageSyrupResettable = 0;
        }

        statistics.add(String.format(resources.getString(R.string.statistics_tree_count), treeCount));
        statistics.add(String.format(resources.getString(R.string.statistics_total_sap), formatUnits(totalSapCollected)));
        statistics.add(String.format(resources.getString(R.string.statistics_average_sap), formatUnits(averageSap)));
        statistics.add(String.format(resources.getString(R.string.statistics_sap_yearly), formatUnits(resettableSapCollected)));
        statistics.add(String.format(resources.getString(R.string.statistics_average_sap_yearly), formatUnits(averageSapResettable)));
        statistics.add(String.format(resources.getString(R.string.statistics_total_syrup), formatUnits(totalSapCollected/40)));
        statistics.add(String.format(resources.getString(R.string.statistics_average_syrup), formatUnits(averageSyrup)));
        statistics.add(String.format(resources.getString(R.string.statistics_syrup_yearly), formatUnits(resettableSapCollected/40)));
        statistics.add(String.format(resources.getString(R.string.statistics_average_syrup_yearly), formatUnits(averageSyrupResettable)));
        statistics.add(String.format(resources.getString(R.string.statistics_edits), totalEdits));
        statistics.add(String.format(resources.getString(R.string.statistics_edits_yearly), resettableEdits));

        final TextView textView = binding.textStatistics;
        textView.setText(String.join("\n• ", statistics));

        return root;
    }
    
    public String formatUnits(double litres) {
        return String.format(units, litres);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}