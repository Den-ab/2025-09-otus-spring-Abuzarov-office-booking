package ru.otus.pw.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.otus.pw.models.EnteraUser;
import ru.otus.pw.repositories.EnteraUserRepository;

import java.util.List;

@RequiredArgsConstructor
@Service
public class UserService {

    private final EnteraUserRepository userRepository;

    public List<EnteraUser> findAll() {

        return this.userRepository.findAll();
    }
}
