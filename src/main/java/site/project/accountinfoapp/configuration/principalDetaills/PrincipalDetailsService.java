package site.project.accountinfoapp.configuration.principalDetaills;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import site.project.accountinfoapp.service.user.domain.User;
import site.project.accountinfoapp.service.user.repository.UserRepository;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PrincipalDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> user = userRepository.findByUsername(username);
        if (user.isEmpty())
            return userRepository.findByUsername(username).map(this::createUserDetails).orElseThrow(() -> {
                throw new UsernameNotFoundException(username + " -> 데이터베이스에서 찾을 수 없습니다.");
            });

        return new PrincipalDetails(user.get());
    }

    private UserDetails createUserDetails(User userAccount) {
        try {
            return org.springframework.security.core.userdetails.User.builder()
                    .username(userAccount.getUsername())
                    .password(userAccount.getPassword())
                    .authorities(String.valueOf(userAccount.getRoles()))
                    .build();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
