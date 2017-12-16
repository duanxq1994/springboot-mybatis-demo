package com.xinge.demo.model.entity;

import lombok.Data;

import java.util.List;

/**
 * 批量返回DTO
 *
 * @author wgyi
 * @version $Id: BatchResultDTO.java, v 0.1 2016年6月29日 下午3:45:29 wgyi Exp $
 */
@Data
public class BatchResultDTO<T> {

    private List<T> module;

    private Long count;

}
