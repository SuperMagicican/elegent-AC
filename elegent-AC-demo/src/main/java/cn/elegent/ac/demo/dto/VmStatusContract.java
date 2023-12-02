package cn.elegent.ac.demo.dto;

import lombok.Data;

import java.util.List;

/**
 * 售货机状态
 */
@Data
public class VmStatusContract {
    private String innerCode;
    private List<StatusInfo> statusInfo;
}