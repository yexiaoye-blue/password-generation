package eu.org.yexiaoye.passwordgeneration.base;

import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.WindowManager;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewbinding.ViewBinding;

import eu.org.yexiaoye.passwordgeneration.util.LogUtil;

/**
 * Activity基类，使用ViewBinging绑定布局
 */
public abstract class BaseActivity<VB extends ViewBinding> extends AppCompatActivity {

    private VB mBinding;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mBinding = onCreateViewBinding(getLayoutInflater());
        requestFeature();
        setContentView(mBinding.getRoot());

        LogUtil.initLog(this.getPackageName(),false);
        initView();
        loadData();
        registerListener();
    }

    protected abstract VB onCreateViewBinding(LayoutInflater layoutInflater);

    protected abstract void registerListener();
    protected abstract void loadData();
    protected abstract void initView();

    protected void requestFeature() {
        // 经测试在代码里直接声明透明状态栏更有效
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS,
                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
    }

    public VB getViewBinding() {
        return mBinding;
    }
}
