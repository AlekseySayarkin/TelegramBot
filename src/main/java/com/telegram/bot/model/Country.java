package com.telegram.bot.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;

@Entity
@Table(name = "Countries")
@Getter
@Setter
@RequiredArgsConstructor
@ToString
public class Country {

    @Id
    @Column(name = "cn_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "cn_name")
    private String name;

}
