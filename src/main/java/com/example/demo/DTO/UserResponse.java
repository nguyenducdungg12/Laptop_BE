package com.example.demo.DTO;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
@AllArgsConstructor
@NoArgsConstructor
@Data
public class UserResponse {
	public String username;
	public String phone;
	public String email;
    public String image;
    private Boolean role;
	public Date createBy;
	public Date updatedBy;

}
