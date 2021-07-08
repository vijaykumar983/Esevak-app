
package in.jploft.esevak.pojo;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ProductDetailData {

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

        @SerializedName("description")
        private String mDescription;
        @SerializedName("faq")
        private List<Faq> mFaq;
        @SerializedName("id")
        private String mId;
        @SerializedName("image")
        private String mImage;
        @SerializedName("name")
        private String mName;
        @SerializedName("price")
        private String mPrice;

        public String getDescription() {
            return mDescription;
        }

        public void setDescription(String description) {
            mDescription = description;
        }

        public List<Faq> getFaq() {
            return mFaq;
        }

        public void setFaq(List<Faq> faq) {
            mFaq = faq;
        }

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

        public String getPrice() {
            return mPrice;
        }

        public void setPrice(String price) {
            mPrice = price;
        }

    }

    public static class Faq {

        @SerializedName("content")
        private String mContent;
        @SerializedName("id")
        private String mId;
        @SerializedName("title")
        private String mTitle;

        public String getContent() {
            return mContent;
        }

        public void setContent(String content) {
            mContent = content;
        }

        public String getId() {
            return mId;
        }

        public void setId(String id) {
            mId = id;
        }

        public String getTitle() {
            return mTitle;
        }

        public void setTitle(String title) {
            mTitle = title;
        }

    }


}
