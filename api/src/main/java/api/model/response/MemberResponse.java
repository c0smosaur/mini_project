package api.model.response;

import db.enums.MemberType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MemberResponse {
    private Long memberId;
    private String username;
    private String name;
    private MemberType type;
    private String profileImage;
}
