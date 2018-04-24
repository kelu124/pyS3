package me.drakeet.materialdialog;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface.OnDismissListener;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build.VERSION;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import com.itextpdf.text.html.HtmlUtilities;

public class MaterialDialog {
    private static final int BUTTON_BOTTOM = 9;
    private static final int BUTTON_TOP = 9;
    private AlertDialog mAlertDialog;
    private Drawable mBackgroundDrawable;
    private int mBackgroundResId = -1;
    private Builder mBuilder;
    private boolean mCancel;
    private Context mContext;
    private boolean mHasShow = false;
    private LayoutParams mLayoutParams;
    private CharSequence mMessage;
    private View mMessageContentView;
    private int mMessageContentViewResId;
    private int mMessageResId;
    private Button mNegativeButton;
    private OnDismissListener mOnDismissListener;
    private Button mPositiveButton;
    private CharSequence mTitle;
    private int mTitleResId;
    private View mView;
    private int nId = -1;
    OnClickListener nListener;
    private String nText;
    private int pId = -1;
    OnClickListener pListener;
    private String pText;

    private class Builder {
        private Window mAlertDialogWindow;
        private LinearLayout mButtonLayout;
        private ViewGroup mMessageContentRoot;
        private TextView mMessageView;
        private TextView mTitleView;

        class C10451 implements OnFocusChangeListener {
            C10451() {
            }

            public void onFocusChange(View v, boolean hasFocus) {
                Builder.this.mAlertDialogWindow.setSoftInputMode(5);
                ((InputMethodManager) MaterialDialog.this.mContext.getSystemService("input_method")).toggleSoftInput(2, 1);
            }
        }

