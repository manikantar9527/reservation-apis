package com.persistent.dto;

import java.util.Date;

import javax.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PassengerDto {
	private Long userId;
	@NotNull(message = "Name can not be null")
	private String name;
	@NotNull(message = "Contact Number can not be null")
	private String contactNumber;
	private String emailId;
	@NotNull(message = "Gender can not be null")
	private String gender;
	@NotNull(message = "Age can not be null")
	private Integer age;
	private String address;
	// @JsonIgnore
	private String password;
	private Date createdOn;
}
