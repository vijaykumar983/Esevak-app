package in.jploft.esevak.ui.components.activities.verifyOtp;

import android.text.TextUtils;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.HashMap;

import in.jploft.esevak.R;
import in.jploft.esevak.network.ApiResponse;
import in.jploft.esevak.network.RestApi;
import in.jploft.esevak.network.RestApiFactory;
import in.jploft.esevak.pojo.ResendOtpData;
import in.jploft.esevak.pojo.UserData;
import in.jploft.esevak.utils.Utility;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class VerifyOtpViewModel extends ViewModel {
    MutableLiveData<ApiResponse<UserData>> responseLiveData = new MutableLiveData<>();
    ApiResponse<UserData> apiResponse = null;

    MutableLiveData<ApiResponse<ResendOtpData>> responseResendLiveData = new MutableLiveData<>();
    ApiResponse<ResendOtpData> apiResendResponse = null;


    private RestApi restApi = null;
    private Disposable subscription = null;


    {
        restApi = RestApiFactory.create();
        apiResponse = new ApiResponse<>(ApiResponse.Status.LOADING, null, null);
        apiResendResponse = new ApiResponse<>(ApiResponse.Status.LOADING, null, null);
    }

    public final void verifyOtp(HashMap<String, String> reqData) {
        subscription = restApi.verifyOtp(reqData)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(new Consumer<Disposable>() {
                    @Override
                    public void accept(Disposable disposable) throws Exception {
                        responseLiveData.postValue(apiResponse.loading());
                    }
                })
                .subscribe(new Consumer<UserData>() {
                    @Override
                    public void accept(UserData verifyOtpData) throws Exception {
                        responseLiveData.postValue(apiResponse.success(verifyOtpData));
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        responseLiveData.postValue(apiResponse.error(throwable));
                    }
                });

    }

    public final void resendOtp(HashMap<String, String> reqData) {
        subscription = restApi.resendOtp(reqData)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(new Consumer<Disposable>() {
                    @Override
                    public void accept(Disposable disposable) throws Exception {
                        responseResendLiveData.postValue(apiResendResponse.loading());
                    }
                })
                .subscribe(new Consumer<ResendOtpData>() {
                    @Override
                    public void accept(ResendOtpData resendOtpData) throws Exception {
                        responseResendLiveData.postValue(apiResendResponse.success(resendOtpData));
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        responseResendLiveData.postValue(apiResendResponse.error(throwable));
                    }
                });

    }


    public void disposeSubscriber() {
        if (subscription != null)
            subscription.dispose();
    }


    /*Validations*/
    public boolean isValidFormData(AppCompatActivity mActivity, String edt1, String edt2, String edt3, String edt4, String edt5) {
        if (TextUtils.isEmpty(edt1) || TextUtils.isEmpty(edt2) || TextUtils.isEmpty(edt3) || TextUtils.isEmpty(edt4) || TextUtils.isEmpty(edt5)) {
            Utility.showSnackBarMsgError(mActivity, mActivity.getString(R.string.enter_otp));
            return false;
        }
        return true;
    }

}
