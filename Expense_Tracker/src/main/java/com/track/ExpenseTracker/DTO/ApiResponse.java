package com.track.ExpenseTracker.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ApiResponse<T>{
 public Boolean response;
 public String message;
 public T data;

 ApiResponse(Boolean response,String message){
     this.response=response;
     this.message=message;
 }

}
