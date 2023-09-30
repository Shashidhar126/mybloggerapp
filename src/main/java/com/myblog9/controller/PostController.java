package com.myblog9.controller;

import com.myblog9.payload.PostDto;
import com.myblog9.payload.PostResponse;
import com.myblog9.service.PostService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/post")

public class PostController {
    public PostController(PostService postService) {//constructor based injuction
        this.postService = postService;
    }

    private PostService postService;
    //http://localhost:8080/api/post
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<?>savePost(@Valid @RequestBody PostDto postDto, BindingResult result){//@Valid is the one that enables error checking in controller layer
        if(result.hasErrors()){
            return new ResponseEntity<>(result.getFieldError().getDefaultMessage(),HttpStatus.INTERNAL_SERVER_ERROR);//spring validation
        }
        PostDto dto=postService.savePost(postDto)  ;
        return new ResponseEntity<>(dto, HttpStatus.CREATED);
    }
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")//http://localhost:8080/api/post/1
    public ResponseEntity<String> deletePost(@PathVariable("id") long id){//path parameter
        postService.deletePost(id);
        return new ResponseEntity<>("post is deleted",HttpStatus.OK);
    }
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")//http://localhost:8080/api/post/1
    public ResponseEntity<PostDto>updatePost(@PathVariable("id") long id,@RequestBody PostDto postDto){
        PostDto dto=postService.updatePost(id,postDto);
        return new ResponseEntity<>(dto,HttpStatus.OK);
    }
    //http://localhost:8080/api/post/1
    @GetMapping("/{id}")
    public ResponseEntity<PostDto> getPostById(@PathVariable("id") long id){
        PostDto dto = postService.getPostById(id);
        return new ResponseEntity<>(dto,HttpStatus.OK);
    }
   // http://localhost:8080/api/post?pageNo=0&pageSize=5&sortBy=title&sortDir=desc
    @GetMapping
    public PostResponse getPost( @RequestParam(value="pageNo",defaultValue="0",required =false)int pageNo,
                                  @RequestParam(value="pageSize",defaultValue="5",required =false)int pageSize,
                                  @RequestParam(value = "sortBy", defaultValue = "id", required = false) String sortBy
            ,
                                  @RequestParam(value = "sortDir", defaultValue = "asc", required = false) String sortDir
                                  ){
        PostResponse postResponse=postService.getPosts(pageNo,pageSize,sortBy, sortDir);
        return postResponse;
    }
}