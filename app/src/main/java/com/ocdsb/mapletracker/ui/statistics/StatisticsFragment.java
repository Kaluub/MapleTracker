package com.ocdsb.mapletracker.ui.statistics;

import android.content.res.Resources;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

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

        for (TreePin tree: mapAPI.treePins) {
            totalSapCollected += tree.sapLitresCollectedTotal;
            resettableSapCollected += tree.sapLitresCollectedResettable;
            totalEdits += tree.editsTotal;
            resettableEdits += tree.editsResettable;

            Marker marker = new Marker(map);
            marker.setTitle(String.format(
                    resources.getString(R.string.statistics_tree_title),
                    tree.name,
                    tree.sapLitresCollectedTotal,
                    tree.sapLitresCollectedResettable
            ));
            marker.setPosition(new GeoPoint(tree.latitude, tree.longitude));
            map.getOverlays().add(marker);
        }

        statistics.add(String.format(resources.getString(R.string.statistics_tree_count), treeCount));
        statistics.add(String.format(resources.getString(R.string.statistics_total_sap), totalSapCollected));
        statistics.add(String.format(resources.getString(R.string.statistics_average_sap), totalSapCollected/treeCount));
        statistics.add(String.format(resources.getString(R.string.statistics_sap_yearly), resettableSapCollected));
        statistics.add(String.format(resources.getString(R.string.statistics_average_sap_yearly), resettableSapCollected/treeCount));
        statistics.add(String.format(resources.getString(R.string.statistics_total_syrup), totalSapCollected/40));
        statistics.add(String.format(resources.getString(R.string.statistics_average_syrup), totalSapCollected/treeCount/40));
        statistics.add(String.format(resources.getString(R.string.statistics_syrup_yearly), resettableSapCollected/40));
        statistics.add(String.format(resources.getString(R.string.statistics_average_syrup_yearly), resettableSapCollected/treeCount/40));
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