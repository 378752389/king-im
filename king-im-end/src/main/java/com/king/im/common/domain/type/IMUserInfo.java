package com.king.im.common.domain.type;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class IMUserInfo {

    private Long id;

    private Integer terminalType;
}
