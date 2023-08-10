package com.guet.demo_android.ui.reflow;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.guet.demo_android.MainActivity;
import com.guet.demo_android.databinding.FragmentReflowBinding;
//是Fragment类的一个子类，表示应用程序用户界面的一部分。
public class ReflowFragment extends Fragment {

    private FragmentReflowBinding binding;

    //onCreateView()方法是由Android系统的Fragment管理器（FragmentManager）在需要显示Fragment时调用的。
    //负责将片段的布局文件实例化，设置ViewModel，并将UI组件与ViewModel中的数据关联起来。
    //LayoutInflater inflater: 用于将XML布局文件转换为对应的View对象。
    //iewGroup container: 是Fragment所附加的父容器View。
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //初始化ViewModel
        //这里使用ViewModelProvider来创建或获取ReflowViewModel的实例。ViewModel是用来管理UI相关数据和业务逻辑的类，
        //使用ViewModel可以将数据与UI组件（如Fragment）分离，避免配置变更等情况下数据丢失，并提供更好的代码组织和维护。
        ReflowViewModel reflowViewModel = new ViewModelProvider(this).get(ReflowViewModel.class);
        MainActivity a= (MainActivity) getActivity();
        a.setBottomVisible();
        //创建View绑定（View Binding）：
        //这里使用FragmentReflowBinding.inflate()方法来实例化Fragment的布局，并得到与该布局相关联的FragmentReflowBinding对象。
        //FragmentReflowBinding是一个自动生成的绑定类，它能够让你轻松地访问布局中的视图和组件。
        //方法将Fragment的布局文件实例化为一个View对象，然后将该View对象添加到container中，最后返回该View对象作为Fragment的用户界面。
        binding = FragmentReflowBinding.inflate(inflater, container, false);
        //获取根视图：
        //通过getRoot()方法，可以得到Fragment的根视图。根视图是整个Fragment布局的最顶层视图。
        View root = binding.getRoot();

        //设置数据与视图的关联：
        //这里通过binding对象找到在布局中定义的名为textReflow的TextView，并将其赋值给textView变量。
        final TextView textView = binding.textReflow;

        //观察LiveData的变化：
        //通过reflowViewModel.getText()方法获取ReflowViewModel中的LiveData对象mText，然后使用observe()方法观察该LiveData的变化。
        //当mText的值发生变化时，这个观察者会自动被通知，然后调用textView::setText来更新textView的文本内容。
        //这样，textView的文本将始终与mText的值保持同步。
        reflowViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);

        //最后，将根视图root返回给系统，这样系统就能将这个视图显示在屏幕上，作为Fragment的用户界面。
        //返回的视图添加到Activity的布局中
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}