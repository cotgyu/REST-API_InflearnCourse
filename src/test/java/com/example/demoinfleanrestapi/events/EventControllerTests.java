package com.example.demoinfleanrestapi.events;

import com.example.demoinfleanrestapi.accounts.Account;
import com.example.demoinfleanrestapi.accounts.AccountRepository;
import com.example.demoinfleanrestapi.accounts.AccountRoles;
import com.example.demoinfleanrestapi.accounts.AccountService;
import com.example.demoinfleanrestapi.common.AppProperties;
import com.example.demoinfleanrestapi.common.BaseControllerTest;
import com.example.demoinfleanrestapi.common.TestDescription;
import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.common.util.Jackson2JsonParser;
import org.springframework.test.web.servlet.ResultActions;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.stream.IntStream;

import static org.springframework.restdocs.headers.HeaderDocumentation.*;
import static org.springframework.restdocs.hypermedia.HypermediaDocumentation.linkWithRel;
import static org.springframework.restdocs.hypermedia.HypermediaDocumentation.links;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


public class EventControllerTests extends BaseControllerTest {

    @Autowired
    EventRepository eventRepository;

    @Autowired
    AccountService accountService;

    @Autowired
    AccountRepository accountRepository;

    @Autowired
    AppProperties appProperties;


    @Before
    public void setUp(){
        this.eventRepository.deleteAll();
        this.accountRepository.deleteAll();
    }


    @Test
    @TestDescription("정상적으로 이벤트를 생성하는 테스트")
    public void createEvent() throws Exception {

        EventDto event = EventDto.builder()
                .name("Spring")
                .description("REST")
                .beginEnrollmentDateTime(LocalDateTime.of(2019,11,1,1,1))
                .closeEnrollmentDateTime(LocalDateTime.of(2019,11,1,1,2))
                .beginEventDateTime(LocalDateTime.of(2019,11,1,1,1))
                .endEventDateTime(LocalDateTime.of(2019,11,1,1,2))
                .basePrice(100)
                .maxPrice(200)
                .limitOfEnrollment(100)
                .location("강남")
                .build();


        mockMvc.perform(post("/api/events")
                    .header(HttpHeaders.AUTHORIZATION, getBearerToken(true))
                    .contentType(MediaType.APPLICATION_JSON_UTF8)
                    .accept(MediaTypes.HAL_JSON)
                    .content(objectMapper.writeValueAsString((event)))
                )
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("id").exists())
                .andExpect(header().exists(HttpHeaders.LOCATION))
                .andExpect(header().string(HttpHeaders.CONTENT_TYPE, MediaTypes.HAL_JSON_UTF8_VALUE))
                .andExpect(jsonPath("id").value(Matchers.not(100)))
                .andExpect(jsonPath("free").value(false))
                .andExpect(jsonPath("offline").value(true))
                .andExpect(jsonPath("eventStatus").value(EventStatus.DRAFT.name()))
                .andExpect(jsonPath("_links.self").exists())
                .andExpect(jsonPath("_links.query-events").exists())
                .andExpect(jsonPath("_links.update-event").exists())
                .andDo(document("create-event",
                        links(
                                linkWithRel("self").description("link to self"),
                                linkWithRel("query-events").description("link to query events"),
                                linkWithRel("update-event").description("link to update an existing events"),
                                linkWithRel("profile").description("profile to update an existing events")

                                ),
                        requestHeaders(
                                headerWithName(HttpHeaders.ACCEPT).description("accept header"),
                                headerWithName(HttpHeaders.CONTENT_TYPE).description("content type header")
                                ),
                        requestFields(
                                fieldWithPath("name").description("Name of new Event"),
                                fieldWithPath("description").description("description of new Event"),
                                fieldWithPath("beginEnrollmentDateTime").description("date time of begin of new Event"),
                                fieldWithPath("closeEnrollmentDateTime").description("date time of close of new Event"),
                                fieldWithPath("beginEventDateTime").description("date time of begin of new Event"),
                                fieldWithPath("endEventDateTime").description("date time of end of new Event"),
                                fieldWithPath("location").description("location of new Event"),
                                fieldWithPath("basePrice").description("basePrice of new Event"),
                                fieldWithPath("maxPrice").description("maxPrice of new Event"),
                                fieldWithPath("limitOfEnrollment").description("limit of enrollment")
                                ),
                        responseHeaders(
                                headerWithName(HttpHeaders.LOCATION).description("LOCATION header"),
                                headerWithName(HttpHeaders.CONTENT_TYPE).description("content type header")
                                ),
                        relaxedResponseFields(
                                fieldWithPath("id").description("identifier of new Event"),
                                fieldWithPath("name").description("name of new Event"),
                                fieldWithPath("description").description("description of new Event"),
                                fieldWithPath("beginEnrollmentDateTime").description("date time of begin of new Event"),
                                fieldWithPath("closeEnrollmentDateTime").description("date time of close of new Event"),
                                fieldWithPath("beginEventDateTime").description("date time of begin of new Event"),
                                fieldWithPath("endEventDateTime").description("date time of end of new Event"),
                                fieldWithPath("location").description("location of new Event"),
                                fieldWithPath("basePrice").description("basePrice of new Event"),
                                fieldWithPath("maxPrice").description("maxPrice of new Event"),
                                fieldWithPath("limitOfEnrollment").description("limit of enrollment"),
                                fieldWithPath("free").description("it tells is this event is free or not"),
                                fieldWithPath("offline").description("it tells is this event is offline or not"),
                                fieldWithPath("eventStatus").description("event Status"),
                                fieldWithPath("_links.self.href").description("link to self"),
                                fieldWithPath("_links.query-events.href").description("link to query-events"),
                                fieldWithPath("_links.update-event.href").description("link to update-event"),
                                fieldWithPath("_links.profile.href").description("link to profile")
                        )
                ))


