package eu.org.yexiaoye.passwordgeneration.ui.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import eu.org.yexiaoye.passwordgeneration.R;
import eu.org.yexiaoye.passwordgeneration.bean.PasswordInfo;
import eu.org.yexiaoye.passwordgeneration.dao.PasswordDao;
import eu.org.yexiaoye.passwordgeneration.dao.impl.PasswordDaoImpl;
import eu.org.yexiaoye.passwordgeneration.util.DatabaseUtil;

public class PasswordListAdapter extends RecyclerView.Adapter<PasswordListAdapter.InnerViewHolder> implements IOperationData{

    private static final String TAG = "PasswordListAdapter";
    private Context mContext;
    private OnRecyclerViewItemClickListener mItemClickListener;
    private OnRecyclerViewItemLongClickListener mItemLongClickListener;

    private List<PasswordInfo> mPasswordInfoList = new ArrayList<>();

    public PasswordListAdapter(Context context) {
        mContext = context;
    }

    /**
     * 对外提供设置数据的接口
     * @param passwordInfoList  密码信息集合
     */
    @SuppressLint("NotifyDataSetChanged")
    public void setData(List<PasswordInfo> passwordInfoList) {
        if (passwordInfoList != null ) {
            mPasswordInfoList.clear();
            mPasswordInfoList.addAll(passwordInfoList);
        }

        // 更新UI
        notifyDataSetChanged();
    }
    /**
     * 改变信息显示列表的状态
     * @param isShowCheckBox    true 表示编辑状态，false，表示默认显示状态
     */
    public void changeListState(boolean isShowCheckBox) {

    }

    public void setOnItemClickListener(OnRecyclerViewItemClickListener listener) {
        this.mItemClickListener = listener;
    }

    public void setOnItemLongClickListener(OnRecyclerViewItemLongClickListener listener) {
        this.mItemLongClickListener = listener;
    }

    @NonNull
    @Override
    public InnerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(mContext).inflate(R.layout.item_password_info,parent,false);
        return new InnerViewHolder(itemView, mItemClickListener,mItemLongClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull InnerViewHolder holder, int position) {
        // 设置数据
        holder.showDataPage(mPasswordInfoList.get(position));
    }

    @Override
    public int getItemCount() {
        return mPasswordInfoList == null ? 0 : mPasswordInfoList.size();
    }

    @Override
    public void onItemMove(int fromPosition, int destPosition) {
        // 交换数据并通知页面更新
        Collections.swap(mPasswordInfoList,fromPosition,destPosition);

        // 修改数据库,也就是修改 两条数据的index
        // PasswordDao dao = new PasswordDaoImpl(DatabaseUtil.getInstance(mContext));
        // dao.update()

        notifyItemMoved(fromPosition,destPosition);
    }

    @Override
    public void onItemRemove(int position) {
        mPasswordInfoList.remove(position);
        notifyItemRemoved(position);
    }

    public static class InnerViewHolder extends RecyclerView.ViewHolder {

        public TextView mTxtPassword,mTxtRemark,mTxtStoredDate;

        public InnerViewHolder(@NonNull View itemView,
                               OnRecyclerViewItemClickListener itemClickListener,OnRecyclerViewItemLongClickListener itemLongClickListener) {
            super(itemView);
            mTxtPassword = itemView.findViewById(R.id.txt_password);
            mTxtRemark = itemView.findViewById(R.id.txt_remark);
            mTxtStoredDate = itemView.findViewById(R.id.txt_stored_date);

            // 设置监听
            if (itemClickListener != null) {
                itemView.setOnClickListener(v -> {
                    itemClickListener.onItemClick(v,getAdapterPosition());
                });
            }

            if (itemLongClickListener != null) {
                itemView.setOnLongClickListener(v -> {
                    itemLongClickListener.onItemLongClick(v,getAdapterPosition());
                    return false;
                });
            }

        }

        @SuppressLint("SetTextI18n")
        public void showDataPage(PasswordInfo passwordInfo) {
            mTxtPassword.setText(passwordInfo.getPwdContent());
            mTxtRemark.setText(passwordInfo.getRemark());
            mTxtStoredDate.setText(passwordInfo.getDateStored());
        }

    }

    public interface OnRecyclerViewItemClickListener {
        void onItemClick(View view,int position);
    }

    public interface OnRecyclerViewItemLongClickListener {
        void onItemLongClick(View view,int position);
    }
}
