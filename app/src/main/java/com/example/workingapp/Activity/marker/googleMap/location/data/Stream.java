package com.example.workingapp.Activity.marker.googleMap.location.data;


public class Stream {
    private String APIName;
    private String APILocation;

    public Stream(String APIName, String APILocation) {
        this.APIName = APIName;
        this.APILocation = APILocation;
    }

    public String getAPIName() {
        return APIName;
    }

    public void setAPIName(String APIName) {
        this.APIName = APIName;
    }

    public String getAPILocation() {
        return APILocation;
    }

    public void setAPILocation(String APILocation) {
        this.APILocation = APILocation;
    }
}
