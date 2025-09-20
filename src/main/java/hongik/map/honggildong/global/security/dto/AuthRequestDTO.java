package hongik.map.honggildong.global.security.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import org.hibernate.validator.constraints.Length;

public class AuthRequestDTO{


        @Getter
        public static class SignUp{
                @NotBlank(message = "이메일은 필수 입력 값입니다.")
                private String email;
                //@Length(min = 8, max = 20, message = "비밀번호는 8자 이상 20자 이하여야 합니다.")
                private String password;
                @NotBlank(message = "닉네임은 필수 입력 값입니다.")
                private String nickname;
                @NotBlank(message = "이름은 필수 입력 값입니다.")
                private String fullName;
        }

        @Getter
        public static class Login{
                @NotBlank
                private String email;
                @NotBlank
                private String password;
        }
}
