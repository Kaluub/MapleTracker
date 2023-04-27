package com.ocdsb.mapletracker.ui.management;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.google.android.material.button.MaterialButton;
import com.ocdsb.mapletracker.R;
import com.ocdsb.mapletracker.databinding.FragmentEditTreeBinding;

public class EditTreeFragment extends Fragment {
    private FragmentEditTreeBinding binding;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentEditTreeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        //Get user text input
        EditText editText = root.findViewById(R.id.editName);
        System.out.println("This is edit text: " + editText);
        System.out.println(editText.getText());
        MaterialButton button = binding.saveButton;
        System.out.println(button);
        button.setOnClickListener(v -> {
            System.out.println("The rad user tapped the cool button");
            System.out.println(editText.getText());
        });
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}