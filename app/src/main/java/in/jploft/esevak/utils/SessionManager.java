package in.jploft.esevak.utils;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;

import in.jploft.esevak.pojo.UserData;

import static android.content.Context.MODE_PRIVATE;

public class SessionManager extends BaseObservable {

    private final String IS_LOGIN = "isLoggedIn";
    private final String AUTH_TOKEN = "auth_token";

    private final String USER_ID = "user_id";
    private final String FULL_NAME = "full_name";
    private final String EMAIL = "email";
    private final String PHONE = "phone";
    private final String STATUS = "status";
    private final String DEVICE_ID = "device_id";
    private final String VERIFY_STATUS = "verify_status";
    private final String FIRST_NAME = "first_name";
    private final String LAST_NAME = "last_name";
    private final String DOB = "dob";
    private final String GENDER = "gender";
    private final String REFER_CODE = "refer_code";
    private final String WALLET = "wallet";
    private final String REWALLET = "rewallet";
    private final String ADDRESS = "address";
    private final String PROFILE_IMAGE = "profile_image";
    private final String PIN = "pin";
    private final String UPI = "upi";
    private final String DELETE = "delete";
    private final String ADD_PHONE = "add_phone";

    private final String LOCATION = "location";
    private final String LATITUDE = "latitude";
    private final String LONGITUDE = "longitude";
    private final String SELECT_LOCATION = "select_location";


    private static SharedPreferences shared;
    private static SharedPreferences.Editor editor;
    private static SessionManager session;

    public static SessionManager getInstance(Context context) {
        if (session == null) {
            session = new SessionManager();
        }
        if (shared == null) {
            shared = context.getSharedPreferences("EsevakApp", MODE_PRIVATE);
            editor = shared.edit();
        }
        return session;
    }


    public boolean isLogin() {
        return shared.getBoolean(IS_LOGIN, false);
    }

    public void setLogin() {
        editor.putBoolean(IS_LOGIN, true);
        editor.commit();
    }

    public String getAuthToken() {
        return shared.getString(AUTH_TOKEN, "");
    }

    public void setAuthToken(String authToken) {
        editor.putString(AUTH_TOKEN, authToken);
        editor.commit();
    }

    public String getLOCATION() {
        return shared.getString(LOCATION, "");
    }

    public void setLOCATION(String location) {
        editor.putString(LOCATION, location);
        editor.commit();
    }
    public String getLATITUDE() {
        return shared.getString(LATITUDE, "");
    }

    public void setLATITUDE(String latitude) {
        editor.putString(LATITUDE, latitude);
        editor.commit();
    }
    public String getLONGITUDE() {
        return shared.getString(LONGITUDE, "");
    }

    public void setLONGITUDE(String longitude) {
        editor.putString(LONGITUDE, longitude);
        editor.commit();
    }
    public boolean isSELECT_LOCATION() {
        return shared.getBoolean(SELECT_LOCATION, false);
    }

    public void setSELECT_LOCATION() {
        editor.putBoolean(SELECT_LOCATION, true);
        editor.commit();
    }

    @Bindable("data")
    public UserData.Data getUserData() {

        UserData.Data userData = new UserData.Data();
        userData.setId(shared.getString(USER_ID, ""));
        userData.setFullname(shared.getString(FULL_NAME, ""));
        userData.setEmail(shared.getString(EMAIL, ""));
        userData.setPhone(shared.getString(PHONE, ""));
        userData.setStatus(shared.getString(STATUS, ""));
        userData.setDeviceId(shared.getString(DEVICE_ID, ""));
        userData.setVerifyStatus(shared.getString(VERIFY_STATUS, ""));
        userData.setFirstName(shared.getString(FIRST_NAME, ""));
        userData.setLastName(shared.getString(LAST_NAME, ""));
        userData.setDob(shared.getString(DOB, ""));
        userData.setGender(shared.getString(GENDER, ""));
        userData.setReferCode(shared.getString(REFER_CODE, ""));
        userData.setWallet(shared.getString(WALLET, ""));
        userData.setRewallet(shared.getString(REWALLET, ""));
        userData.setAddress(shared.getString(ADDRESS, ""));
        userData.setProfileImage(shared.getString(PROFILE_IMAGE, ""));
        userData.setPin(shared.getString(PIN, ""));
        userData.setUpi(shared.getString(UPI, ""));
        userData.setDelete(shared.getString(DELETE, ""));
        userData.setAddphone(shared.getString(ADD_PHONE, ""));
        return userData;
    }

    @Bindable("data")
    public void setUserData(UserData.Data userData) {

        editor.putString(USER_ID, userData.getId());
        editor.putString(FULL_NAME, userData.getFullname());
        editor.putString(EMAIL, userData.getEmail());

        editor.putString(PHONE, userData.getPhone());
        editor.putString(STATUS, userData.getStatus());
        editor.putString(DEVICE_ID, userData.getDeviceId());
        editor.putString(VERIFY_STATUS, userData.getVerifyStatus());
        editor.putString(FIRST_NAME, userData.getFirstName());
        editor.putString(LAST_NAME, userData.getLastName());
        editor.putString(DOB, userData.getDob());
        editor.putString(GENDER, userData.getGender());
        editor.putString(REFER_CODE, userData.getReferCode());
        editor.putString(WALLET, userData.getWallet());
        editor.putString(REWALLET, userData.getRewallet());
        editor.putString(ADDRESS, userData.getAddress());
        editor.putString(PROFILE_IMAGE, userData.getProfileImage());
        editor.putString(PIN, userData.getPin());
        editor.putString(UPI, userData.getUpi());
        editor.putString(DELETE, userData.getDelete());
        editor.putString(ADD_PHONE, userData.getAddphone());
        editor.commit();
    }


    public void logout() {

        editor.putString(USER_ID, "");
        editor.putString(FULL_NAME, "");
        editor.putString(EMAIL, "");
        editor.putString(PHONE, "");
        editor.putString(STATUS, "");
        editor.putString(DEVICE_ID, "");
        editor.putString(VERIFY_STATUS, "");
        editor.putString(FIRST_NAME, "");
        editor.putString(LAST_NAME, "");
        editor.putString(DOB, "");
        editor.putString(GENDER, "");
        editor.putString(REFER_CODE, "");
        editor.putString(WALLET, "");
        editor.putString(REWALLET, "");
        editor.putString(ADDRESS, "");
        editor.putString(PROFILE_IMAGE, "");
        editor.putString(PIN, "");
        editor.putString(UPI, "");
        editor.putString(DELETE, "");
        editor.putString(ADD_PHONE, "");
        editor.putString(LOCATION, "");
        editor.putBoolean(SELECT_LOCATION, false);
        editor.putString(LATITUDE, "");
        editor.putString(LONGITUDE, "");
        editor.putString(AUTH_TOKEN, "");
        editor.putBoolean(IS_LOGIN, false);
        editor.commit();
    }
}