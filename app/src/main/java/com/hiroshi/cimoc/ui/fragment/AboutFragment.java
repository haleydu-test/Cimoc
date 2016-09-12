package com.hiroshi.cimoc.ui.fragment;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.net.Uri;
import android.widget.TextView;

import com.hiroshi.cimoc.R;
import com.hiroshi.cimoc.presenter.AboutPresenter;
import com.hiroshi.cimoc.ui.view.AboutView;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by Hiroshi on 2016/7/20.
 */
public class AboutFragment extends BaseFragment implements AboutView {

    @BindView(R.id.about_update_summary) TextView mUpdateText;

    private AboutPresenter mPresenter;
    private boolean update;
    private boolean checking;

    @OnClick(R.id.about_support_btn) void onSupportClick() {
        ClipboardManager manager = (ClipboardManager) getActivity().getSystemService(Context.CLIPBOARD_SERVICE);
        manager.setPrimaryClip(ClipData.newPlainText(null, getString(R.string.about_support_email)));
        showSnackbar(R.string.about_already_clip);
    }

    @OnClick(R.id.about_resource_btn) void onResourceClick() {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(getString(R.string.about_resource_url)));
        startActivity(intent);
    }

    @OnClick(R.id.about_update_btn) void onUpdateClick() {
        if (update) {
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(getString(R.string.about_update_url)));
            startActivity(intent);
        } else if (!checking) {
            try {
                PackageInfo info = getActivity().getPackageManager().getPackageInfo(getActivity().getPackageName(), 0);
                mUpdateText.setText(R.string.about_update_doing);
                checking = true;
                mPresenter.checkUpdate(info.versionName);
            } catch (Exception e){
                mUpdateText.setText(R.string.about_update_fail);
                checking = false;
            }
        }
    }

    @Override
    public void onUpdateNone() {
        mUpdateText.setText(R.string.about_update_latest);
        checking = false;
    }

    @Override
    public void onUpdateReady() {
        mUpdateText.setText(R.string.about_update_download);
        checking = false;
        update = true;
    }

    @Override
    public void onCheckError() {
        mUpdateText.setText(R.string.about_update_fail);
        checking = false;
    }

    @Override
    protected int getLayoutView() {
        return R.layout.fragment_about;
    }

    @Override
    public void onDestroy() {
        mPresenter.detachView();
        super.onDestroy();
    }

    @Override
    protected void initPresenter() {
        mPresenter = new AboutPresenter();
        mPresenter.attachView(this);
    }

    @Override
    protected void initData() {
        update = false;
    }

}