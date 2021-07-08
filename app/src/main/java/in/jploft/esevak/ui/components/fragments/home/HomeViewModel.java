package in.jploft.esevak.ui.components.fragments.home;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.HashMap;

import in.jploft.esevak.network.ApiResponse;
import in.jploft.esevak.network.RestApi;
import in.jploft.esevak.network.RestApiFactory;
import in.jploft.esevak.pojo.AllCityData;
import in.jploft.esevak.pojo.DashboardData;
import in.jploft.esevak.pojo.UpdateCityData;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class HomeViewModel extends ViewModel {
    MutableLiveData<ApiResponse<DashboardData>> responseLiveData = new MutableLiveData<>();
    ApiResponse<DashboardData> apiResponse = null;

    MutableLiveData<ApiResponse<AllCityData>> responseAllCityLiveData = new MutableLiveData<>();
    ApiResponse<AllCityData> apiAllCityResponse = null;

    MutableLiveData<ApiResponse<UpdateCityData>> responseUpdateCityLiveData = new MutableLiveData<>();
    ApiResponse<UpdateCityData> apiUpdateCityResponse = null;


    private RestApi restApi = null;
    private Disposable subscription = null;


    {
        restApi = RestApiFactory.create();
        apiResponse = new ApiResponse<>(ApiResponse.Status.LOADING, null, null);
        apiAllCityResponse = new ApiResponse<>(ApiResponse.Status.LOADING, null, null);
        apiUpdateCityResponse = new ApiResponse<>(ApiResponse.Status.LOADING, null, null);
    }

    public final void dashboardApi(HashMap<String, String> reqData) {
        subscription = restApi.dashboard(reqData)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(new Consumer<Disposable>() {
                    @Override
                    public void accept(Disposable disposable) throws Exception {
                        responseLiveData.postValue(apiResponse.loading());
                    }
                })
                .subscribe(new Consumer<DashboardData>() {
                    @Override
                    public void accept(DashboardData dashboardData) throws Exception {
                        responseLiveData.postValue(apiResponse.success(dashboardData));
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        responseLiveData.postValue(apiResponse.error(throwable));
                    }
                });

    }
    public final void allCityApi() {
        subscription = restApi.allCity()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(new Consumer<Disposable>() {
                    @Override
                    public void accept(Disposable disposable) throws Exception {
                        responseAllCityLiveData.postValue(apiAllCityResponse.loading());
                    }
                })
                .subscribe(new Consumer<AllCityData>() {
                    @Override
                    public void accept(AllCityData allCityData) throws Exception {
                        responseAllCityLiveData.postValue(apiAllCityResponse.success(allCityData));
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        responseAllCityLiveData.postValue(apiAllCityResponse.error(throwable));
                    }
                });

    }

    public final void updateCityApi(HashMap<String, String> reqData) {
        subscription = restApi.updateCity(reqData)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(new Consumer<Disposable>() {
                    @Override
                    public void accept(Disposable disposable) throws Exception {
                        responseUpdateCityLiveData.postValue(apiUpdateCityResponse.loading());
                    }
                })
                .subscribe(new Consumer<UpdateCityData>() {
                    @Override
                    public void accept(UpdateCityData updateCityData) throws Exception {
                        responseUpdateCityLiveData.postValue(apiUpdateCityResponse.success(updateCityData));
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        responseUpdateCityLiveData.postValue(apiUpdateCityResponse.error(throwable));
                    }
                });

    }

    public void disposeSubscriber() {
        if (subscription != null)
            subscription.dispose();
    }

}