package in.jploft.esevak.ui.components.activities.resetPassword;

import android.text.TextUtils;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.HashMap;

import in.jploft.esevak.R;
import in.jploft.esevak.network.ApiResponse;
import in.jploft.esevak.network.RestApi;
import in.jploft.esevak.network.RestApiFactory;
import in.jploft.esevak.pojo.ForgotPassData;
import in.jploft.esevak.pojo.ResetPassData;
import in.jploft.esevak.utils.Utility;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class ResetPassViewModel extends ViewModel {
    MutableLiveData<ApiResponse<ResetPassData>> responseLiveData = new MutableLiveData<>();
    ApiResponse<ResetPassData> apiResponse = null;

    MutableLiveData<ApiResponse<ForgotPassData>> responseResendLiveData = new MutableLiveData<>();
    ApiResponse<ForgotPassData> apiResendResponse = null;


    private RestApi restApi = null;
    private Disposable subscription = null;


    {
        restApi = RestApiFactory.create();
        apiResponse = new ApiResponse<>(ApiResponse.Status.LOADING, null, null);
        apiResendResponse = new ApiResponse<>(ApiResponse.Status.LOADING, null, null);
    }

    public final void resetPass(HashMap<String, String> reqData) {
        subscription = restApi.resetPass(reqData)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(new Consumer<Disposable>() {
                    @Override
                    public void accept(Disposable disposable) throws Exception {
                        responseLiveData.postValue(apiResponse.loading());
                    }
                })
                .subscribe(new Consumer<ResetPassData>() {
                    @Override
                    public void accept(ResetPassData resetPassData) throws Exception {
                        responseLiveData.postValue(apiResponse.success(resetPassData));
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        responseLiveData.postValue(apiResponse.error(throwable));
                    }
                });

    }

    public final void resendOtp(HashMap<String, String> reqData) {
        subscription = restApi.forgotPass(reqData)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(new Consumer<Disposable>() {
                    @Override
                    public void accept(Disposable disposable) throws Exception {
                        responseResendLiveData.postValue(apiResendResponse.loading());
                    }
                })
                .subscribe(new Consumer<ForgotPassData>() {
                    @Override
                    public void accept(ForgotPassData forgotPassData) throws Exception {
                        responseResendLiveData.postValue(apiResendResponse.success(forgotPassData));
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
    public boolean isValidFormData(AppCompatActivity mActivity, String otp, String pass, String confirmPass) {
        if (TextUtils.isEmpty(otp)) {
            Utility.showSnackBarMsgError(mActivity, mActivity.getString(R.string.enter_mobile));
            return false;
        }
        if (otp.length() != 5) {
            Utility.showSnackBarMsgError(mActivity, mActivity.getString(R.string.enter_valid_otp));
            return false;
        }
        if (TextUtils.isEmpty(pass)) {
            Utility.showSnackBarMsgError(mActivity, mActivity.getString(R.string.enter_password));
            return false;
        }
        if (pass.length() < 6) {
            Utility.showSnackBarMsgError(mActivity, mActivity.getString(R.string.minimum_6_characters));
            return false;
        }

        if (TextUtils.isEmpty(confirmPass)) {
            Utility.showSnackBarMsgError(mActivity, mActivity.getString(R.string.enter_cpassword));
            return false;
        }
        if (!pass.equals(confirmPass)) {
            Utility.showSnackBarMsgError(mActivity, mActivity.getString(R.string.password_not_match));
            return false;
        }

        return true;
    }

}
