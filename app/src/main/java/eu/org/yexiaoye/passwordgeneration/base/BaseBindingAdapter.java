package eu.org.yexiaoye.passwordgeneration.base;

import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewbinding.ViewBinding;

public abstract class BaseBindingAdapter<VB extends ViewBinding> extends RecyclerView.Adapter<BaseBindingAdapter.BaseHolder> {

    @NonNull
    @Override
    public BaseHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new BaseHolder(onBindingView(parent));
    }

    @Override
    public void onBindViewHolder(@NonNull BaseHolder holder, int position) {
        onBindingData(holder,position);
    }

    /**
     * 绑定布局，用于将子类的ViewBinding传递给BaseHolder()
     * @param parent    ViewGroup
     * @return          VB extends ViewBinding
     */
    protected abstract VB onBindingView(@NonNull ViewGroup parent);

    /**
     * 由RecyclerView调用，显示指定位置的数据
     * 这个方法应该更新ViewHolder itemView的内容，以反映给定位置上的项目。
     * @param holder    BaseHolder<VB>
     * @param position  item在数据中的位置
     */
    protected abstract void onBindingData(BaseHolder<VB> holder, int position);

    protected static class BaseHolder<VB extends ViewBinding> extends RecyclerView.ViewHolder {
        private final VB viewBinding;
        public BaseHolder(@NonNull VB viewBinding) {
            super(viewBinding.getRoot());
            this.viewBinding = viewBinding;
        }

        public VB getViewBinding() {
            return viewBinding;
        }
    }
}
