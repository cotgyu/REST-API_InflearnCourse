package com.example.demoinfleanrestapi.events;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;

import java.time.LocalDateTime;

@Component
public class EventValidator {

    public void validate(EventDto eventDto, Errors errors){
        if(eventDto.getBasePrice() > eventDto.getMaxPrice() && eventDto.getMaxPrice() != 0){
            // field 에러
            errors.rejectValue("basePrice", "wrongValue","BasePrice is wrong.");
            errors.rejectValue("maxPrice", "wrongValue","MaxPrice is wrong.");
            // global 에러
            errors.reject("wrongPrices", "Values for prices are wrong");
        }

        LocalDateTime endEventDateTime = eventDto.getEndEventDateTime();
        if(endEventDateTime.isBefore(eventDto.getBeginEventDateTime()) ||
        endEventDateTime.isBefore(eventDto.getCloseEnrollmentDateTime()) ||
        endEventDateTime.isBefore(eventDto.getBeginEventDateTime())){
            errors.rejectValue("endEventDateTime", "wrongValue","endEventDateTime is wrong.");

        }

        // TODO beginEventDateTime
        // TODO CloseEnrollmentDateTime
    }
}
