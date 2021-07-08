
package in.jploft.esevak.pojo;

import com.google.gson.annotations.SerializedName;


public class PlaceOrderData {

    @SerializedName("message")
    private String mMessage;
    @SerializedName("statusCode")
    private String mStatusCode;

    public String getMessage() {
        return mMessage;
    }

    public void setMessage(String message) {
        mMessage = message;
    }

    public String getStatusCode() {
        return mStatusCode;
    }

    public void setStatusCode(String statusCode) {
        mStatusCode = statusCode;
    }

}
