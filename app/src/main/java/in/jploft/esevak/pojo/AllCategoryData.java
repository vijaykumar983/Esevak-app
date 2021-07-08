
package in.jploft.esevak.pojo;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class AllCategoryData {

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

        @SerializedName("category")
        private List<Category> mCategory;

        public List<Category> getCategory() {
            return mCategory;
        }

        public void setCategory(List<Category> category) {
            mCategory = category;
        }

    }

    public static class Category {

        @SerializedName("id")
        private String mId;
        @SerializedName("image")
        private String mImage;
        @SerializedName("name")
        private String mName;

        public String getId() {
            return mId;
        }

        public void setId(String id) {
            mId = id;
        }

        public String getImage() {
            return mImage;
        }

        public void setImage(String image) {
            mImage = image;
        }

        public String getName() {
            return mName;
        }

        public void setName(String name) {
            mName = name;
        }

    }


}
