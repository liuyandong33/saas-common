package build.dream.common.utils;

import build.dream.common.constants.Constants;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PagedSearchModel {
    private List<SearchCondition> searchConditions = new ArrayList<SearchCondition>();
    private String whereClause;
    private Map<String, Object> namedParameters = new HashMap<String, Object>();
    private String groupBy;
    private String orderBy;
    private Integer page;
    private Integer rows;

    public PagedSearchModel() {

    }

    public PagedSearchModel(boolean autoSetDeletedFalse) {
        if (autoSetDeletedFalse) {
            this.addSearchCondition("deleted", Constants.SQL_OPERATION_SYMBOL_EQUAL, 0);
        }
    }

    public PagedSearchModel(List<SearchCondition> searchConditions, Integer page, Integer rows) {
        this.searchConditions = searchConditions;
        this.page = page;
        this.rows = rows;
    }

    public List<SearchCondition> getSearchConditions() {
        return searchConditions;
    }

    public void setSearchConditions(List<SearchCondition> searchConditions) {
        this.searchConditions = searchConditions;
    }

    public String getWhereClause() {
        return whereClause;
    }

    public void setWhereClause(String whereClause) {
        this.whereClause = whereClause;
    }

    public Map<String, Object> getNamedParameters() {
        return namedParameters;
    }

    public void setNamedParameters(Map<String, Object> namedParameters) {
        this.namedParameters = namedParameters;
    }

    public void addNamedParameter(String name, Object value) {
        this.namedParameters.put(name, value);
    }

    public void addSearchCondition(String columnName, String operationSymbol, Object searchParameter) {
        searchConditions.add(new SearchCondition(columnName, operationSymbol, searchParameter));
    }

    public String getGroupBy() {
        return groupBy;
    }

    public void setGroupBy(String groupBy) {
        this.groupBy = groupBy;
    }

    public String getOrderBy() {
        return orderBy;
    }

    public void setOrderBy(String orderBy) {
        this.orderBy = orderBy;
    }

    public Integer getPage() {
        return page;
    }

    public void setPage(Integer page) {
        this.page = page;
    }

    public Integer getRows() {
        return rows;
    }

    public void setRows(Integer rows) {
        this.rows = rows;
    }

    public Integer getOffset() {
        return (page - 1) * rows;
    }

    public Integer getMaxResults() {
        return rows;
    }

    /**
     * 获取开始行号，oracle数据库专用
     *
     * @return
     */
    public Integer getStartRowNumber() {
        return (page - 1) * rows;
    }

    /**
     * 获取结束行号，oracle数据库专用
     *
     * @return
     */
    public Integer getEndRowNumber() {
        return page * rows;
    }

    public static class Builder {
        private final PagedSearchModel instance = new PagedSearchModel();

        public Builder autoSetDeletedFalse() {
            instance.addSearchCondition("deleted", Constants.SQL_OPERATION_SYMBOL_EQUAL, 0);
            return this;
        }

        public Builder searchConditions(List<SearchCondition> searchConditions) {
            instance.setSearchConditions(searchConditions);
            return this;
        }

        public Builder addSearchCondition(String columnName, String operationSymbol, Object searchParameter) {
            instance.searchConditions.add(new SearchCondition(columnName, operationSymbol, searchParameter));
            return this;
        }

        public Builder whereClause(String whereClause) {
            instance.setWhereClause(whereClause);
            return this;
        }

        public Builder namedParameters(Map<String, Object> namedParameters) {
            instance.setNamedParameters(namedParameters);
            return this;
        }

        public Builder addNamedParameter(String name, Object value) {
            instance.namedParameters.put(name, value);
            return this;
        }

        public Builder groupBy(String groupBy) {
            instance.setGroupBy(groupBy);
            return this;
        }

        public Builder orderBy(String orderBy) {
            instance.setOrderBy(orderBy);
            return this;
        }

        public Builder page(Integer page) {
            instance.setPage(page);
            return this;
        }

        public Builder rows(Integer rows) {
            instance.setRows(rows);
            return this;
        }

        public PagedSearchModel build() {
            PagedSearchModel pagedSearchModel = new PagedSearchModel();
            pagedSearchModel.setSearchConditions(instance.getSearchConditions());
            pagedSearchModel.setWhereClause(instance.getWhereClause());
            pagedSearchModel.setNamedParameters(instance.getNamedParameters());
            pagedSearchModel.setGroupBy(instance.getGroupBy());
            pagedSearchModel.setOrderBy(instance.getOrderBy());
            pagedSearchModel.setPage(instance.getPage());
            pagedSearchModel.setRows(instance.getRows());
            return pagedSearchModel;
        }
    }

    public static Builder builder() {
        return new Builder();
    }
}
