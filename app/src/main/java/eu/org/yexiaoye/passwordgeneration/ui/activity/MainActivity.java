package eu.org.yexiaoye.passwordgeneration.ui.activity;

import android.annotation.SuppressLint;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.text.Editable;
import android.text.TextUtils;
import android.view.LayoutInflater;

import android.view.View;
import android.widget.NumberPicker;
import android.widget.Toast;


import androidx.annotation.RequiresApi;

import java.util.List;

import eu.org.yexiaoye.passwordgeneration.base.BaseActivity;
import eu.org.yexiaoye.passwordgeneration.bean.PasswordInfo;
import eu.org.yexiaoye.passwordgeneration.dao.PasswordDao;
import eu.org.yexiaoye.passwordgeneration.dao.impl.PasswordDaoImpl;

import eu.org.yexiaoye.passwordgeneration.databinding.ActivityMainBinding;
import eu.org.yexiaoye.passwordgeneration.util.DatabaseUtil;
import eu.org.yexiaoye.passwordgeneration.util.PasswordGeneration;

public class MainActivity extends BaseActivity<ActivityMainBinding> {

    private static final String TAG = "MainActivity";

    // 获取剪切板管理器
    private ClipboardManager clipboardManager;
    private PasswordDao mPasswordDao;

    @Override
    protected ActivityMainBinding onCreateViewBinding(LayoutInflater layoutInflater) {
        return ActivityMainBinding.inflate(getLayoutInflater());
    }

    @Override
    protected void registerListener() {
        // 生成密码
        getViewBinding().btnGenerationPwd.setOnClickListener(v -> {
            boolean digitChecked = getViewBinding().cbDigit.isChecked();
            boolean lowerCaseChecked = getViewBinding().cbLowerCase.isChecked();
            boolean upperCaseChecked = getViewBinding().cbUpperCase.isChecked();
            boolean specialCharsChecked = getViewBinding().cbSpecialChars.isChecked();
            if (!(digitChecked || lowerCaseChecked || upperCaseChecked || specialCharsChecked)) {
                Toast.makeText(this,"请至少选择一个字符规则！",Toast.LENGTH_SHORT).show();
                return;
            }

            // 密码长度
            int pwdLength = getViewBinding().numPicker.getValue();
            String strPassword = PasswordGeneration.generatePassword(pwdLength, lowerCaseChecked, upperCaseChecked, digitChecked, specialCharsChecked);
            getViewBinding().etPwdContent.setText(strPassword);

        });

        // 复制密码
        getViewBinding().btnCopy.setOnClickListener(v -> {
            Editable etPwdContent = getViewBinding().etPwdContent.getText();
            if (TextUtils.isEmpty(etPwdContent)) {
                Toast.makeText(this,"请点击生成密码！",Toast.LENGTH_SHORT).show();
                return;
            }
            String strGenerationPwd = etPwdContent.toString();
            ClipData clipData = ClipData.newPlainText("Generated Password",strGenerationPwd);
            clipboardManager.setPrimaryClip(clipData);
            Toast.makeText(this,"复制成功！",Toast.LENGTH_SHORT).show();
        });

        // 保存密码
        getViewBinding().btnSavePwd.setOnClickListener(v -> {
            Editable etPwdContentText = getViewBinding().etPwdContent.getText();

            // 判空
            if (TextUtils.isEmpty(etPwdContentText)) {
                Toast.makeText(this,"请点击生成密码！",Toast.LENGTH_SHORT).show();
                return;
            }
            String strPwdContent = etPwdContentText.toString();

            // 查询数据库中是否存在此密码
            // PasswordInfo passwordInfo = mPasswordDao.selectByPassword(strPwdContent);
            List<PasswordInfo> passwordInfoList = mPasswordDao.selectAllIndexAndPassword();
            if (passwordInfoList.size() != 0) {
                for(PasswordInfo info : passwordInfoList) {
                    if (strPwdContent.equals(info.getPwdContent())) {
                        Toast.makeText(this,"已经存储此密码啦！",Toast.LENGTH_SHORT).show();
                        return;
                    }
                }
            }

            PasswordInfo newPasswordInfo = new PasswordInfo();
            newPasswordInfo.setIndex(passwordInfoList.size() + 1);
            newPasswordInfo.setPwdContent(strPwdContent);

            Editable etRemarkText = getViewBinding().etRemark.getText();
            if (!TextUtils.isEmpty(etRemarkText)) {
                newPasswordInfo.setRemark(etRemarkText.toString());
            }

            long insert = mPasswordDao.insert(newPasswordInfo);
            if (insert != -1) {
                getViewBinding().etRemark.setText("");
                Toast.makeText(this,"保存成功！",Toast.LENGTH_SHORT).show();
            }else {
                Toast.makeText(this,"啊哦~未知错误！",Toast.LENGTH_SHORT).show();
            }
        });

        // 查看已保存的密码
        getViewBinding().imgViewingPasswords.setOnClickListener(v -> {
            startActivity(new Intent(this, PwdRepositoryActivity.class));
        });

    }

    @Override
    protected void loadData() {
        clipboardManager = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
        mPasswordDao = new PasswordDaoImpl(DatabaseUtil.getInstance(this));
    }

    @RequiresApi(api = Build.VERSION_CODES.Q)
    @SuppressLint("ResourceAsColor")
    @Override
    protected void initView() {
        // NumberPicker自定义样式与数值
        NumberPicker numberPicker = getViewBinding().numPicker;
        numberPicker.setMaxValue(32);
        numberPicker.setMinValue(6);
        numberPicker.setTextSize(60);
        numberPicker.setTextColor(0xFF000000);
    }
}
