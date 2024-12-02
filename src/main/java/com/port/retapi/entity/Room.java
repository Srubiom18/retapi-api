package com.port.retapi.entity;

import jakarta.persistence.*;
import lombok.*;

import java.sql.Date;
import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "rooms")
public class Room {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String inviteCode; 

    private String name;

    private String description;

    private Date creationDate;

    private Date initDate;

    private Date endDate;

    private Integer maxPlayer;

    private Integer challengePerPerson;

    List<Challenge> totalChallengesList;





}
