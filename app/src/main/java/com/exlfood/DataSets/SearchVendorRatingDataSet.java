package com.exlfood.DataSets;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SearchVendorRatingDataSet {

   @SerializedName("rating")
   @Expose
   public String rating;

   public String getRating() {
      return rating;
   }

   public void setRating(String rating) {
      this.rating = rating;
   }
}
