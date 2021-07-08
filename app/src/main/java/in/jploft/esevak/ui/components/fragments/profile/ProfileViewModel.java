package in.jploft.esevak.ui.components.fragments.profile;

import android.text.TextUtils;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.HashMap;

import in.jploft.esevak.R;
import in.jploft.esevak.network.ApiResponse;
import in.jploft.esevak.network.RestApi;
import in.jploft.esevak.network.RestApiFactory;
import in.jploft.esevak.pojo.AllCategoryData;
import in.jploft.esevak.pojo.UserData;
import in.jploft.esevak.pojo.UserData;
import in.jploft.esevak.utils.Utility;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MultipartBody;
import retrofit2.http.Part;

public class ProfileViewModel extends ViewModel {
    MutableLiveData<ApiResponse<UserData>> responseLiveData = new MutableLiveData<>();
    ApiResponse<UserData> apiResponse = null;

    MutableLiveData<ApiResponse<UserData>> responseMyProfileLiveData = new MutableLiveData<>();
    ApiResponse<UserData> apiMyProfileResponse = null;

    private RestApi restApi = null;
    private Disposable subscription = null;

    {
        restApi = RestApiFactory.create();
        apiResponse = new ApiResponse<>(ApiResponse.Status.LOADING, null, null);
        apiMyProfileResponse = new ApiResponse<>(ApiResponse.Status.LOADING, null, null);
    }

    public final void updateProfile(@Part MultipartBody.Part id,
                                    @Part MultipartBody.Part name,
                                    @Part MultipartBody.Part email,
                                    @Part MultipartBody.Part phone,
                                    @Part MultipartBody.Part address,
                                    @Part MultipartBody.Part image) {
        subscription = restApi.updateProfile(id, name,email, phone, address, image)
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
                    public void accept(UserData userData) throws Exception {
                        responseLiveData.postValue(apiResponse.success(userData));
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        responseLiveData.postValue(apiResponse.error(throwable));
                    }
                });

    }

    public final void myProfileApi(HashMap<String, String> reqData) {
        subscription = restApi.myProfile(reqData)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(new Consumer<Disposable>() {
                    @Override
                    public void accept(Disposable disposable) throws Exception {
                        responseMyProfileLiveData.postValue(apiMyProfileResponse.loading());
                    }
                })
                .subscribe(new Consumer<UserData>() {
                    @Override
                    public void accept(UserData UserData) throws Exception {
                        responseMyProfileLiveData.postValue(apiMyProfileResponse.success(UserData));
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        responseMyProfileLiveData.postValue(apiMyProfileResponse.error(throwable));
                    }
                });
    }


    public void disposeSubscriber() {
        if (subscription != null)
            subscription.dispose();
    }

    /*Validations Student*/
    public boolean isValidFormData(AppCompatActivity mActivity, String image, String name, String email, String address, String phone) {
        if (TextUtils.isEmpty(image)) {
            Utility.showSnackBarMsgError(mActivity, mActivity.getString(R.string.please_select_image));
            return false;
        }
        if (TextUtils.isEmpty(name)) {
            Utility.showSnackBarMsgError(mActivity, mActivity.getString(R.string.enter_name));
            return false;
        }

        if (name.length() < 3) {
            Utility.showSnackBarMsgError(mActivity, mActivity.getString(R.string.minimum_3_char_long_name));
            return false;
        }

        if (TextUtils.isEmpty(email)) {
            Utility.showSnackBarMsgError(mActivity, mActivity.getString(R.string.enter_email));
            return false;
        }

        if (!Utility.isValidEmail(email)) {
            Utility.showSnackBarMsgError(mActivity, mActivity.getString(R.string.enter_valid_email_id));
            return false;
        }
        if (TextUtils.isEmpty(phone)) {
            Utility.showSnackBarMsgError(mActivity, mActivity.getString(R.string.enter_mobile));
            return false;
        }
        if (phone.length() < 9) {
            Utility.showSnackBarMsgError(mActivity, mActivity.getString(R.string.enter_valid_mobile_number));
            return false;
        }

        if (TextUtils.isEmpty(address)) {
            Utility.showSnackBarMsgError(mActivity, mActivity.getString(R.string.enter_address));
            return false;
        }

        if (address.length() < 3) {
            Utility.showSnackBarMsgError(mActivity, mActivity.getString(R.string.minimum_3_char_long_address));
            return false;
        }

        return true;
    }
}