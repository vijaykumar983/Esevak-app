package in.jploft.esevak.ui.components.fragments.wallet;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.HashMap;

import in.jploft.esevak.network.ApiResponse;
import in.jploft.esevak.network.RestApi;
import in.jploft.esevak.network.RestApiFactory;
import in.jploft.esevak.pojo.TransactionHistoryData;
import in.jploft.esevak.pojo.WalletData;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class WalletViewModel extends ViewModel {
    MutableLiveData<ApiResponse<WalletData>> responseLiveData = new MutableLiveData<>();
    ApiResponse<WalletData> apiResponse = null;

    /*transaction history*/
    MutableLiveData<ApiResponse<TransactionHistoryData>> responseTranHistoryLiveData = new MutableLiveData<>();
    ApiResponse<TransactionHistoryData> apiTranHistoryResponse = null;

    private RestApi restApi = null;
    private Disposable subscription = null;

    {
        restApi = RestApiFactory.create();
        apiResponse = new ApiResponse<>(ApiResponse.Status.LOADING, null, null);
        apiTranHistoryResponse = new ApiResponse<>(ApiResponse.Status.LOADING, null, null);
    }

    public final void walletApi(HashMap<String, String> reqData) {
        subscription = restApi.wallet(reqData)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(new Consumer<Disposable>() {
                    @Override
                    public void accept(Disposable disposable) throws Exception {
                        responseLiveData.postValue(apiResponse.loading());
                    }
                })
                .subscribe(new Consumer<WalletData>() {
                    @Override
                    public void accept(WalletData walletData) throws Exception {
                        responseLiveData.postValue(apiResponse.success(walletData));
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        responseLiveData.postValue(apiResponse.error(throwable));
                    }
                });

    }

    public final void transactionHistoryApi(HashMap<String, String> reqData) {
        subscription = restApi.transactionHistory(reqData)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(new Consumer<Disposable>() {
                    @Override
                    public void accept(Disposable disposable) throws Exception {
                        responseTranHistoryLiveData.postValue(apiTranHistoryResponse.loading());
                    }
                })
                .subscribe(new Consumer<TransactionHistoryData>() {
                    @Override
                    public void accept(TransactionHistoryData transactionHistoryData) throws Exception {
                        responseTranHistoryLiveData.postValue(apiTranHistoryResponse.success(transactionHistoryData));
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        responseTranHistoryLiveData.postValue(apiTranHistoryResponse.error(throwable));
                    }
                });

    }

    public void disposeSubscriber() {
        if (subscription != null)
            subscription.dispose();
    }
}