package in.jploft.esevak.pojo;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class TimeSlotData {

    @SerializedName("timeslot")
    private List<TimeslotItem> timeslot;

    @SerializedName("message")
    private String message;

    @SerializedName("statusCode")
    private String statusCode;

    public void setTimeslot(List<TimeslotItem> timeslot) {
        this.timeslot = timeslot;
    }

    public List<TimeslotItem> getTimeslot() {
        return timeslot;
    }

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


    public static class TimeslotItem {

        @SerializedName("date")
        private String date;

        @SerializedName("start_time")
        private String startTime;

        @SerializedName("end_time")
        private String endTime;

        @SerializedName("id")
        private String id;

        @SerializedName("day")
        private String day;

        @SerializedName("delete")
        private String delete;

        @SerializedName("status")
        private String status;

        public void setDate(String date) {
            this.date = date;
        }

        public String getDate() {
            return date;
        }

        public void setStartTime(String startTime) {
            this.startTime = startTime;
        }

        public String getStartTime() {
            return startTime;
        }

        public void setEndTime(String endTime) {
            this.endTime = endTime;
        }

        public String getEndTime() {
            return endTime;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getId() {
            return id;
        }

        public void setDay(String day) {
            this.day = day;
        }

        public String getDay() {
            return day;
        }

        public void setDelete(String delete) {
            this.delete = delete;
        }

        public String getDelete() {
            return delete;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getStatus() {
            return status;
        }
    }
}