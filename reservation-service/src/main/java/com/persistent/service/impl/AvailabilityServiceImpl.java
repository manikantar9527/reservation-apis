package com.persistent.service.impl;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.persistent.config.AppConstants;
import com.persistent.dao.Availability;
import com.persistent.dao.TrainInfo;
import com.persistent.dto.AvailabilityDto;
import com.persistent.dto.StatusDto;
import com.persistent.repository.AvailabilityRepository;
import com.persistent.repository.TrainInfoRepository;
import com.persistent.service.AvailabilityService;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class AvailabilityServiceImpl implements AvailabilityService {

	@Autowired
	private AvailabilityRepository availabilityRepository;

	@Autowired
	private TrainInfoRepository trainInfoRepository;

	@Override
	public StatusDto addAvailability(AvailabilityDto reqDto) {
		log.info("addAvailability request details" + reqDto);
		List<Availability> availabilities = availabilityRepository.findByTrainTrainIdAndDate(reqDto.getTrainId(),
				reqDto.getDate());
		TrainInfo trainInfo = trainInfoRepository.findByTrainId(reqDto.getTrainId());
		int seatsPerCoach = trainInfo.getTotalSeats() / trainInfo.getNoOfCoaches();
		if (availabilities.isEmpty()) {
			for (int i = 1; i < trainInfo.getNoOfCoaches() + 1; i++) {
				availabilityRepository.save(new Availability(null, reqDto.getDate(), trainInfo, new Date(),
						seatsPerCoach / 2, seatsPerCoach / 2, "c" + i));
			}
			log.info(AppConstants.AVAILABILITY_DETAILS_ADDED);
			return new StatusDto(0, AppConstants.AVAILABILITY_DETAILS_ADDED);
		}
		log.info(AppConstants.AVAILABILITY_DETAILS_ALREADY_EXIST);
		return new StatusDto(1, AppConstants.AVAILABILITY_DETAILS_ALREADY_EXIST);
	}

	@Override
	public List<Availability> ticketAvailability(AvailabilityDto reqDto) {
		return availabilityRepository.findByTrainTrainIdAndDate(reqDto.getTrainId(), reqDto.getDate());
	}
}
