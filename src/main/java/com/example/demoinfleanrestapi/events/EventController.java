package com.example.demoinfleanrestapi.events;

import com.example.demoinfleanrestapi.common.ErrorsResource;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.MediaTypes;
import org.springframework.hateoas.PagedResources;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.mvc.ControllerLinkBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.util.Optional;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;

@Controller
@RequestMapping(value = "/api/events", produces = MediaTypes.HAL_JSON_UTF8_VALUE)
public class EventController {

    private final EventRepository eventRepository;

    private final ModelMapper modelMapper;

    private final EventValidator eventValidator;

    public EventController(EventRepository eventRepository, ModelMapper modelMapper, EventValidator eventValidator){
        this.eventRepository = eventRepository;
        this.modelMapper = modelMapper;
        this.eventValidator = eventValidator;
    }






    @PostMapping
    public ResponseEntity createEvent(@RequestBody @Valid EventDto eventDto, Errors errors){

        // @Valid를 통해 검증한 에러들을 errors에 담고 검증하여 badRequest
        if(errors.hasErrors()){
            return badRequest(errors);
        }


        // validate를 통해 검증한 에러들을 errors에 담고 검증하여 badRequest
        eventValidator.validate(eventDto, errors);
        if(errors.hasErrors()){
            return badRequest(errors);
        }




//        Event event = Event.builder()
//                .name(eventDto.getName())
//                .description(eventDto.getDescription())
//                .build();

        // 위 방법을 생략하는 방법
        Event event = modelMapper.map(eventDto, Event.class);

        event.update();

        Event newEvent = this.eventRepository.save(event);
        ControllerLinkBuilder selfLinkBuilder = linkTo(EventController.class).slash(newEvent.getId());
        URI createUri = selfLinkBuilder.toUri();
        EventResoruce eventResoruce  = new EventResoruce(event);
        eventResoruce.add(linkTo(EventController.class).withRel("query-events"));
        //EventResource로 옮김
        //eventResoruce.add(selfLinkBuilder.withSelfRel());
        eventResoruce.add(selfLinkBuilder.withRel("update-event"));
        eventResoruce.add(new Link("/docs/index.html#resources-events-create").withRel("profile"));

        return ResponseEntity.created(createUri).body(eventResoruce);
    }

    // 리펙토링 해서 생성된 메소드 (option command M)
    private ResponseEntity badRequest(Errors errors) {
        return ResponseEntity.badRequest().body(new ErrorsResource(errors));
    }

    @GetMapping
    public ResponseEntity queryEvents(Pageable pageable, PagedResourcesAssembler<Event> assembler){

        Page<Event> page = this.eventRepository.findAll(pageable);
        PagedResources<Resource<Event>> pagedResources =  assembler.toResource(page, e -> new EventResoruce(e));
        pagedResources.add(new Link("/docs/index.html#resource-events-list").withRel("profile"));

        return ResponseEntity.ok(pagedResources);
    }

    @GetMapping("{id}")
    public ResponseEntity getEvent(@PathVariable Integer id){
        Optional<Event> optionalEvent = this.eventRepository.findById(id);

        if(!optionalEvent.isPresent()){
            return ResponseEntity.notFound().build();
        }

        Event event = optionalEvent.get();
        EventResoruce eventResoruce = new EventResoruce(event);
        eventResoruce.add(new Link("/docs/index.html#resource-events-get").withRel("profile"));

        return ResponseEntity.ok(eventResoruce);
    }

    @PutMapping("/{id}")
    public ResponseEntity updateEvent(@PathVariable Integer id,
                                      @RequestBody @Valid  EventDto eventDto,
                                      Errors errors){
        Optional<Event> optionalEvent = this.eventRepository.findById(id);
        if(!optionalEvent.isPresent()){
            return ResponseEntity.notFound().build();
        }

        if(errors.hasErrors()){
            return badRequest(errors);
        }

        this.eventValidator.validate(eventDto, errors);

        if (errors.hasErrors()) {
            return badRequest(errors);
        }

        Event exisitingEvent =  optionalEvent.get();
        this.modelMapper.map(eventDto, exisitingEvent);
        Event savedEvent = this.eventRepository.save(exisitingEvent);

        EventResoruce eventResoruce = new EventResoruce(savedEvent);
        eventResoruce.add(new Link("/docs/index.html#resource-events-update").withRel("profile"));

        return ResponseEntity.ok(eventResoruce);

    }



}
