package com.myblog9.security;

import com.myblog9.entity.Role;
import com.myblog9.entity.User;
import com.myblog9.repository.UserRepository;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;
@Service
public class  CustomUserDetailsService implements UserDetailsService {//security related,this class help me to retreive the data from database
    private UserRepository userRepository;

    public CustomUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
    @Override
    public UserDetails loadUserByUsername(String usernameOrEmail) throws UsernameNotFoundException {//automatically spring security takes username from login dto object when we signin by username
        User user = userRepository.findByUsernameOrEmail(usernameOrEmail, usernameOrEmail)//based on username it will go to database and will search the record based on username or email,if username exists user object is intialized,if not it will throw exception
                .orElseThrow(() ->
                        new UsernameNotFoundException("User not found with username or email:" + usernameOrEmail));
        return new
                org.springframework.security.core.userdetails.User(user.getEmail(),
                user.getPassword(), mapRolesToAuthorities(user.getRoles()));//it will fetch perticular roles from Role table and it will initailizes variable
    }
    private Collection< ? extends GrantedAuthority> mapRolesToAuthorities(Set<Role> roles){//roles should come from database wheather user is user or admin and should be assigned .
        return roles.stream().map(role -> new SimpleGrantedAuthority(role.getName())).collect(Collectors.toList());
    }
}
