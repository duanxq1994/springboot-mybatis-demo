package com.xinge.demo.core.entity;

import lombok.Data;

import java.util.List;

/**
 * 批量返回DTO
 *
 * @author wgyi
 */
@Data
public class BatchResultDTO<T> {

    private List<T> module;

    private Long count;

}
