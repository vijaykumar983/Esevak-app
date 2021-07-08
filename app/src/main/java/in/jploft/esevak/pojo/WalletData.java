
package in.jploft.esevak.pojo;

import com.google.gson.annotations.SerializedName;

public class WalletData {

    @SerializedName("message")
    private String mMessage;
    @SerializedName("statusCode")
    private Long mStatusCode;
    @SerializedName("wallet")
    private String mWallet;

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

    public String getWallet() {
        return mWallet;
    }

    public void setWallet(String wallet) {
        mWallet = wallet;
    }

}
