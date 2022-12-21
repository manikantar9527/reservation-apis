package com.persistent.dto;

import java.util.Date;

import javax.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AvailabilityDto {
	@NotNull(message = "Date can not be null")
	private Date Date;
	@NotNull(message = "TrainId can not be null")
	private Long trainId;
	private String mobileNumber;
}
