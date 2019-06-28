package com.xinge.demo.common.util;

import com.github.dozermapper.core.Mapper;
import com.github.pagehelper.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author duanxq
 * @date 2019/2/23
 */
@Component
public class DozerUtil {

    @Autowired
    protected Mapper dozerMapper;

    public <T, S> T convert(S s, Class<T> clz) {
        if (s == null) {
            return null;
        }
        return this.dozerMapper.map(s, clz);
    }

    public <T, S> List<T> convert(List<S> s, Class<T> clz) {
        if (s == null) {
            return null;
        }
        List<T> list;
        if (s instanceof Page) {
            list = new Page<>();
            List<S> result = ((Page<S>) s).getResult();
            for (S s1 : result) {
                ((Page<T>) list).getResult().add(this.dozerMapper.map(s1, clz));
            }
            long total = ((Page<S>) s).getTotal();
            ((Page<T>) list).setTotal(total);
        } else {
            list = new ArrayList<>();
            for (S vs : s) {
                list.add(this.dozerMapper.map(vs, clz));
            }
        }
        return list;
    }

    public <T, S> Set<T> convert(Set<S> s, Class<T> clz) {
        if (s == null) {
            return null;
        }
        Set<T> set = new HashSet<>();
        for (S vs : s) {
            set.add(this.dozerMapper.map(vs, clz));
        }
        return set;
    }

    public <T, S> T[] convert(S[] s, Class<T> clz) {
        if (s == null) {
            return null;
        }
        @SuppressWarnings("unchecked")
        T[] arr = (T[]) Array.newInstance(clz, s.length);
        for (int i = 0; i < s.length; i++) {
            arr[i] = this.dozerMapper.map(s[i], clz);
        }
        return arr;
    }

}
