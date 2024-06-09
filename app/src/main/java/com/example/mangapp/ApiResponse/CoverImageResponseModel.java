package com.example.mangapp.ApiResponse;
import com.google.gson.annotations.SerializedName;

public class CoverImageResponseModel {
    @SerializedName("data")
    private CoverImageData data;

    public CoverImageData getData() {
        return data;
    }

    public static class CoverImageData {
        private String id;
        private String type;
        private Attributes attributes;

        public String getId() {
            return id;
        }

        public String getType() {
            return type;
        }

        public Attributes getAttributes() {
            return attributes;
        }

        public static class Attributes {
            @SerializedName("fileName")
            private String fileName;

            public String getFileName() {
                return fileName;
            }
        }
    }
}