        ;


    }

    private String getBearerToken(boolean needToCreateAccount) throws Exception {
        return "Bearer" + getAccessToken(needToCreateAccount);
    }

    private String getAccessToken(boolean needToCreateAccount) throws Exception {
        //Given
        if(needToCreateAccount){
            creatAccount();
        }


        String clientId = "myApp";
        String clientSecret = "pass";

        ResultActions perform = this.mockMvc.perform(post("/oauth/token")
                .with(httpBasic(clientId,clientSecret))
                .param("username", appProperties.getUserUsername())
                .param("password", appProperties.getUserPassword())
                .param("grant_type", "password"))
                ;

        var responseBody = perform.andReturn().getResponse().getContentAsString();
        Jackson2JsonParser parser = new Jackson2JsonParser();

        return parser.parseMap(responseBody).get("access_token").toString();
    }

    private Account creatAccount() {
        Account keesun = Account.builder()
                .email(appProperties.getUserUsername())
                .password(appProperties.getUserPassword())
                .roles(Set.of(AccountRoles.ADMIN, AccountRoles.USER))
                .build();

        return this.accountService.saveAccount(keesun);
    }


    @Test
    @TestDescription("입력 받을 수 없는 값을 사용한 경우에 에러가 발생하는 테스트")
    public void createEvent_Bad_Request() throws Exception {

        Event event = Event.builder()
                .id(100)
                .name("Spring")
                .description("REST")
                .beginEnrollmentDateTime(LocalDateTime.of(2019,11,1,1,1))
                .closeEnrollmentDateTime(LocalDateTime.of(2019,11,1,1,2))
                .beginEventDateTime(LocalDateTime.of(2019,11,1,1,1))
                .endEventDateTime(LocalDateTime.of(2019,11,1,1,2))
                .basePrice(100)
                .maxPrice(200)
                .limitOfEnrollment(100)
                .location("강남")
                .free(true)
                .offline(false)
                .eventStatus(EventStatus.PUBLISHED)
                .build();


        mockMvc.perform(post("/api/events")
                .header(HttpHeaders.AUTHORIZATION, getBearerToken(true))
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .accept(MediaTypes.HAL_JSON)
                .content(objectMapper.writeValueAsString((event)))
        )
                .andDo(print())
                .andExpect(status().isBadRequest())

        ;


    }

    @Test
    @TestDescription("입력 값이 비어있는 경우 에러가 발생하는 테스트")
    public void createEvent_Bad_Request_Empty_Input() throws Exception{
        EventDto eventDto =     EventDto.builder().build();

        this.mockMvc.perform(post("/api/events")
                .header(HttpHeaders.AUTHORIZATION, getBearerToken(true))
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .content(this.objectMapper.writeValueAsString(eventDto)))
                .andExpect(status().isBadRequest());
    }


    @Test
    @TestDescription("입력 값이 잘못된 경우 에러가 발생하는 테스트")
    public void createEvent_Bad_Request_Wrong_Input() throws Exception{

        // 애노테이션 만으로 검증할 수 없는 경우
        EventDto eventDto = EventDto.builder()
                .name("Spring")
                .description("REST")
                .beginEnrollmentDateTime(LocalDateTime.of(2019,11,26,1,1))
                .closeEnrollmentDateTime(LocalDateTime.of(2019,11,25,1,2))
                .beginEventDateTime(LocalDateTime.of(2019,11,24,1,1))
                .endEventDateTime(LocalDateTime.of(2019,11,23,1,2))
                .basePrice(10000)
                .maxPrice(200)
                .limitOfEnrollment(100)
                .location("강남")
                .build();

        this.mockMvc.perform(post("/api/events")
                .header(HttpHeaders.AUTHORIZATION, getBearerToken(true))
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(this.objectMapper.writeValueAsString(eventDto)))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("content[0].objectName").exists())
                //.andExpect(jsonPath("$[0].field").exists())
                .andExpect(jsonPath("content[0].defaultMessage").exists())
                .andExpect(jsonPath("content[0].code").exists())
                //.andExpect(jsonPath("$[0].rejectValue").exists())
                .andExpect(jsonPath("_links.index").exists())
                ;
    }

    @Test
    @TestDescription("30개의 이벤트를 10개씩 두번째 페이지 조회하기 ")
    public void queryEvents() throws Exception{
        // Given
        IntStream.range(0,30).forEach(i -> {
            this.generateEvent(i);
        });

        // When & Then
        this.mockMvc.perform(get("/api/events")
                    .param("page","1")
                    .param("size", "10")
                    .param("sort", "name,DESC"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("page").exists())
                .andExpect(jsonPath("_embedded.eventList[0]._links.self").exists())
                .andExpect(jsonPath("_links.self").exists())
                .andExpect(jsonPath("_links.profile").exists())
                .andDo(document("query-events"))
        ;
    }

    @Test
    @TestDescription("30개의 이벤트를 10개씩 두번째 페이지 조회하기 ")
    public void queryEventsWithAuthentication() throws Exception{

        // Given
        IntStream.range(0,30).forEach(i -> {
            this.generateEvent(i);
        });

        // When & Then
        this.mockMvc.perform(get("/api/events")
                .header(HttpHeaders.AUTHORIZATION, getBearerToken(true))
                .param("page","1")
                .param("size", "10")
                .param("sort", "name,DESC"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("page").exists())
                .andExpect(jsonPath("_embedded.eventList[0]._links.self").exists())
                .andExpect(jsonPath("_links.self").exists())
                .andExpect(jsonPath("_links.profile").exists())
                .andExpect(jsonPath("_links.create-event").exists())
                .andDo(document("query-events"))
        ;
    }


    @Test
    @TestDescription("기존의 이벤트를 하나 조회하기")
    public void getEvent() throws Exception{
        //Given
        Account account = creatAccount();
        Event event = this.generateEvent(100, account);

        // When & Then
        this.mockMvc.perform(get("/api/events/{id}", event.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("name").exists())
                .andExpect(jsonPath("id").exists())
                .andExpect(jsonPath("_links.self").exists())
                .andExpect(jsonPath("_links.profile").exists())
                .andDo(document("get-an-event"))
                ;
    }

    @Test
    @TestDescription("없는 이벤트를 조회하였을 때 404 응답받기 ")
    public void getEvent404() throws Exception{
        // When & Then
        this.mockMvc.perform(get("/api/events/111883"))
                .andExpect(status().isNotFound());

    }

    @Test
    @TestDescription("이벤트를 정상적으로 수정하기")
    public void updateEvent() throws Exception{
        //Given

        Account account = this.creatAccount();
        Event event = this.generateEvent(200, account);

        EventDto eventDto = this.modelMapper.map(event, EventDto.class);
        String eventName = "Updated Event";
        eventDto.setName(eventName);

        //When & Then
        this.mockMvc.perform(put("/api/events/{id}",event.getId())
                .header(HttpHeaders.AUTHORIZATION, getBearerToken(false))
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(this.objectMapper.writeValueAsString(eventDto)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("name").value(eventName))
                .andExpect(jsonPath("_links.self").exists());



    }


    @Test
    @TestDescription("입력한 값이 비어있는 경우에 이벤트 수정 실")
    public void updateEvent400_Empty() throws Exception{
        //Given
        Event event = this.generateEvent(200);

        EventDto eventDto = new EventDto();

        //When & Then
        this.mockMvc.perform(put("/api/events/{id}",event.getId())
                .header(HttpHeaders.AUTHORIZATION, getBearerToken(true))
                    .contentType(MediaType.APPLICATION_JSON_UTF8)
                    .content(this.objectMapper.writeValueAsString(eventDto)))
                .andDo(print())
                .andExpect(status().isBadRequest());

    }

    @Test
    @TestDescription("입력한 값이 잘못 경우에 이벤트 수정 실패")
    public void updateEvent400_Wrong() throws Exception{
        //Given
        Event event = this.generateEvent(200);

        EventDto eventDto = this.modelMapper.map(event, EventDto.class);
        eventDto.setBasePrice(200000);
        eventDto.setMaxPrice(1000);

        //When & Then
        this.mockMvc.perform(put("/api/events/{id}",event.getId())
                .header(HttpHeaders.AUTHORIZATION, getBearerToken(true))
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(this.objectMapper.writeValueAsString(eventDto)))
                .andDo(print())
                .andExpect(status().isBadRequest());

    }


    @Test
    @TestDescription("존재하지 않는 이벤트 수정 실패")
    public void updateEvent404() throws Exception{
        //Given
        Event event = this.generateEvent(200);

        EventDto eventDto = this.modelMapper.map(event, EventDto.class);
        eventDto.setBasePrice(200000);
        eventDto.setMaxPrice(1000);

        //When & Then
        this.mockMvc.perform(put("/api/events/11111111")
                .header(HttpHeaders.AUTHORIZATION, getBearerToken(true))
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(this.objectMapper.writeValueAsString(eventDto)))
                .andDo(print())
                .andExpect(status().isNotFound());

    }

    private Event generateEvent(int index, Account account) {
        Event event = buildEvent(index);

        event.setManager(account);
        return this.eventRepository.save(event);
    }


    private Event generateEvent(int index) {
        Event event = buildEvent(index);

        return this.eventRepository.save(event);
    }

    private Event buildEvent(int index) {
        return Event.builder()
                    .name("event" + index)
                    .description("REST")
                    .beginEnrollmentDateTime(LocalDateTime.of(2019,11,1,1,1))
                    .closeEnrollmentDateTime(LocalDateTime.of(2019,11,1,1,2))
                    .beginEventDateTime(LocalDateTime.of(2019,11,1,1,1))
                    .endEventDateTime(LocalDateTime.of(2019,11,1,1,2))
                    .basePrice(100)
                    .maxPrice(200)
                    .limitOfEnrollment(100)
                    .location("강남")
                    .free(false)
                    .offline(true)
                    .eventStatus(EventStatus.DRAFT)
                    .build();
    }


}
