package com.tyut.shopping_common.pojo;

import com.baomidou.mybatisplus.annotation.TableId;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.annotation.Nullable;
import java.io.Serializable;

/**
 * 规格项
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SpecificationOption implements Serializable {
    @TableId
    private Long id; // 规格项id
    private String optionName; // 规格项名
    private Long specId; // 对应的规格id
}