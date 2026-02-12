package fun.hades.dto.response;


import lombok.Data;

import java.time.LocalDateTime;

@Data
public class SysProjectDTO {

    // 项目原有字段
    private Long id;
    private String projectName;
    private String projectCode;
    private Long ownerUserId; // 负责人ID（保留，供编辑用）
    private String ownerUserName; // 负责人用户名（新增，供展示用）
    private String memberUserIds;
    private String memberUserNames;
    private Integer status;
    private String description;
    private LocalDateTime createTime;
}
