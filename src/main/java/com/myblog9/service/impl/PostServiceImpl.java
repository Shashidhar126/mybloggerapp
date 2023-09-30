package com.myblog9.service.impl;


import com.myblog9.entity.Post;
import com.myblog9.exceptions.ResourceNotFound;
import com.myblog9.payload.PostDto;
import com.myblog9.payload.PostResponse;
import com.myblog9.repository.PostRepository;
import com.myblog9.service.PostService;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PostServiceImpl implements PostService {
    public PostServiceImpl(PostRepository postRepository, ModelMapper modelMapper) {
        this.postRepository = postRepository;
        this.modelMapper = modelMapper;
    }

    private PostRepository postRepository;
    private ModelMapper modelMapper;
    @Override
    public PostDto savePost(PostDto postDto) {

        Post post = mapToEntity(postDto);//converting dto to entity
        Post savedPost=postRepository.save(post);
        PostDto dto = mapToDto(savedPost);//converting entity to dto

        return dto;
    }

    @Override
    public void deletePost(long id) {
        postRepository.deleteById(id);
    }

    @Override
    public PostDto updatePost(long id, PostDto postDto) {
        Post post =postRepository.findById(id).orElseThrow(

                ()->new ResourceNotFound("Post not found with id:"+id)//iam just throwing exception
        );//finding post by id,if not found throw exception
        post.setTitle(postDto.getTitle());//set new dto content into entity object
        post.setContent(postDto.getContent());
        post.setDescription(postDto.getDescription());
        Post updatePost = postRepository.save(post);//save the edited post back to database
        PostDto dto = mapToDto(updatePost);//converting entity to dto
        return dto;
    }

    @Override
    public PostDto getPostById(long id) {
        Post post =postRepository.findById(id).orElseThrow(
                ()->new ResourceNotFound("Post not found with id:"+id)
        );
        PostDto dto = mapToDto(post);
        return dto;
    }

    @Override
    public PostResponse getPosts(int pageNo, int pageSize, String sortBy, String sortDir) {
        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name()) ?
                Sort.by(sortBy).ascending()//if sortDir=ascending we do ascending dorting
                : Sort.by(sortBy).descending();//if not descending sorting
        Pageable pageable=PageRequest.of(pageNo,pageSize, sort);//pagerequest  is abuilt in class which has lot of overloaded method

        Page<Post> pagePosts = postRepository.findAll(pageable);//take pageable object and paste it here,which will return a page of post
        List<Post>posts = pagePosts.getContent();//get content wil convert page of posts ionto list
        List<PostDto> postDtos = posts.stream().map(post -> mapToDto(post)).collect(Collectors.toList());//this line will convert all list of entity onjects to dto objects
       //all bellow methods together performs pagination
        PostResponse postResponse=new PostResponse();//creating the object of post response
        postResponse.setPostDto(postDtos);//copying the contents from postDtos to postResponse
        postResponse.setPageNo(pagePosts.getNumber());
        postResponse.setPageSize(pagePosts.getSize());
        postResponse.setTotalElements(pagePosts.getTotalElements());
        postResponse.setLast(pagePosts.isLast());
        return postResponse;
    }

    PostDto mapToDto(Post post){
       PostDto dto= modelMapper.map(post,PostDto.class);
//       PostDto dto=new PostDto();
//       dto.setContent(post.getContent());
//       dto.setId(post.getId());
//       dto.setTitle(post.getTitle());
//       dto.setDescription(post.getDescription());
        return dto;
    }
    Post mapToEntity(PostDto postDto){
        Post post=modelMapper.map(postDto,Post.class);
//        Post post=new Post();
//        post.setTitle(postDto.getTitle());
//        post.setDescription(postDto.getDescription());
//        post.setContent(postDto.getContent());
        return post;
    }
}

