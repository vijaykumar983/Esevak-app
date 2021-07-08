package in.jploft.esevak.pojo;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class AllCityData {

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

        @SerializedName("cities")
        private List<CitiesItem> cities;

        public void setCities(List<CitiesItem> cities) {
            this.cities = cities;
        }

        public List<CitiesItem> getCities() {
            return cities;
        }

        public static class CitiesItem {

            @SerializedName("name")
            private String name;

            @SerializedName("id")
            private String id;

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