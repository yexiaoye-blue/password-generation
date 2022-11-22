package eu.org.yexiaoye.passwordgeneration.ui.activity;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.os.Build;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import eu.org.yexiaoye.passwordgeneration.R;
import eu.org.yexiaoye.passwordgeneration.base.BaseActivity;
import eu.org.yexiaoye.passwordgeneration.bean.PasswordInfo;
import eu.org.yexiaoye.passwordgeneration.dao.PasswordDao;
import eu.org.yexiaoye.passwordgeneration.dao.impl.PasswordDaoImpl;


import eu.org.yexiaoye.passwordgeneration.databinding.ActivityPwdRepositoryBinding;
import eu.org.yexiaoye.passwordgeneration.ui.adapter.PasswordListAdapter;
import eu.org.yexiaoye.passwordgeneration.ui.customview.CommonDialog;
import eu.org.yexiaoye.passwordgeneration.util.DatabaseUtil;
import eu.org.yexiaoye.passwordgeneration.util.LogUtil;

public class PwdRepositoryActivity extends BaseActivity<ActivityPwdRepositoryBinding> {

    private static final String TAG = "PwdListActivity";
    private PasswordDao mDao;

    private RecyclerView mRecyclerView;
    private TextView mTxtEmptyDataPage;
    private List<PasswordInfo> mPasswordInfoList;

    private boolean isEmptyData = false;
    private PasswordListAdapter mPasswordListAdapter;
    private int mCurrentDataSize;

    @Override
    protected ActivityPwdRepositoryBinding onCreateViewBinding(LayoutInflater layoutInflater) {
        return ActivityPwdRepositoryBinding.inflate(getLayoutInflater());
    }

    @Override
    protected void registerListener() {
        // TODO:长按拖动排序，修改数据库（由于性能问题，此功能是否实现待考虑）

        /*mPasswordListAdapter.setOnItemClickListener((view, position) -> {
            // Toast.makeText(PwdRepositoryActivity.this,"position = " + position,Toast.LENGTH_SHORT).show();
        });

        mPasswordListAdapter.setOnItemLongClickListener((view,position) -> {
            Toast.makeText(PwdRepositoryActivity.this,"position = " + position,Toast.LENGTH_SHORT).show();
        });*/

        // 返回
        getViewBinding().flContainerBack.setOnClickListener(v -> {
            onBackPressed();
        });

    }

    @Override
    protected void loadData() {
        if (!isEmptyData) {
            mRecyclerView.setVisibility(View.VISIBLE);
            mTxtEmptyDataPage.setVisibility(View.GONE);

            mRecyclerView.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false));
            mPasswordListAdapter = new PasswordListAdapter(this);
            mRecyclerView.setAdapter(mPasswordListAdapter);

            // 是否开启长按排序，若开启，在数据量大的时候，可能影响程序执行速度
            /*ItemTouchHelper helper = new ItemTouchHelper(new CustomItemTouchCallback(mPasswordListAdapter,mPasswordInfoList));
            helper.attachToRecyclerView(mRecyclerView);*/


            mPasswordListAdapter.setData(mPasswordInfoList);
            mCurrentDataSize = mPasswordInfoList.size();

            mPasswordListAdapter.setOnItemLongClickListener(((view, position) -> {

                CommonDialog dialog = new CommonDialog.Builder(this)
                        .setLayoutResID(R.layout.dialog_asking_delete)
                        .setOnClickListener(new CommonDialog.CommonDialogClickListener() {
                            @Override
                            public void onConfirmClick(Dialog dialog) {
                                PasswordInfo passwordInfo = mPasswordInfoList.get(position);

                                dialog.dismiss();

                                // 删除数据库中的数据
                                if (1 == mDao.deleteByPassword(passwordInfo.getPwdContent())) {
                                    //  移除当前列表中的数据
                                    mPasswordInfoList.remove(position);
                                    // 通知页面更新
                                    mPasswordListAdapter.onItemRemove(position);
                                    mCurrentDataSize -= 1;
                                    LogUtil.d(TAG,"删除成功！");
                                }else {
                                    Toast.makeText(PwdRepositoryActivity.this,"删除失败！，未知错误！",Toast.LENGTH_SHORT).show();
                                }

                                if (mCurrentDataSize == 0) {
                                    mRecyclerView.setVisibility(View.GONE);
                                    mTxtEmptyDataPage.setVisibility(View.VISIBLE);
                                    LogUtil.d(TAG,"没有数据啦！");
                                }else {
                                    LogUtil.d(TAG,"数据长度为" + mCurrentDataSize);
                                }
                            }

                            @Override
                            public void onCancelClick(Dialog dialog) {
                                dialog.dismiss();
                            }
                        }).build();
                dialog.show();
            }));

