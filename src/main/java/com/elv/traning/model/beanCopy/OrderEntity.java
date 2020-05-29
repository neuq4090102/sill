package com.elv.traning.model.beanCopy;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author lxh
 * @date 2020-04-16
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OrderEntity extends ChannelOrderEntity {
    private String orderNo;
    private String contacter;
    private String mobile;
    private long invoicePrice;
    private List<OrderRoomEntity> orderRoomEntities;
}
