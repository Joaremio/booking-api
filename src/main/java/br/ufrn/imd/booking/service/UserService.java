package br.ufrn.imd.booking.service;
import br.ufrn.imd.booking.dto.UserRequestDTO;
import br.ufrn.imd.booking.dto.UserResponseDTO;
import br.ufrn.imd.booking.entity.User;
import br.ufrn.imd.booking.enums.Role;
import br.ufrn.imd.booking.exception.EmailAlreadyRegisteredException;
import br.ufrn.imd.booking.mapper.UserMapper;
import br.ufrn.imd.booking.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper userMapper;

    public UserResponseDTO register (UserRequestDTO dto) {
        Optional<User> existingUser = userRepository.findByEmail(dto.email());

        if(existingUser.isPresent()) {
            throw new EmailAlreadyRegisteredException("E-mail já cadastrado: " + dto.email());
        }

        User user = User.builder()
                .name(dto.name())
                .email(dto.email())
                .password(passwordEncoder.encode(dto.password()))
                .role(Role.USER)
                .build();

        User saved = userRepository.save(user);
        return userMapper.toResponseDTO(saved);
    }
}
