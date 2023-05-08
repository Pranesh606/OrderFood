package com.exlfood.DataSets;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class VendorZoneApi {

   @SerializedName("success")
   @Expose
   public SuccessDataSet success;

   @SerializedName("error")
   @Expose
   public ErrorDataSet error;

   @SerializedName("details")
   @Expose
   public ArrayList<VendorZoneDetailsDataSet> vendorZoneDetails;


}
