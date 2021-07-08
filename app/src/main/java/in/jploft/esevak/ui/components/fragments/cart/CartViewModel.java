package in.jploft.esevak.ui.components.fragments.cart;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.HashMap;

import in.jploft.esevak.network.ApiResponse;
import in.jploft.esevak.network.RestApi;
import in.jploft.esevak.network.RestApiFactory;
import in.jploft.esevak.pojo.ApplyCouponData;
import in.jploft.esevak.pojo.MyCartData;
import in.jploft.esevak.pojo.PlaceOrderData;
import in.jploft.esevak.pojo.RemoveCartData;
import in.jploft.esevak.pojo.TimeSlotData;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class CartViewModel extends ViewModel {
    MutableLiveData<ApiResponse<MyCartData>> responseLiveData = new MutableLiveData<>();
    ApiResponse<MyCartData> apiResponse = null;

    /*remove cart*/
    MutableLiveData<ApiResponse<RemoveCartData>> responseRemoveCartLiveData = new MutableLiveData<>();
    ApiResponse<RemoveCartData> apiRemoveCartResponse = null;

    /*apply coupon*/
    MutableLiveData<ApiResponse<ApplyCouponData>> responseApplyCouponLiveData = new MutableLiveData<>();
    ApiResponse<ApplyCouponData> apiApplyCouponResponse = null;

    /*time slot*/
    MutableLiveData<ApiResponse<TimeSlotData>> responseTimeSlotLiveData = new MutableLiveData<>();
    ApiResponse<TimeSlotData> apiTimeSlotResponse = null;


    private RestApi restApi = null;
    private Disposable subscription = null;


    {
        restApi = RestApiFactory.create();
        apiResponse = new ApiResponse<>(ApiResponse.Status.LOADING, null, null);
        apiRemoveCartResponse = new ApiResponse<>(ApiResponse.Status.LOADING, null, null);
        apiApplyCouponResponse = new ApiResponse<>(ApiResponse.Status.LOADING, null, null);
        apiTimeSlotResponse = new ApiResponse<>(ApiResponse.Status.LOADING, null, null);
    }

    public final void myCartApi(HashMap<String, String> reqData) {
        subscription = restApi.myCart(reqData)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(new Consumer<Disposable>() {
                    @Override
                    public void accept(Disposable disposable) throws Exception {
                        responseLiveData.postValue(apiResponse.loading());
                    }
                })
                .subscribe(new Consumer<MyCartData>() {
                    @Override
                    public void accept(MyCartData myCartData) throws Exception {
                        responseLiveData.postValue(apiResponse.success(myCartData));
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        responseLiveData.postValue(apiResponse.error(throwable));
                    }
                });

    }

    public final void removeCartApi(HashMap<String, String> reqData) {
        subscription = restApi.removeCart(reqData)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(new Consumer<Disposable>() {
                    @Override
                    public void accept(Disposable disposable) throws Exception {
                        responseRemoveCartLiveData.postValue(apiRemoveCartResponse.loading());
                    }
                })
                .subscribe(new Consumer<RemoveCartData>() {
                    @Override
                    public void accept(RemoveCartData removeCartData) throws Exception {
                        responseRemoveCartLiveData.postValue(apiRemoveCartResponse.success(removeCartData));
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        responseRemoveCartLiveData.postValue(apiRemoveCartResponse.error(throwable));
                    }
                });

    }

    public final void applyCouponApi(HashMap<String, String> reqData) {
        subscription = restApi.applyCoupon(reqData)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(new Consumer<Disposable>() {
                    @Override
                    public void accept(Disposable disposable) throws Exception {
                        responseApplyCouponLiveData.postValue(apiApplyCouponResponse.loading());
                    }
                })
                .subscribe(new Consumer<ApplyCouponData>() {
                    @Override
                    public void accept(ApplyCouponData applyCouponData) throws Exception {
                        responseApplyCouponLiveData.postValue(apiApplyCouponResponse.success(applyCouponData));
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        responseApplyCouponLiveData.postValue(apiApplyCouponResponse.error(throwable));
                    }
                });

    }
    public final void timeSlotApi() {
        subscription = restApi.timeSlot()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(new Consumer<Disposable>() {
                    @Override
                    public void accept(Disposable disposable) throws Exception {
                        responseTimeSlotLiveData.postValue(apiTimeSlotResponse.loading());
                    }
                })
                .subscribe(new Consumer<TimeSlotData>() {
                    @Override
                    public void accept(TimeSlotData timeSlotData) throws Exception {
                        responseTimeSlotLiveData.postValue(apiTimeSlotResponse.success(timeSlotData));
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        responseTimeSlotLiveData.postValue(apiTimeSlotResponse.error(throwable));
                    }
                });

    }

    public void disposeSubscriber() {
        if (subscription != null)
            subscription.dispose();
    }
}