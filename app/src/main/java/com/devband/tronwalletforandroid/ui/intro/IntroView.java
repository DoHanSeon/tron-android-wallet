package com.devband.tronwalletforandroid.ui.intro;

import com.devband.tronwalletforandroid.ui.mvp.IView;

interface IntroView extends IView {

    void startCreateAccountActivity();

    void startLoginActivity();

    void startBackupAccountActivity();
}
