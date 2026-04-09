package site.zqiusu.domain.po;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserAccount {
    private Long id;
    private String phone;
    private String nickname;
    private String status;
    private Date createTime;
    private Date updateTime;
}
