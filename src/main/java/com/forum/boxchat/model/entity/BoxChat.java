package com.forum.boxchat.model.entity;

import com.forum.boxchat.model.enums.BoxChatType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "box_chat")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class BoxChat {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column( name = "id")
    private int id;

    @Column( name = "name", nullable = false, length = 70 )
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false)
    private BoxChatType type;

    @OneToMany(mappedBy = "boxChat", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<BoxParticipant> participants = new ArrayList<>();

    @OneToMany(mappedBy = "boxChat", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Message> messages = new ArrayList<>();
}

