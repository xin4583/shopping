package com.a.shopping.configure;

import lombok.Data;

import java.util.HashMap;

//分页
@Data
public class QueryPageParam {
    private static int PAGE_SIZE=10;
    private static int PAGE_NUM=1;
    private int pageSize=PAGE_SIZE;
    private int pageNum=PAGE_NUM;
    private HashMap param;
}
