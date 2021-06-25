package com.example.demo.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor

@Data
public class UpdatedUserRequest {
	public String phone;
    public String ngaysinh;
    public String sex;
    public String currentPassword;
    public String newPassword;
    public String name;
}
