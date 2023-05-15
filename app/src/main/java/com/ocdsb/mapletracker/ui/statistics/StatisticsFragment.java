package com.ocdsb.mapletracker.ui.statistics;

import android.content.res.Resources;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.ocdsb.mapletracker.Config;
import com.ocdsb.mapletracker.R;
import com.ocdsb.mapletracker.api.MapAPI;
import com.ocdsb.mapletracker.data.TreePin;
import com.ocdsb.mapletracker.databinding.FragmentStatisticsBinding;

import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Marker;
import org.osmdroid.views.overlay.infowindow.MarkerInfoWindow;

import java.util.ArrayList;

public class StatisticsFragment extends Fragment {

    private FragmentStatisticsBinding binding;
    private MapView map;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentStatisticsBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        map = binding.map;
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

        String units = getString(R.string.unit_litres);
        if (Config.useGallons) {
            units = getString(R.string.unit_gallons);
        }

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
                    String.format(units, displayTotalSap),
                    String.format(units, displayResettableSap)
            ));
            marker.setPosition(new GeoPoint(tree.latitude, tree.longitude));
            map.getOverlays().add(marker);
        }

        if (Config.useGallons) {
            totalSapCollected /= 3.785;
            resettableSapCollected /= 3.785;
        }

        statistics.add(String.format(resources.getString(R.string.statistics_tree_count), treeCount));
        statistics.add(String.format(resources.getString(R.string.statistics_total_sap), String.format(units, totalSapCollected)));
        statistics.add(String.format(resources.getString(R.string.statistics_average_sap), String.format(units, totalSapCollected/treeCount)));
        statistics.add(String.format(resources.getString(R.string.statistics_sap_yearly), String.format(units, resettableSapCollected)));
        statistics.add(String.format(resources.getString(R.string.statistics_average_sap_yearly), String.format(units, resettableSapCollected/treeCount)));
        statistics.add(String.format(resources.getString(R.string.statistics_total_syrup), String.format(units, totalSapCollected/40)));
        statistics.add(String.format(resources.getString(R.string.statistics_average_syrup), String.format(units, totalSapCollected/treeCount/40)));
        statistics.add(String.format(resources.getString(R.string.statistics_syrup_yearly), String.format(units, resettableSapCollected/40)));
        statistics.add(String.format(resources.getString(R.string.statistics_average_syrup_yearly), String.format(units, resettableSapCollected/treeCount/40)));
        statistics.add(String.format(resources.getString(R.string.statistics_edits), totalEdits));
        statistics.add(String.format(resources.getString(R.string.statistics_edits_yearly), resettableEdits));

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