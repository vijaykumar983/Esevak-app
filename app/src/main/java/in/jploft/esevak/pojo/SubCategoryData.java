package in.jploft.esevak.pojo;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class SubCategoryData {

    @SerializedName("data")
    private Data data;

    @SerializedName("message")
    private String message;

    @SerializedName("statusCode")
    private int statusCode;

    public void setData(Data data) {
        this.data = data;
    }

    public Data getData() {
        return data;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public static class Data {

        @SerializedName("category")
        private List<CategoryItem> category;

        public void setCategory(List<CategoryItem> category) {
            this.category = category;
        }

        public List<CategoryItem> getCategory() {
            return category;
        }

        public static class CategoryItem {

            @SerializedName("image")
            private String image;

            @SerializedName("name")
            private String name;

            @SerializedName("id")
            private String id;

            public void setImage(String image) {
                this.image = image;
            }

            public String getImage() {
                return image;
            }

            public void setName(String name) {
                this.name = name;
            }

            public String getName() {
                return name;
            }

            public void setId(String id) {
                this.id = id;
            }

            public String getId() {
                return id;
            }
        }
    }
}