package ru.otus.pw.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import ru.otus.pw.services.EnteraUserDetailsService;

/**
 * Конфигурирование SS.
 */
@Configuration
@RequiredArgsConstructor
public class SecurityConfig {
    //region Fields

    /**
     * Сервис для работы с пользователями в SS.
     */
    private final EnteraUserDetailsService enteraUserDetailsService;

    //endregion
    //region Public

    /**
     * Бин шифрования для пароля.
     *
     * @return Сервис для шифрования пароля.
     */
    @Bean
    public PasswordEncoder passwordEncoder() {

        return new BCryptPasswordEncoder();
    }

    /**
     * Бин провайдера аутентификатора.
     *
     * @return Провайдер аутентификатора.
     */
    @Bean
    public AuthenticationProvider authenticationProvider() {

        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
        authenticationProvider.setUserDetailsService(enteraUserDetailsService);
        authenticationProvider.setPasswordEncoder(passwordEncoder());

        return authenticationProvider;
    }

    /**
     * Бин менеджера аутентификауией.
     *
     * @param config Конфигурация аутентификации.
     *
     * @return Менеджер аутентификауией.
     */
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {

        return config.getAuthenticationManager();
    }

    //endregion
}
