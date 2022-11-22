package eu.org.yexiaoye.passwordgeneration.ui.customview;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.ColorInt;

import eu.org.yexiaoye.passwordgeneration.R;

public class CommonDialog extends Dialog {

    private CharSequence mCsTitleText;
    private CharSequence mCsFirstContentText;
    private CharSequence mCsSecondContentText;
    private CharSequence mCsConfirmText, mCsCancelText;

    private final int layoutResID;
    private final CommonDialogClickListener mDialogClickListener;
    private final CommonDialogTextChangeListener mDialogTextChangeListener;

    private TextView mTxtTitle;
    private EditText mFirstEditText;
    private EditText mSecondEditText;
    private Button mBtnCancel;
    private Button mBtnConfirm;

    private CommonDialog(Context context,Builder builder) {
        super(context, builder.themeResID);
        this.layoutResID = builder.getLayoutResID();
        this.mCsTitleText = builder.getTxtTitleText();
        this.mCsFirstContentText = builder.getEtFirstContentText();
        this.mCsSecondContentText = builder.getEtSecondContentText();
        this.mCsConfirmText = builder.getConfirmButtonText();
        this.mCsCancelText = builder.getCancelButtonText();
        this.mDialogClickListener = builder.getDialogListener();
        this.mDialogTextChangeListener = builder.getDialogTextChangeListener();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(layoutResID);
        // 点击背景取消dialog
        setCanceledOnTouchOutside(true);

        // 设置背景透明（style文件设置透明不好用）
        Window window = getWindow();
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        initView();
        setListener();
    }

    private void setListener() {
        mBtnCancel.setOnClickListener(v -> {
            mDialogClickListener.onCancelClick(this);
        });
        mBtnConfirm.setOnClickListener(v -> {
            mDialogClickListener.onConfirmClick(this);
        });

        if (mFirstEditText != null && mSecondEditText != null) {
            mFirstEditText.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void afterTextChanged(Editable s) {
                    mDialogTextChangeListener.onFirstContentChange(getThis());
                }
            });

