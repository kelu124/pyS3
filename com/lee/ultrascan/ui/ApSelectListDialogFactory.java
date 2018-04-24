package com.lee.ultrascan.ui;

import android.content.Context;
import com.lee.ultrascan.C0796R;
import java.util.List;
import me.drakeet.materialdialog.MaterialDialog;

public class ApSelectListDialogFactory {
    private ApSelectListDialogFactory() {
        throw new AssertionError();
    }

    public static MaterialDialog createApSelectDialog(Context context, List<String> list) {
        return new MaterialDialog(context).setTitle((int) C0796R.string.dialog_ap_select_title);
    }
}
