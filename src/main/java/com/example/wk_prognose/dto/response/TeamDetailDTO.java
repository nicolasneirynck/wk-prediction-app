package com.example.wk_prognose.dto.response;

import java.util.List;

public record TeamDetailDTO(Long id, String name, String ownerName,
                            List<TeamMemberDTO> members, String inviteCode) {
}
