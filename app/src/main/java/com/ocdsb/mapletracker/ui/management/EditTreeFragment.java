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

import com.ocdsb.mapletracker.R;
import com.ocdsb.mapletracker.databinding.FragmentEditTreeBinding;
import com.ocdsb.mapletracker.databinding.FragmentNewTreeBinding;

public class EditTreeFragment extends Fragment {

    private EditTreeViewModel mViewModel;
    private FragmentEditTreeBinding binding;

    public static EditTreeFragment newInstance() {
        return new EditTreeFragment();
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentEditTreeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        EditText editText = (EditText)  root.findViewById(R.id.editName);
        editText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                boolean handled = false;
                if (actionId == EditorInfo.IME_ACTION_SEND) {
                    System.out.println("inside onEditorAction");
                    handled = true;
                }
                return handled;
            }
        });
        return inflater.inflate(R.layout.fragment_edit_tree, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(EditTreeViewModel.class);
        // TODO: Use the ViewModel
    }

}