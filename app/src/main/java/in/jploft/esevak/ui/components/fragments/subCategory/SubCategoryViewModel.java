package in.jploft.esevak.ui.components.fragments.subCategory;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.HashMap;

import in.jploft.esevak.network.ApiResponse;
import in.jploft.esevak.network.RestApi;
import in.jploft.esevak.network.RestApiFactory;
import in.jploft.esevak.pojo.AllCategoryData;
import in.jploft.esevak.pojo.SubCategoryData;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class SubCategoryViewModel extends ViewModel {
    MutableLiveData<ApiResponse<SubCategoryData>> responseLiveData = new MutableLiveData<>();
    ApiResponse<SubCategoryData> apiResponse = null;


    private RestApi restApi = null;
    private Disposable subscription = null;


    {
        restApi = RestApiFactory.create();
        apiResponse = new ApiResponse<>(ApiResponse.Status.LOADING, null, null);
    }

    public final void subCategory(HashMap<String, String> reqData) {
        subscription = restApi.subCategory(reqData)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(new Consumer<Disposable>() {
                    @Override
                    public void accept(Disposable disposable) throws Exception {
                        responseLiveData.postValue(apiResponse.loading());
                    }
                })
                .subscribe(new Consumer<SubCategoryData>() {
                    @Override
                    public void accept(SubCategoryData subCategoryData) throws Exception {
                        responseLiveData.postValue(apiResponse.success(subCategoryData));
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