            mPasswordListAdapter.setOnItemClickListener(((view, position) -> {
                PasswordInfo passwordInfo = mPasswordInfoList.get(position);
                final String[] oldPwd = {passwordInfo.getPwdContent()};
                final String[] oldRemark = {passwordInfo.getRemark()};
                int index = passwordInfo.getIndex();

                // 修改或复制，0为复制,1为修改
                final int[] modifiedOrCopy = {0};

                CommonDialog build = new CommonDialog.Builder(this)
                        .setTitle("修改或复制")
                        .setFirstContent(oldPwd[0])
                        .setSecondContent(oldRemark[0])
                        .setConfirmButtonText("复制")
                        .setDialogTextChangeListener(new CommonDialog.CommonDialogTextChangeListener() {
                            @Override
                            public void onFirstContentChange(Dialog dialog) {
                                // 当输入框发生修改时，则模式更改为  修改
                                ((CommonDialog) dialog).setCsConfirmText("确认");
                                modifiedOrCopy[0] = 1;
                            }

                            @Override
                            public void onSecondContentChange(Dialog dialog) {
                                ((CommonDialog) dialog).setCsConfirmText("确认");
                                modifiedOrCopy[0] = 1;
                            }
                        })
                        .setLayoutResID(R.layout.dialog_common)
                        .setOnClickListener(new CommonDialog.CommonDialogClickListener() {
                            @RequiresApi(api = Build.VERSION_CODES.M)
                            @SuppressLint("ResourceAsColor")
                            @Override
                            public void onConfirmClick(Dialog dialog) {

                                String currentFirstContent = ((CommonDialog) dialog).getFirstEditText().getText().toString().trim();
                                String currentSecondContent = ((CommonDialog) dialog).getSecondEditText().getText().toString().trim();

                                if (modifiedOrCopy[0] == 1) {
                                    boolean modifiedPwd = false;
                                    boolean modifiedRemark = false;

                                    ((CommonDialog) dialog).getFirstEditText().addTextChangedListener(new TextWatcher() {
                                        @Override
                                        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                                        }

                                        @RequiresApi(api = Build.VERSION_CODES.M)
                                        @Override
                                        public void onTextChanged(CharSequence s, int start, int before, int count) {
                                            ((CommonDialog) dialog).setCsTitleText("修改或复制");
                                            ((CommonDialog) dialog).setTxtTitleColor(getColor(R.color.black));
                                        }

                                        @Override
                                        public void afterTextChanged(Editable s) {
                                        }
                                    });

                                    // 判断密码是否相同，若相同则不修改
                                    if (!oldPwd[0].equals(currentFirstContent)) {

                                        // 密码长度判断
                                        if (currentFirstContent.length() < 6 || currentFirstContent.length() > 32) {
                                            ((CommonDialog) dialog).setCsTitleText("密码长度不合法!");
                                            ((CommonDialog) dialog).setTxtTitleColor(getColor(R.color.pink));
                                            return;
                                        }

                                        // 密码是已经否存在判断
                                        for (PasswordInfo passwordInfo : mPasswordInfoList) {
                                            if (passwordInfo.getPwdContent().equals(currentFirstContent)) {
                                                ((CommonDialog) dialog).setCsTitleText("密码已经存在啦！");
                                                ((CommonDialog) dialog).setTxtTitleColor(R.color.pink);
                                                return;
                                            }
                                        }

                                        modifiedPwd = true;
                                        oldPwd[0] = currentFirstContent;
                                    }

                                    if (!oldRemark[0].equals(currentSecondContent)) {
                                        modifiedRemark = true;
                                        oldRemark[0] = currentSecondContent;
                                    }
                                    dialog.dismiss();

                                    // 只要一项修改，那么我们就应该修改数据
                                    if (modifiedPwd || modifiedRemark) {
                                        passwordInfo.setPwdContent(oldPwd[0]);
                                        passwordInfo.setRemark(oldRemark[0]);
                                        mPasswordInfoList.remove(position);
                                        mPasswordInfoList.add(position,passwordInfo);
                                        // 更新页面
                                        mPasswordListAdapter.setData(mPasswordInfoList);
                                        int update = mDao.update(new PasswordInfo(index, oldPwd[0], oldRemark[0]));
                                        if (update == 1) {
                                            // mPasswordListAdapter.setData();
                                            LogUtil.d(TAG,"修改成功！newPwd=" + oldPwd[0] + ", newRemark=" +  oldRemark[0]);
                                        }else {
                                            Toast.makeText(PwdRepositoryActivity.this,"修改失败，未知错误！",Toast.LENGTH_SHORT).show();
                                            LogUtil.d(TAG,"修改失败！");
                                        }
                                    }
                                }else {
                                    dialog.dismiss();
                                    ClipData clipData = ClipData.newPlainText("Generated Password",currentFirstContent);
                                    ClipboardManager clipboardManager = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                                    clipboardManager.setPrimaryClip(clipData);
                                    Toast.makeText(PwdRepositoryActivity.this,"复制成功！",Toast.LENGTH_SHORT).show();
                                }

                            }

                            @Override
                            public void onCancelClick(Dialog dialog) {
                                dialog.dismiss();
                            }
                        }).build();
                build.show();

            }));

        }else {
            mRecyclerView.setVisibility(View.GONE);
            mTxtEmptyDataPage.setVisibility(View.VISIBLE);
        }

    }

    @Override
    protected void initView() {
        mRecyclerView = getViewBinding().recyclerView;
        mTxtEmptyDataPage = getViewBinding().txtEmptyDataPage;
    }

    @Override
    protected void requestFeature() {
        mDao = new PasswordDaoImpl(DatabaseUtil.getInstance(this));
        mPasswordInfoList = mDao.selectAll();
        /*for (PasswordInfo info : mPasswordInfoList) {
            LogUtil.d(TAG,"====>" + info.getIndex());
        }*/
        if (mPasswordInfoList == null || mPasswordInfoList.size() == 0) {
            isEmptyData = true;
        }else {
            isEmptyData = false;
        }
    }
}

