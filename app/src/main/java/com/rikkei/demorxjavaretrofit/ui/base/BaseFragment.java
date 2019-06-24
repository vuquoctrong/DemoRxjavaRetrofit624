package com.rikkei.demorxjavaretrofit.ui.base;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.rikkei.demorxjavaretrofit.ui.NavigationManager;
import com.rikkei.demorxjavaretrofit.ui.main.MainActivity;

import butterknife.ButterKnife;
import butterknife.Unbinder;
import io.reactivex.annotations.Nullable;
import io.reactivex.disposables.CompositeDisposable;

public abstract class BaseFragment extends Fragment {

    private NavigationManager navigationManager;
    private Unbinder unbinder;
    private MainActivity activity;

    @LayoutRes
    protected abstract int layoutRes();

    /**
     * if return true, use super.onBackPressed()
     */
    public abstract boolean onBackPressed();

    protected abstract void initView();

    protected abstract CompositeDisposable dispose();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(layoutRes(), container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Bundle bundle = getArguments();
        if (bundle != null) {
            handleReceivedData(bundle);
        }

        initView();
    }

    public void handleReceivedData(Bundle bundle) {
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        activity = (MainActivity) context;
        navigationManager = activity.getNavigationManager();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (unbinder != null) {
            unbinder.unbind();
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        dispose().dispose();
        activity = null;
    }

    protected boolean isDuplicateClick() {
        return activity.isDuplicateClick();
    }

    protected MainActivity getMainActivity() {
        return activity;
    }

    protected NavigationManager getNavigationManager() {
        return navigationManager;
    }



}
