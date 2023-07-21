package com.example.newneekclone.global.security;


import com.example.newneekclone.domain.user.entity.User;
import com.example.newneekclone.domain.user.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import static com.example.newneekclone.global.enums.ErrorCode.USER_NOT_FOUND;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;

    public UserDetailsServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(username).orElseThrow(() -> new UsernameNotFoundException(USER_NOT_FOUND.getDetail()));
        return new UserDetailsImpl(user);
    }

    public UserDetails loadUserByNickname(String Nickname) throws UsernameNotFoundException {
        User user = userRepository.findByNickname(Nickname)
                .orElseThrow(() -> new UsernameNotFoundException(USER_NOT_FOUND.getDetail()));

        return new UserDetailsImpl(user);
    }
}
