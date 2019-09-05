package com.online.workflow.util;

public class PageUtil {

    public static String getPageSql(String sql, PageModel pageModel){
          
          
        int startIndexs = (pageModel.getPage() - 1) * pageModel.getRows() +1;
        int startIndex =startIndexs-1;
        int endIndex = pageModel.getPage() * pageModel.getRows();
        StringBuilder pageSql = new StringBuilder();
        pageSql.append(sql);
        pageSql.append("  limit "+startIndex+","+endIndex);
//        pageSql.append("select * from (");
//        pageSql.append("select tt.*,rownum as rowno from (");
//        pageSql.append(sql);
//        pageSql.append(") tt where rownum <= "+endIndex);
//        pageSql.append(") table_alias where table_alias.rowno>="+startIndex);
        return pageSql.toString(); 
    }
    
    public static String getCountSql(String sql){
        StringBuilder countSql = new StringBuilder();
        countSql.append("select count(*) from (" + sql +") a");
        return countSql.toString();
    }

}
