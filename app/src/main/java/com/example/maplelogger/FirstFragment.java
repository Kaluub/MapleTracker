package com.example.maplelogger;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.example.maplelogger.databinding.FragmentFirstBinding;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

public class FirstFragment extends Fragment {

    private FragmentFirstBinding binding;

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {

        binding = FragmentFirstBinding.inflate(inflater, container, false);
        return binding.getRoot();

    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.buttonFirst.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavHostFragment.findNavController(FirstFragment.this)
                        .navigate(R.id.action_FirstFragment_to_SecondFragment);
            }
        });

            public boolean navButton BottomNavigationView.OnNavigationItemSelectedListener { item ->
                    when(item.itemId) {
                R.id.Home -> {
                    // Respond to navigation item 1 click
                    true
                }
                R.id.item2 -> {
                    // Respond to navigation item 2 click
                    true
                }
                else -> false
                }
            }

        }



    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}