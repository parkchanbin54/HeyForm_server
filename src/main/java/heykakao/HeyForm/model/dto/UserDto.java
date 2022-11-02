package heykakao.HeyForm.model.dto;

import heykakao.HeyForm.model.Survey;
import heykakao.HeyForm.model.User;
import lombok.*;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class UserDto {
    private String token;

    public UserDto(User user) {
        this.token = user.getToken();
    }
}
