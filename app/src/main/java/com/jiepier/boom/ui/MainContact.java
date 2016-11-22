package com.jiepier.boom.ui;

import com.jiepier.boom.base.BasePresenter;
import com.jiepier.boom.base.BaseView;
import com.jiepier.boom.bean.AppProcessInfo;

import java.util.List;

/**
 * Created by panruijiesx on 2016/11/22.
 */

public class MainContact {

    interface View extends BaseView{

        void showDialog();

        void dismissDialog();
    }

    interface Model{

    }

    interface Presenter extends BasePresenter<View>{

        List<AppProcessInfo> getProcessInfo();
    }
}
