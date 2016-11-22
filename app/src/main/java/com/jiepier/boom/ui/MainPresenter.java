package com.jiepier.boom.ui;

import android.support.annotation.NonNull;

import com.jiepier.boom.bean.AppProcessInfo;

import java.util.List;

/**
 * Created by panruijiesx on 2016/11/22.
 */

public class MainPresenter implements MainContact.Presenter{

    private MainContact.View mView;

    @Override
    public List<AppProcessInfo> getProcessInfo() {
        return null;
    }

    @Override
    public void attachView(@NonNull MainContact.View view) {
        mView = view;
    }

    @Override
    public void detachView() {
        mView = null;
    }
}
