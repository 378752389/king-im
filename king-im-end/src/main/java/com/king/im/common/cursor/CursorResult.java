package com.king.im.common.cursor;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CursorResult<T> {

    private Long cursor;

    private Integer size;

    private Boolean isLast;

    private List<T> data;
}
