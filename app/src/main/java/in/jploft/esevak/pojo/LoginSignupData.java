
package in.jploft.esevak.pojo;

import com.google.gson.annotations.SerializedName;

public class LoginSignupData {

    @SerializedName("data")
    private Data mData;
    @SerializedName("message")
    private String mMessage;
    @SerializedName("statusCode")
    private Long mStatusCode;

    public Data getData() {
        return mData;
    }

    public void setData(Data data) {
        mData = data;
    }

    public String getMessage() {
        return mMessage;
    }

    public void setMessage(String message) {
        mMessage = message;
    }

    public Long getStatusCode() {
        return mStatusCode;
    }

    public void setStatusCode(Long statusCode) {
        mStatusCode = statusCode;
    }


    public static class Data {

        @SerializedName("otp")
        private String mOtp;
        @SerializedName("user_id")
        private String mUserId;
        @SerializedName("userType")
        private String mUserType;

        public String getOtp() {
            return mOtp;
        }

        public void setOtp(String otp) {
            mOtp = otp;
        }

        public String getUserId() {
            return mUserId;
        }

        public void setUserId(String userId) {
            mUserId = userId;
        }

        public String getUserType() {
            return mUserType;
        }

        public void setUserType(String userType) {
            mUserType = userType;
        }

    }


}
