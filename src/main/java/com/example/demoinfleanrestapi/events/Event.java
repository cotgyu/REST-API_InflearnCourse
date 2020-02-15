package com.example.demoinfleanrestapi.events;

import com.example.demoinfleanrestapi.accounts.Account;
import com.example.demoinfleanrestapi.accounts.AccountSerializer;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Builder @AllArgsConstructor @NoArgsConstructor
@Getter @Setter @EqualsAndHashCode(of = "id")
@Entity
public class Event {

    @Id @GeneratedValue
    private Integer id;

    private String name;
    private String description;
    private LocalDateTime beginEnrollmentDateTime;
    private LocalDateTime closeEnrollmentDateTime;
    private LocalDateTime beginEventDateTime;
    private LocalDateTime endEventDateTime;
    private String location; // (optional) 이게 없으면 온라인 모임 private int basePrice; // (optional)
    private int basePrice;
    private int maxPrice; // (optional)
    private int limitOfEnrollment;

    private boolean offline;
    private boolean free;

    @Builder.Default
    @Enumerated(EnumType.STRING) // 기본값은 ORDINAL 순서로 숫자값이 저장됨
    private EventStatus eventStatus= EventStatus.DRAFT;

    @ManyToOne
    @JsonSerialize(using = AccountSerializer.class)
    private Account manager;

    public void update() {

        // Update free
        if(this.basePrice == 0 && this.maxPrice ==0){
            this.free = true;
        }else{
            this.free = false;
        }


        // Update offline
        if(this.location == null || this.location.trim().isEmpty()){
            this.offline = false;
        }else{
            this.offline = true;
        }
    }
}
