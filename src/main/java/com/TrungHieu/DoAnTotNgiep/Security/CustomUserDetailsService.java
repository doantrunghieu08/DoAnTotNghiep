package com.TrungHieu.DoAnTotNgiep.Security;


import com.TrungHieu.DoAnTotNgiep.Entity.User;
import com.TrungHieu.DoAnTotNgiep.Repository.UserRepository;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@AllArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    UserRepository userRepository;

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Không tìm thấy user: " + username));

        // Đảm bảo lấy đúng password_hash từ DB
        String passwordFromDb = user.getPassword();

        // Tạo quyền với tiền tố ROLE_
        String roleWithPrefix = "ROLE_" + user.getRole();

        return org.springframework.security.core.userdetails.User.withUsername(user.getUsername())
                .password(passwordFromDb)
                .authorities(new SimpleGrantedAuthority(roleWithPrefix)) // Truyền đối tượng Authority chuẩn
                .accountLocked(user.getStatus().name().equals("LOCKED")) // Kiểm tra trạng thái luôn
                .build();
    }
}