package com.example.userservice.service;

import com.example.userservice.domain.dto.model.Role;
import com.example.userservice.domain.dto.model.User;
import com.example.userservice.domain.dto.UserDto;
import com.example.userservice.repository.UserContactsRepository;
import com.example.userservice.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;


import java.util.List;
import java.util.Optional;

import static com.example.userservice.domain.dto.model.Role.ROLE_ADMIN;
import static com.example.userservice.domain.dto.model.Role.ROLE_USER;
import static java.util.Collections.emptyList;

@Service
@AllArgsConstructor
public class UserServiceData implements UserService, UserDetailsService {

    private final BCryptPasswordEncoder encoder;

    private final UserRepository userRepository;

    private final UserContactsRepository userContactsRepository;


    @Override
    public List<User> findAll() {
        return userRepository.findAll();
    }

    /**
     * Создание пользователя
     *
     * @return созданный пользователь
     */
    @Override
    public Optional<User> add(User user) {
        String email = user.getUserContacts().getEmail();
        String phone = user.getUserContacts().getPhone();
        if (userRepository.existsByUserLastName(user.getUserLastName())
                && userRepository.existsByUserName(user.getUsername())
                && userRepository.existsByUserMiddleName(user.getUserMiddleName())
        ) {
            throw new RuntimeException("Пользователь с таким именем, фамилией и отчеством, уже существует");
        }
        if (email != null && (userContactsRepository.existsByEmail(email))) {
            throw new RuntimeException("Пользователь с таким email уже существует");
        }
        if (phone != null && (userContactsRepository.existsByPhone(phone))) {
            throw new RuntimeException("Пользователь с таким номером телефона уже существует");
        }
        user.setRoles(List.of(ROLE_USER));
        return Optional.ofNullable(save(user));
    }

    @Override
    public boolean update(User user) {
        userRepository.save(user);
        return userRepository.findById(user.getId()).isPresent();

    }

    @Override
    public Optional<User> findById(long id) {
        return userRepository.findById(id);
    }

    @Override
    public boolean delete(User user) {
        userRepository.delete(user);
        return userRepository.findById(user.getId()).isEmpty();
    }

    @Override
    public boolean updatePatch(UserDto userDto) {
        var person = userRepository.findById(userDto.getId());
        if (person.isPresent()) {
            User result = person.get();
            result.setPassword(encoder.encode(userDto.getPassword()));
            this.add(result);
            return true;
        }
        return false;
    }

    /**
     * Получение пользователя по имени пользователя
     *
     * @return пользователь
     */
    @Override
    public User findUserByUserName(String username) {
        return userRepository.findUserByUserName(username)
                .orElseThrow(() -> new UsernameNotFoundException("Пользователь не найден"));
    }

    /**
     * Сохранение пользователя
     *
     * @return сохраненный пользователь
     */
    @Override
    public User save(User user) {
        return userRepository.save(user);
    }

    @Override
    public List<User> findUserByUserNameContains(String username) {
        return userRepository.findAllUsersByUserNameContaining(username);
    }

    @Override
    public Optional<User> setRoleAdmin(long id) {
        Optional<User> user = findById(id);
        if (user.isPresent() && user.get().getRoles().contains(Role.ROLE_USER)
                && !(user.get().getRoles().contains(Role.ROLE_ADMIN))) {
            List<Role> roles = user.get().getRoles();
            roles.add(Role.ROLE_ADMIN);
            user.get().setRoles(roles);
            return user;
        }
        return Optional.empty();
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findUserByUserName(username).orElseThrow();
        if (user == null) {
            throw new UsernameNotFoundException(username);
        }
        return new org.springframework.security.core.userdetails.User(
                user.getUsername(), user.getPassword(), emptyList());
    }

    /**
     * Получение пользователя по имени пользователя
     * <p>
     * Нужен для Spring Security
     *
     * @return пользователь
     */
    public UserDetailsService userDetailsService() {
        return this::findUserByUserName;
    }

    /**
     * Получение текущего пользователя
     *
     * @return текущий пользователь
     */
    @Override
    public User getCurrentUser() {
        /* Получение имени пользователя из контекста Spring Security*/
        return findUserByUserName(SecurityContextHolder.
                getContext().getAuthentication().getName());
    }

    /**
     * Выдача прав администратора текущему пользователю
     * <p>
     * Нужен для демонстрации
     */
    @Deprecated
    public void getAdmin() {
        var user = getCurrentUser();
        user.setRoles(List.of(ROLE_ADMIN));
        save(user);
    }

}
