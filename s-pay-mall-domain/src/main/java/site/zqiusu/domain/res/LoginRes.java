package site.zqiusu.domain.res;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LoginRes {
    private String token;
    private String userId;
    private String nickname;
    private Boolean isNewUser;
}
