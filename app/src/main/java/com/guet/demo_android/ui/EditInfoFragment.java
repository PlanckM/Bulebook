package com.guet.demo_android.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.guet.demo_android.databinding.FragmentEditInfoBinding;

public class EditInfoFragment extends Fragment {
    private FragmentEditInfoBinding binding;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding=FragmentEditInfoBinding.inflate(inflater,container,false);
        View root=binding.getRoot();
        return root;
    }
}
