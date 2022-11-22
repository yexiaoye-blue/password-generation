package eu.org.yexiaoye.passwordgeneration.base;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public abstract class BaseFragment extends Fragment {

    private View rootView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = onSubViewLoaded(inflater,container);
        initView(rootView);
        loadData();
        registerListener();
        return rootView;
    }

    protected abstract void registerListener();

    protected abstract void loadData();

    protected abstract void initView(View rootView);

    protected abstract View onSubViewLoaded(LayoutInflater inflater, ViewGroup container);
}
