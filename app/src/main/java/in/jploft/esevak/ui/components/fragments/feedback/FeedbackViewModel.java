package in.jploft.esevak.ui.components.fragments.feedback;

import android.text.TextUtils;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.HashMap;

import in.jploft.esevak.R;
import in.jploft.esevak.network.ApiResponse;
import in.jploft.esevak.network.RestApi;
import in.jploft.esevak.network.RestApiFactory;
import in.jploft.esevak.pojo.FeedbackData;
import in.jploft.esevak.utils.Utility;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class FeedbackViewModel extends ViewModel {
    MutableLiveData<ApiResponse<FeedbackData>> responseLiveData = new MutableLiveData<>();
    ApiResponse<FeedbackData> apiResponse = null;


    private RestApi restApi = null;
    private Disposable subscription = null;


    {
        restApi = RestApiFactory.create();
        apiResponse = new ApiResponse<>(ApiResponse.Status.LOADING, null, null);
    }

    public final void feedbackApi(HashMap<String, String> reqData) {
        subscription = restApi.feedback(reqData)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(new Consumer<Disposable>() {
                    @Override
                    public void accept(Disposable disposable) throws Exception {
                        responseLiveData.postValue(apiResponse.loading());
                    }
                })
                .subscribe(new Consumer<FeedbackData>() {
                    @Override
                    public void accept(FeedbackData feedbackData) throws Exception {
                        responseLiveData.postValue(apiResponse.success(feedbackData));
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

    /*Validations*/
    public boolean isValidFormData(AppCompatActivity mActivity, String description) {

        if (TextUtils.isEmpty(description)) {
            Utility.showToastMessageError(mActivity, mActivity.getString(R.string.enter_description));
            return false;
        }

        if (description.length() < 3) {
            Utility.showToastMessageError(mActivity, mActivity.getString(R.string.minimum_3_char_desciption));
            return false;
        }

        return true;
    }
}