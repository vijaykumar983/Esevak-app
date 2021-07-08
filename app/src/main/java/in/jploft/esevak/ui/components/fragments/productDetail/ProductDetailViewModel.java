package in.jploft.esevak.ui.components.fragments.productDetail;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.HashMap;

import in.jploft.esevak.network.ApiResponse;
import in.jploft.esevak.network.RestApi;
import in.jploft.esevak.network.RestApiFactory;
import in.jploft.esevak.pojo.AddToCartData;
import in.jploft.esevak.pojo.ProductDetailData;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class ProductDetailViewModel extends ViewModel {
    MutableLiveData<ApiResponse<ProductDetailData>> responseLiveData = new MutableLiveData<>();
    ApiResponse<ProductDetailData> apiResponse = null;

    /*add to cart*/
    MutableLiveData<ApiResponse<AddToCartData>> responseAddCartLiveData = new MutableLiveData<>();
    ApiResponse<AddToCartData> apiAddCartResponse = null;


    private RestApi restApi = null;
    private Disposable subscription = null;


    {
        restApi = RestApiFactory.create();
        apiResponse = new ApiResponse<>(ApiResponse.Status.LOADING, null, null);
        apiAddCartResponse = new ApiResponse<>(ApiResponse.Status.LOADING, null, null);
    }

    public final void productDetail(HashMap<String, String> reqData) {
        subscription = restApi.productDetail(reqData)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(new Consumer<Disposable>() {
                    @Override
                    public void accept(Disposable disposable) throws Exception {
                        responseLiveData.postValue(apiResponse.loading());
                    }
                })
                .subscribe(new Consumer<ProductDetailData>() {
                    @Override
                    public void accept(ProductDetailData productDetailData) throws Exception {
                        responseLiveData.postValue(apiResponse.success(productDetailData));
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        responseLiveData.postValue(apiResponse.error(throwable));
                    }
                });

    }

    public final void addToCartApi(HashMap<String, String> reqData) {
        subscription = restApi.addToCart(reqData)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(new Consumer<Disposable>() {
                    @Override
                    public void accept(Disposable disposable) throws Exception {
                        responseAddCartLiveData.postValue(apiAddCartResponse.loading());
                    }
                })
                .subscribe(new Consumer<AddToCartData>() {
                    @Override
                    public void accept(AddToCartData addToCartData) throws Exception {
                        responseAddCartLiveData.postValue(apiAddCartResponse.success(addToCartData));
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        responseAddCartLiveData.postValue(apiAddCartResponse.error(throwable));
                    }
                });

    }


    public void disposeSubscriber() {
        if (subscription != null)
            subscription.dispose();
    }
}