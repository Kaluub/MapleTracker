package com.ocdsb.mapletracker.ui.management;

import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.material.button.MaterialButton;
import com.ocdsb.mapletracker.R;
import com.ocdsb.mapletracker.databinding.FragmentEditTreeBinding;
import com.ocdsb.mapletracker.databinding.FragmentManagementBinding;
import com.ocdsb.mapletracker.databinding.FragmentNewTreeBinding;

public class EditTreeFragment extends Fragment {

    private EditTreeViewModel mViewModel;
    private FragmentEditTreeBinding binding;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentEditTreeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        //Get user text input
        EditText editText = (EditText) root.findViewById(R.id.editName);
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
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(EditTreeViewModel.class);
        // TODO: Use the ViewModel
    }

}