package com.elv.traning.model.jackson;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author lxh
 * @date 2020-04-17
 */

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties({ "extra", "uselessValue" })
public class JsonEntity implements Comparable {

    @JsonProperty("firstName")
    private String _first_name = "Bob";

    private int value = 666;
    @JsonIgnore
    private int internalValue = 888;

    private String extra = "fluffy";
    private int uselessValue = -13;

    @Override
    public int compareTo(Object o) {
        return this.getValue() - ((JsonEntity) o).getValue();
    }
}
