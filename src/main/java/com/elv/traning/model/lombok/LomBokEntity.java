package com.elv.traning.model.lombok;

import com.elv.core.constant.BooleanEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.Setter;

import java.util.List;

/**
 * @author lxh
 * @date 2020-04-16
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LomBokEntity {

    private long id;
    @NonNull
    private String name;
    private boolean man;
    private BooleanEnum adultEnum;
    private List<Hobby> hobbies;
    private String remark;
}

class Hobby {

    @Getter
    @Setter
    private String name;

    @Getter
    @Setter
    @NonNull

    private String desc;

}

