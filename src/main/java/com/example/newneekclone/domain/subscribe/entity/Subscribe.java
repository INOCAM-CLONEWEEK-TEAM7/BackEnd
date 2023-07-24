package com.example.newneekclone.domain.subscribe.entity;

import com.example.newneekclone.domain.subscribe.dto.SubscribeRequestDto;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "subscribe")
@Getter
@Setter
@NoArgsConstructor
public class Subscribe {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "email")
    private String email;

    @Column(name = "nickname")
    private String nickname;


    public Subscribe(String email, String nickname) {
        this.email = email;
        this.nickname = nickname;
    }

}
