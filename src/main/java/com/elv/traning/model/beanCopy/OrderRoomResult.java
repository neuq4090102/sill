package com.elv.traning.model.beanCopy;

import lombok.Data;

import java.io.Serializable;

/**
 * @author lxh
 * @date 2020-04-16
 */
@Data
public class OrderRoomResult implements Serializable {

    private static final long serialVersionUID = 6489994932604273643L;

    private String orderRoomNo;
    private boolean halfDay;
}
