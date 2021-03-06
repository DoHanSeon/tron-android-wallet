package com.devband.tronwalletforandroid.ui.token.holder;

import com.devband.tronlib.TronNetwork;
import com.devband.tronlib.dto.Token;
import com.devband.tronlib.dto.TokenHolder;
import com.devband.tronlib.dto.TokenHolders;
import com.devband.tronwalletforandroid.common.AdapterDataModel;
import com.devband.tronwalletforandroid.ui.mvp.BasePresenter;

import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;

public class HolderPresenter extends BasePresenter<HolderView> {

    private AdapterDataModel<TokenHolder>  mAdapterDataModel;

    public HolderPresenter(HolderView view) {
        super(view);
    }

    public void setAdapterDataModel(AdapterDataModel<TokenHolder> adapterDataModel) {
        this.mAdapterDataModel = adapterDataModel;
    }

    @Override
    public void onCreate() {

    }

    @Override
    public void onPause() {

    }

    @Override
    public void onResume() {

    }

    @Override
    public void onDestroy() {

    }

    public void getTokenHolders(String tokenName, long startIndex, int pageSize) {
        mView.showLoadingDialog();

        TronNetwork.getInstance()
                .getTokenHolders(tokenName, startIndex, pageSize, "-balance")
                .map((tokenHolders -> {
                    Token token = TronNetwork.getInstance().getTokenDetail(tokenName).blockingGet();

                    for (TokenHolder tokenHolder : tokenHolders.getData()) {
                        tokenHolder.setTotalSupply(token.getTotalSupply());
                        tokenHolder.setBalancePercent(((double) tokenHolder.getBalance() / (double) token.getTotalSupply()) * 100f);
                    }

                    return tokenHolders;
                }))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<TokenHolders>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onSuccess(TokenHolders tokenHolders) {
                        mAdapterDataModel.addAll(tokenHolders.getData());
                        mView.finishLoading(tokenHolders.getTotal());
                    }

                    @Override
                    public void onError(Throwable e) {
                        mView.showServerError();
                    }
                });
    }
}
