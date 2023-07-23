package kaphein.template.cmstemplatebatis.auth;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * 회원 비밀번호 암호화 및 비밀번호 검사를 위한 {@link PasswordEncoder} 인스턴스의 bean을 등록한다.
 */
@Configuration
public class PasswordEncoderConfig
{
    @Bean
    public PasswordEncoder passwordEncoder()
    {
        return new BCryptPasswordEncoder();
    }
}
