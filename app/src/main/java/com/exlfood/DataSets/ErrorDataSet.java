package com.exlfood.DataSets;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ErrorDataSet {

   @SerializedName("status")
   @Expose
   public String status;
   @SerializedName("message")
   @Expose
   public String message;

   public String getStatus() {
      return status;
   }

   public void setStatus(String status) {
      this.status = status;
   }

   public String getMessage() {
      return message;
   }

   public void setMessage(String message) {
      this.message = message;
   }

}
