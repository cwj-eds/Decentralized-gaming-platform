package com.decentralized.gaming.platform.common;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 分页结果
 *
 * @author DecentralizedGamingPlatform
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PageResult<T> {
    
    private List<T> records;
    private Long total;
    private Long current;
    private Long size;
    private Long pages;
    
    public static <T> PageResult<T> of(List<T> records, Long total, Long current, Long size) {
        PageResult<T> pageResult = new PageResult<>();
        pageResult.setRecords(records);
        pageResult.setTotal(total);
        pageResult.setCurrent(current);
        pageResult.setSize(size);
        pageResult.setPages((total + size - 1) / size);
        return pageResult;
    }
}
