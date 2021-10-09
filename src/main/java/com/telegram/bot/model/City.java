package com.telegram.bot.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;

@Entity
@Table(name = "Cities")
@Getter
@Setter
@RequiredArgsConstructor
@ToString
public class City {

    @Id
    @Column(name = "ct_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "ct_name")
    private String name;

    @Column(name = "ct_info")
    private String info;

    @ManyToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "cn_id")
    private Country country;
}
