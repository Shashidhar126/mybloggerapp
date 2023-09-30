package com.myblog9.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import javax.persistence.*;
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "comments")
public class Comment {//@joinColumn should be written in child tablechild table
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String name;
    private String email;
    private String body;
    @ManyToOne(fetch = FetchType.LAZY)//
    @JoinColumn(name = "post_id", nullable = false)//@join column joins parent table(posts) column =id(primary key)  with child table(commennts) column- post_id ( foreign key)
    private Post post;//iam not using list
}
