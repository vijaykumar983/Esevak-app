package in.jploft.esevak.ui.components.activities.payment;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.HashMap;

import in.jploft.esevak.network.ApiResponse;
import in.jploft.esevak.network.RestApi;
import in.jploft.esevak.network.RestApiFactory;
import in.jploft.esevak.pojo.PlaceOrderData;
import in.jploft.esevak.pojo.WalletData;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class PaymentViewModel extends ViewModel {
    MutableLiveData<ApiResponse<PlaceOrderData>> responseLiveData = new MutableLiveData<>();
    ApiResponse<PlaceOrderData> apiResponse = null;

    /*my wallet*/
    MutableLiveData<ApiResponse<WalletData>> responseWalletLiveData = new MutableLiveData<>();
    ApiResponse<WalletData> apiWalletResponse = null;


    private RestApi restApi = null;
    private Disposable subscription = null;


    {
        restApi = RestApiFactory.create();
        apiResponse = new ApiResponse<>(ApiResponse.Status.LOADING, null, null);
        apiWalletResponse = new ApiResponse<>(ApiResponse.Status.LOADING, null, null);
    }

    public final void walletApi(HashMap<String, String> reqData) {
        subscription = restApi.wallet(reqData)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(new Consumer<Disposable>() {
                    @Override
                    public void accept(Disposable disposable) throws Exception {
                        responseWalletLiveData.postValue(apiWalletResponse.loading());
                    }
                })
                .subscribe(new Consumer<WalletData>() {
                    @Override
                    public void accept(WalletData walletData) throws Exception {
                        responseWalletLiveData.postValue(apiWalletResponse.success(walletData));
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        responseWalletLiveData.postValue(apiWalletResponse.error(throwable));
                    }
                });

    }

    public final void placeOrderApi(String url) {
        subscription = restApi.placeOrders(url)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(new Consumer<Disposable>() {
                    @Override
                    public void accept(Disposable disposable) throws Exception {
                        responseLiveData.postValue(apiResponse.loading());
                    }
                })
                .subscribe(new Consumer<PlaceOrderData>() {
                    @Override
                    public void accept(PlaceOrderData placeOrderData) throws Exception {
                        responseLiveData.postValue(apiResponse.success(placeOrderData));
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        responseLiveData.postValue(apiResponse.error(throwable));
                    }
                });

    }

    public void disposeSubscriber() {
        if (subscription != null)
            subscription.dispose();
    }
}