        private Builder() {
            MaterialDialog.this.mAlertDialog = new android.app.AlertDialog.Builder(MaterialDialog.this.mContext).create();
            MaterialDialog.this.mAlertDialog.show();
            MaterialDialog.this.mAlertDialog.getWindow().clearFlags(131080);
            MaterialDialog.this.mAlertDialog.getWindow().setSoftInputMode(15);
            this.mAlertDialogWindow = MaterialDialog.this.mAlertDialog.getWindow();
            this.mAlertDialogWindow.setBackgroundDrawable(new ColorDrawable(0));
            View contentView = LayoutInflater.from(MaterialDialog.this.mContext).inflate(C1046R.layout.layout_material_dialog, null);
            contentView.setFocusable(true);
            contentView.setFocusableInTouchMode(true);
            this.mAlertDialogWindow.setBackgroundDrawableResource(C1046R.drawable.material_dialog_window);
            this.mAlertDialogWindow.setContentView(contentView);
            this.mTitleView = (TextView) this.mAlertDialogWindow.findViewById(C1046R.id.title);
            this.mMessageView = (TextView) this.mAlertDialogWindow.findViewById(C1046R.id.message);
            this.mButtonLayout = (LinearLayout) this.mAlertDialogWindow.findViewById(C1046R.id.buttonLayout);
            MaterialDialog.this.mPositiveButton = (Button) this.mButtonLayout.findViewById(C1046R.id.btn_p);
            MaterialDialog.this.mNegativeButton = (Button) this.mButtonLayout.findViewById(C1046R.id.btn_n);
            this.mMessageContentRoot = (ViewGroup) this.mAlertDialogWindow.findViewById(C1046R.id.message_content_root);
            if (MaterialDialog.this.mView != null) {
                LinearLayout linearLayout = (LinearLayout) this.mAlertDialogWindow.findViewById(C1046R.id.contentView);
                linearLayout.removeAllViews();
                linearLayout.addView(MaterialDialog.this.mView);
            }
            if (MaterialDialog.this.mTitleResId != 0) {
                setTitle(MaterialDialog.this.mTitleResId);
            }
            if (MaterialDialog.this.mTitle != null) {
                setTitle(MaterialDialog.this.mTitle);
            }
            if (MaterialDialog.this.mTitle == null && MaterialDialog.this.mTitleResId == 0) {
                this.mTitleView.setVisibility(8);
            }
            if (MaterialDialog.this.mMessageResId != 0) {
                setMessage(MaterialDialog.this.mMessageResId);
            }
            if (MaterialDialog.this.mMessage != null) {
                setMessage(MaterialDialog.this.mMessage);
            }
            if (MaterialDialog.this.pId != -1) {
                MaterialDialog.this.mPositiveButton.setVisibility(0);
                MaterialDialog.this.mPositiveButton.setText(MaterialDialog.this.pId);
                MaterialDialog.this.mPositiveButton.setOnClickListener(MaterialDialog.this.pListener);
                if (MaterialDialog.isLollipop()) {
                    MaterialDialog.this.mPositiveButton.setElevation(0.0f);
                }
            }
            if (MaterialDialog.this.nId != -1) {
                MaterialDialog.this.mNegativeButton.setVisibility(0);
                MaterialDialog.this.mNegativeButton.setText(MaterialDialog.this.nId);
                MaterialDialog.this.mNegativeButton.setOnClickListener(MaterialDialog.this.nListener);
                if (MaterialDialog.isLollipop()) {
                    MaterialDialog.this.mNegativeButton.setElevation(0.0f);
                }
            }
            if (!MaterialDialog.this.isNullOrEmpty(MaterialDialog.this.pText)) {
                MaterialDialog.this.mPositiveButton.setVisibility(0);
                MaterialDialog.this.mPositiveButton.setText(MaterialDialog.this.pText);
                MaterialDialog.this.mPositiveButton.setOnClickListener(MaterialDialog.this.pListener);
                if (MaterialDialog.isLollipop()) {
                    MaterialDialog.this.mPositiveButton.setElevation(0.0f);
                }
            }
            if (!MaterialDialog.this.isNullOrEmpty(MaterialDialog.this.nText)) {
                MaterialDialog.this.mNegativeButton.setVisibility(0);
                MaterialDialog.this.mNegativeButton.setText(MaterialDialog.this.nText);
                MaterialDialog.this.mNegativeButton.setOnClickListener(MaterialDialog.this.nListener);
                if (MaterialDialog.isLollipop()) {
                    MaterialDialog.this.mNegativeButton.setElevation(0.0f);
                }
            }
            if (MaterialDialog.this.isNullOrEmpty(MaterialDialog.this.pText) && MaterialDialog.this.pId == -1) {
                MaterialDialog.this.mPositiveButton.setVisibility(8);
            }
            if (MaterialDialog.this.isNullOrEmpty(MaterialDialog.this.nText) && MaterialDialog.this.nId == -1) {
                MaterialDialog.this.mNegativeButton.setVisibility(8);
            }
            if (MaterialDialog.this.mBackgroundResId != -1) {
                ((LinearLayout) this.mAlertDialogWindow.findViewById(C1046R.id.material_background)).setBackgroundResource(MaterialDialog.this.mBackgroundResId);
            }
            if (MaterialDialog.this.mBackgroundDrawable != null) {
                ((LinearLayout) this.mAlertDialogWindow.findViewById(C1046R.id.material_background)).setBackground(MaterialDialog.this.mBackgroundDrawable);
            }
            if (MaterialDialog.this.mMessageContentView != null) {
                setContentView(MaterialDialog.this.mMessageContentView);
            } else if (MaterialDialog.this.mMessageContentViewResId != 0) {
                setContentView(MaterialDialog.this.mMessageContentViewResId);
            }
            MaterialDialog.this.mAlertDialog.setCanceledOnTouchOutside(MaterialDialog.this.mCancel);
            MaterialDialog.this.mAlertDialog.setCancelable(MaterialDialog.this.mCancel);
            if (MaterialDialog.this.mOnDismissListener != null) {
                MaterialDialog.this.mAlertDialog.setOnDismissListener(MaterialDialog.this.mOnDismissListener);
            }
        }

        public void setTitle(int resId) {
            this.mTitleView.setText(resId);
        }

        public void setTitle(CharSequence title) {
            this.mTitleView.setText(title);
        }

        public void setMessage(int resId) {
            if (this.mMessageView != null) {
                this.mMessageView.setText(resId);
            }
        }

        public void setMessage(CharSequence message) {
            if (this.mMessageView != null) {
                this.mMessageView.setText(message);
            }
        }

        public void setPositiveButton(String text, OnClickListener listener) {
            Button button = new Button(MaterialDialog.this.mContext);
            button.setLayoutParams(new LayoutParams(-2, -2));
            button.setBackgroundResource(C1046R.drawable.material_card);
            button.setTextColor(Color.argb(255, 35, 159, 242));
            button.setText(text);
            button.setGravity(17);
            button.setTextSize(14.0f);
            button.setPadding(MaterialDialog.this.dip2px(HtmlUtilities.DEFAULT_FONT_SIZE), 0, MaterialDialog.this.dip2px(32.0f), MaterialDialog.this.dip2px(9.0f));
            button.setOnClickListener(listener);
            this.mButtonLayout.addView(button);
        }

