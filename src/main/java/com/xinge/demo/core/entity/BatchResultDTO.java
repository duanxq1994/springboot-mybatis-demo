package com.xinge.demo.core.entity;

import com.github.pagehelper.PageSerializable;

import java.util.List;

/**
 * 批量返回DTO
 *
 * @author wgyi
 */

public class BatchResultDTO<T> extends PageSerializable<T> {

    private static final long serialVersionUID = 1420295871173553373L;

    public BatchResultDTO() {

    }

    public BatchResultDTO(List<T> list) {
        super(list);
    }

    public static <T> BatchResultDTO<T> of(List<T> list){
        return new BatchResultDTO<T>(list);
    }

    @Override
    public String toString() {
        return "BatchResultDTO{" +
                "total=" + total +
                ", list=" + list +
                '}';
    }
}
