package cn.elegent.ac.core;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class SendBody{

    private Object data;

    private String chainID;

    public SendBody(Object data, String chainID) {
        this.data = data;
        this.chainID = chainID;
    }

}