            mSecondEditText.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void afterTextChanged(Editable s) {
                    mDialogTextChangeListener.onSecondContentChange(getThis());
                }
            });
        }



        /*mEtFirstContent.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String currentFirstContent = s.toString().trim();
                setEtFirstContentText(currentFirstContent);
            }
        });

        mEtSecondContent.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                setEtSecondContentText(s.toString().trim());
            }
        });*/
    }

    @Override
    public void show() {
        super.show();
    }

    private void initView() {
        mTxtTitle = findViewById(R.id.txt_title);
        mFirstEditText = findViewById(R.id.et_first_content);
        mSecondEditText = findViewById(R.id.et_second_content);
        mBtnCancel = findViewById(R.id.btn_cancel);
        mBtnConfirm = findViewById(R.id.btn_confirm);

        if (!TextUtils.isEmpty(mCsTitleText)) {
            mTxtTitle.setText(mCsTitleText);
        }

        if (!TextUtils.isEmpty(mCsFirstContentText)) {
            mFirstEditText.setText(mCsFirstContentText);
        }

        if (!TextUtils.isEmpty(mCsSecondContentText)) {
            mSecondEditText.setText(mCsSecondContentText);
        }

        if (!TextUtils.isEmpty(mCsConfirmText)) {
            mBtnConfirm.setText(mCsConfirmText);
        }

        if (!TextUtils.isEmpty(mCsCancelText)) {
            mBtnCancel.setText(mCsCancelText);
        }
    }

    public CommonDialog getThis() {
        return this;
    }

    /**
     * 设置标题的颜色
     * @param color color
     */
    public void setTxtTitleColor(@ColorInt int color) {
        mTxtTitle.setTextColor(color);
    }

    public TextView getTitleTextView() {
        return mTxtTitle;
    }
    public void setCsTitleText(CharSequence csTitleText) {
        mCsTitleText = csTitleText;
        mTxtTitle.setText(mCsTitleText);
    }

    public EditText getFirstEditText() {
        return mFirstEditText;
    }

    public void setCsFirstContentText(CharSequence csFirstContentText) {
        mCsFirstContentText = csFirstContentText;
        mFirstEditText.setText(mCsFirstContentText);
    }

    public EditText getSecondEditText() {
        return mSecondEditText;
    }

    public void setCsSecondContentText(CharSequence csSecondContentText) {
        mCsSecondContentText = csSecondContentText;
        mSecondEditText.setText(mCsSecondContentText);
    }

    public Button getConfirmButton() {
        return mBtnConfirm;
    }

    public void setCsConfirmText(CharSequence csConfirmText) {
        mCsConfirmText = csConfirmText;
        mBtnConfirm.setText(csConfirmText);
    }

    public Button getCancelButton() {
        return mBtnCancel;
    }

    public void setCsCancelText(CharSequence csCancelText) {
        mCsCancelText = csCancelText;
        mBtnCancel.setText(csCancelText);
    }

    /**
     * Builder类
     */
    public static class Builder {

        private final Context mContext;

        private CharSequence mTxtTitleText;
        private CharSequence mEtFirstContentText,mEtSecondContentText;

        private CharSequence mCancelButtonText,mConfirmButtonText;
        private CommonDialogClickListener mDialogListener;
        private CommonDialogTextChangeListener mDialogTextChangeListener;

        private int layoutResID;
        private int themeResID;

        public Builder(Context context) {
            mContext = context;
        }

        public Builder setLayoutResID(int layoutResID) {
            this.layoutResID = layoutResID;
            return this;
        }

        public Builder setThemeResID(int themeResID) {
            this.themeResID = themeResID;
            return this;
        }

        public Builder setTitle(CharSequence title) {
            this.mTxtTitleText = title;
            return this;
        }

        public Builder setFirstContent (CharSequence firstContent) {
            this.mEtFirstContentText = firstContent;
            return this;
        }

        public Builder setSecondContent(CharSequence secondContent) {
            this.mEtSecondContentText = secondContent;
            return this;
        }

        public Builder setCancelButtonText(CharSequence cancelButtonText) {
            this.mCancelButtonText = cancelButtonText;
            return this;
        }


        public Builder setConfirmButtonText(CharSequence confirmButtonText) {
            this.mConfirmButtonText = confirmButtonText;
            return this;
        }

        public Builder setOnClickListener(final CommonDialogClickListener listener) {
            this.mDialogListener = listener;
            return this;
        }

        public Builder setDialogTextChangeListener(CommonDialogTextChangeListener dialogTextChangeListener) {
            mDialogTextChangeListener = dialogTextChangeListener;
            return this;
        }

        private Context getContext() {
            return mContext;
        }

        private CharSequence getCancelButtonText() {
            return mCancelButtonText;
        }

        private CharSequence getConfirmButtonText() {
            return mConfirmButtonText;
        }

        private CharSequence getTxtTitleText() {
            return mTxtTitleText;
        }

        private CharSequence getEtFirstContentText() {
            return mEtFirstContentText;
        }

        private CharSequence getEtSecondContentText() {
            return mEtSecondContentText;
        }

        private int getLayoutResID() {
            return layoutResID;
        }

        private int getThemeResID() {
            return themeResID;
        }

        private CommonDialogClickListener getDialogListener() {
            return mDialogListener;
        }

        private CommonDialogTextChangeListener getDialogTextChangeListener() {
            return mDialogTextChangeListener;
        }

        public CommonDialog build() {
            return new CommonDialog(mContext,this);
        }

    }

    public interface CommonDialogClickListener {
        void onConfirmClick(Dialog dialog);
        void onCancelClick(Dialog dialog);
    }

    public interface CommonDialogTextChangeListener {
        void onFirstContentChange(Dialog dialog);
        void onSecondContentChange(Dialog dialog);
    }
}