        public void setNegativeButton(String text, OnClickListener listener) {
            Button button = new Button(MaterialDialog.this.mContext);
            LayoutParams params = new LayoutParams(-2, -2);
            button.setLayoutParams(params);
            button.setBackgroundResource(C1046R.drawable.material_card);
            button.setText(text);
            button.setTextColor(Color.argb(222, 0, 0, 0));
            button.setTextSize(14.0f);
            button.setGravity(17);
            button.setPadding(0, 0, 0, MaterialDialog.this.dip2px(8.0f));
            button.setOnClickListener(listener);
            if (this.mButtonLayout.getChildCount() > 0) {
                params.setMargins(20, 0, 10, MaterialDialog.this.dip2px(9.0f));
                button.setLayoutParams(params);
                this.mButtonLayout.addView(button, 1);
                return;
            }
            button.setLayoutParams(params);
            this.mButtonLayout.addView(button);
        }

        public void setView(View view) {
            LinearLayout l = (LinearLayout) this.mAlertDialogWindow.findViewById(C1046R.id.contentView);
            l.removeAllViews();
            view.setLayoutParams(new ViewGroup.LayoutParams(-1, -2));
            view.setOnFocusChangeListener(new C10451());
            l.addView(view);
            if (view instanceof ViewGroup) {
                int i;
                ViewGroup viewGroup = (ViewGroup) view;
                for (i = 0; i < viewGroup.getChildCount(); i++) {
                    if (viewGroup.getChildAt(i) instanceof EditText) {
                        EditText editText = (EditText) viewGroup.getChildAt(i);
                        editText.setFocusable(true);
                        editText.requestFocus();
                        editText.setFocusableInTouchMode(true);
                    }
                }
                for (i = 0; i < viewGroup.getChildCount(); i++) {
                    if (viewGroup.getChildAt(i) instanceof AutoCompleteTextView) {
                        AutoCompleteTextView autoCompleteTextView = (AutoCompleteTextView) viewGroup.getChildAt(i);
                        autoCompleteTextView.setFocusable(true);
                        autoCompleteTextView.requestFocus();
                        autoCompleteTextView.setFocusableInTouchMode(true);
                    }
                }
            }
        }

        public void setContentView(View contentView) {
            contentView.setLayoutParams(new ViewGroup.LayoutParams(-1, -1));
            if (contentView instanceof ListView) {
                MaterialDialog.this.setListViewHeightBasedOnChildren((ListView) contentView);
            }
            LinearLayout linearLayout = (LinearLayout) this.mAlertDialogWindow.findViewById(C1046R.id.message_content_view);
            if (linearLayout != null) {
                linearLayout.removeAllViews();
                linearLayout.addView(contentView);
            }
            int i = 0;
            while (true) {
                if (i < (linearLayout != null ? linearLayout.getChildCount() : 0)) {
                    if (linearLayout.getChildAt(i) instanceof AutoCompleteTextView) {
                        AutoCompleteTextView autoCompleteTextView = (AutoCompleteTextView) linearLayout.getChildAt(i);
                        autoCompleteTextView.setFocusable(true);
                        autoCompleteTextView.requestFocus();
                        autoCompleteTextView.setFocusableInTouchMode(true);
                    }
                    i++;
                } else {
                    return;
                }
            }
        }

        public void setContentView(int layoutResId) {
            this.mMessageContentRoot.removeAllViews();
            LayoutInflater.from(this.mMessageContentRoot.getContext()).inflate(layoutResId, this.mMessageContentRoot);
        }

        public void setBackground(Drawable drawable) {
            ((LinearLayout) this.mAlertDialogWindow.findViewById(C1046R.id.material_background)).setBackground(drawable);
        }

        public void setBackgroundResource(int resId) {
            ((LinearLayout) this.mAlertDialogWindow.findViewById(C1046R.id.material_background)).setBackgroundResource(resId);
        }

        public void setCanceledOnTouchOutside(boolean canceledOnTouchOutside) {
            MaterialDialog.this.mAlertDialog.setCanceledOnTouchOutside(canceledOnTouchOutside);
            MaterialDialog.this.mAlertDialog.setCancelable(canceledOnTouchOutside);
        }
    }

