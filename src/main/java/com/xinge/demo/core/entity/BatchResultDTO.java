package com.xinge.demo.core.entity;

import com.github.pagehelper.Page;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * 批量返回DTO
 *
 * @author wgyi
 */
@Data
public class BatchResultDTO<T> {

    public BatchResultDTO() {
        setCount(0L);
        setModule(new ArrayList<T>());
    }

    public BatchResultDTO(List<T> list) {
        if (list instanceof Page) {
            setModule(((Page<T>) list).getResult());
            setCount(((Page<T>) list).getTotal());
        } else {
            throw new UnsupportedOperationException();
        }
    }

    public BatchResultDTO(List<T> list, long total) {
        setModule(list);
        setCount(total);
    }

    private List<T> module;

    private Long count;

}
