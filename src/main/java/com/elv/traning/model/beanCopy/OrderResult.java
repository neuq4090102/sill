package com.elv.traning.model.beanCopy;

import com.elv.core.annotation.desensitization.Blur;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * @author lxh
 * @date 2020-04-16
 */
@Data
@NoArgsConstructor
public class OrderResult implements Serializable {

    private static final long serialVersionUID = 2323027276494000422L;

    private String channelOrderNo;
    private long channelPrice;
    private String orderNo;

    @Blur(fromIdx = 1, ratio = 1.0D, mask = "#")
    private String contacter;

    @Blur(fromIdx = 6, setpSize = 3, mask = "#")
    private String mobile;

    @Blur(fromIdx = 1, setpSize = 3)
    private long invoicePrice;
    private List<OrderRoomResult> orderRoomEntities;

}