    public MaterialDialog(Context context) {
        this.mContext = context;
    }

    public void show() {
        if (this.mHasShow) {
            this.mAlertDialog.show();
        } else {
            this.mBuilder = new Builder();
        }
        this.mHasShow = true;
    }

    public MaterialDialog setView(View view) {
        this.mView = view;
        if (this.mBuilder != null) {
            this.mBuilder.setView(view);
        }
        return this;
    }

    public MaterialDialog setContentView(View view) {
        this.mMessageContentView = view;
        this.mMessageContentViewResId = 0;
        if (this.mBuilder != null) {
            this.mBuilder.setContentView(this.mMessageContentView);
        }
        return this;
    }

    public MaterialDialog setContentView(int layoutResId) {
        this.mMessageContentViewResId = layoutResId;
        this.mMessageContentView = null;
        if (this.mBuilder != null) {
            this.mBuilder.setContentView(layoutResId);
        }
        return this;
    }

    public MaterialDialog setBackground(Drawable drawable) {
        this.mBackgroundDrawable = drawable;
        if (this.mBuilder != null) {
            this.mBuilder.setBackground(this.mBackgroundDrawable);
        }
        return this;
    }

    public MaterialDialog setBackgroundResource(int resId) {
        this.mBackgroundResId = resId;
        if (this.mBuilder != null) {
            this.mBuilder.setBackgroundResource(this.mBackgroundResId);
        }
        return this;
    }

    public void dismiss() {
        this.mAlertDialog.dismiss();
    }

    private int dip2px(float dpValue) {
        return (int) ((dpValue * this.mContext.getResources().getDisplayMetrics().density) + 0.5f);
    }

    private static boolean isLollipop() {
        return VERSION.SDK_INT >= 21;
    }

    public MaterialDialog setTitle(int resId) {
        this.mTitleResId = resId;
        if (this.mBuilder != null) {
            this.mBuilder.setTitle(resId);
        }
        return this;
    }

    public MaterialDialog setTitle(CharSequence title) {
        this.mTitle = title;
        if (this.mBuilder != null) {
            this.mBuilder.setTitle(title);
        }
        return this;
    }

    public MaterialDialog setMessage(int resId) {
        this.mMessageResId = resId;
        if (this.mBuilder != null) {
            this.mBuilder.setMessage(resId);
        }
        return this;
    }

    public MaterialDialog setMessage(CharSequence message) {
        this.mMessage = message;
        if (this.mBuilder != null) {
            this.mBuilder.setMessage(message);
        }
        return this;
    }

    public MaterialDialog setPositiveButton(int resId, OnClickListener listener) {
        this.pId = resId;
        this.pListener = listener;
        return this;
    }

    public Button getPositiveButton() {
        return this.mPositiveButton;
    }

    public Button getNegativeButton() {
        return this.mNegativeButton;
    }

    public MaterialDialog setPositiveButton(String text, OnClickListener listener) {
        this.pText = text;
        this.pListener = listener;
        return this;
    }

    public MaterialDialog setNegativeButton(int resId, OnClickListener listener) {
        this.nId = resId;
        this.nListener = listener;
        return this;
    }

    public MaterialDialog setNegativeButton(String text, OnClickListener listener) {
        this.nText = text;
        this.nListener = listener;
        return this;
    }

    public MaterialDialog setCanceledOnTouchOutside(boolean cancel) {
        this.mCancel = cancel;
        if (this.mBuilder != null) {
            this.mBuilder.setCanceledOnTouchOutside(this.mCancel);
        }
        return this;
    }

    public MaterialDialog setOnDismissListener(OnDismissListener onDismissListener) {
        this.mOnDismissListener = onDismissListener;
        return this;
    }

    private boolean isNullOrEmpty(String nText) {
        return nText == null || nText.isEmpty();
    }

    private void setListViewHeightBasedOnChildren(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter != null) {
            int totalHeight = 0;
            for (int i = 0; i < listAdapter.getCount(); i++) {
                View listItem = listAdapter.getView(i, null, listView);
                listItem.measure(0, 0);
                totalHeight += listItem.getMeasuredHeight();
            }
            ViewGroup.LayoutParams params = listView.getLayoutParams();
            params.height = (listView.getDividerHeight() * (listAdapter.getCount() - 1)) + totalHeight;
            listView.setLayoutParams(params);
        }
    }
}
