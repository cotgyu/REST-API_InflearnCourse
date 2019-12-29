package com.example.demoinfleanrestapi.events;

import com.fasterxml.jackson.annotation.JsonUnwrapped;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.ResourceSupport;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;


public class EventResoruce extends Resource<Event> {

//    @JsonUnwrapped
//    private Event event;
//
//    public EventResoruce(Event event){
//        this.event = event;
//    }
//
//    public Event getEvent(){
//        return event;
//    }

    public EventResoruce(Event event, Link... links){
        super(event, links);

        //아래와 같음
        //add(new Link("http://localhost:8080/api/events/" + event.getId()));
        add(linkTo(EventController.class).slash(event.getId()).withSelfRel());
    }
}
