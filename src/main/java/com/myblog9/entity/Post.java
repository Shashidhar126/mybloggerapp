package com.myblog9.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import javax.persistence.*;
import java.util.List;

@Data//encapsulation is achieved
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table
        (
                name = "posts", uniqueConstraints = {@UniqueConstraint(columnNames = {"title"})}//created table name in database called posts and makes column title as unique
        )
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "title", nullable = false)
    private String title;
    @Column(name = "description", nullable = false)
    private String description;
    @Column(name = "content", nullable = false)
    private String content;
    @OneToMany(mappedBy = "post",cascade = CascadeType.ALL)
    private List<Comment> comments;

}