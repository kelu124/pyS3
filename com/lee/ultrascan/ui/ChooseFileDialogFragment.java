package com.lee.ultrascan.ui;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AlertDialog.Builder;
import com.lee.ultrascan.C0796R;
import java.io.File;
import java.io.FilenameFilter;

public class ChooseFileDialogFragment extends DialogFragment {
    private static final String ARG_DIR = "ARG_DIR";
    private static final String ARG_EXTENSION = "ARG_EXTENSION";
    public static final String TAG = "ChooseFileDialogFragment";

    public interface ChooseFileDialogListener {
        void onSelectFile(String str);
    }

    public static ChooseFileDialogFragment newInstance(String dirPath, String extension) {
        Bundle args = new Bundle();
        args.putString(ARG_DIR, dirPath);
        args.putString(ARG_EXTENSION, extension);
        ChooseFileDialogFragment fragment = new ChooseFileDialogFragment();
        fragment.setArguments(args);
        return fragment;
    }

    public Dialog onCreateDialog(Bundle savedInstanceState) {
        String dirPath = getArguments().getString(ARG_DIR, "");
        final String[] fileList = loadFileList(new File(dirPath), getArguments().getString(ARG_EXTENSION));
        AlertDialog dialog = new Builder(getActivity()).setTitle(getActivity().getString(C0796R.string.dialog_select_saved_image_title)).setItems(fileList, new OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                ((MainActivity) ChooseFileDialogFragment.this.getActivity()).onSelectFile(fileList[which]);
            }
        }).create();
        setCancelable(true);
        dialog.setCanceledOnTouchOutside(true);
        return dialog;
    }

    private String[] loadFileList(File dir, final String extension) {
        if (dir.exists()) {
            return dir.list(new FilenameFilter() {
                public boolean accept(File dir, String filename) {
                    return filename.endsWith(extension) || new File(dir, filename).isDirectory();
                }
            });
        }
        return new String[0];
    }
}
