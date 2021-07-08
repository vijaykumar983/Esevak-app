package in.jploft.esevak.pojo;

import com.google.gson.annotations.SerializedName;

public class FeedbackData {

    @SerializedName("message")
    private String message;

    @SerializedName("statusCode")
    private String statusCode;

    public void setMessage(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setStatusCode(String statusCode) {
        this.statusCode = statusCode;
    }

    public String getStatusCode() {
        return statusCode;
    }
}