package build.dream.common.erp.catering.domains;

import java.math.BigInteger;

public class CanNotDeleteReason {
    /**
     * 商户ID
     */
    private BigInteger tenantId;
    /**
     * 商户编码
     */
    private String tenantCode;
    /**
     * 门店ID
     */
    private BigInteger branchId;
    /**
     * 表id
     */
    private BigInteger tableId;
    /**
     * 表名
     */
    private String tableName;
    /**
     * 导致不能删除的表id
     */
    private BigInteger causeTableId;
    /**
     * 导致不能删除的表名字
     */
    private String causeTableName;
    /**
     * 原因
     */
    private String reason;

    public BigInteger getTenantId() {
        return tenantId;
    }

    public void setTenantId(BigInteger tenantId) {
        this.tenantId = tenantId;
    }

    public String getTenantCode() {
        return tenantCode;
    }

    public void setTenantCode(String tenantCode) {
        this.tenantCode = tenantCode;
    }

    public BigInteger getBranchId() {
        return branchId;
    }

    public void setBranchId(BigInteger branchId) {
        this.branchId = branchId;
    }

    public BigInteger getTableId() {
        return tableId;
    }

    public void setTableId(BigInteger tableId) {
        this.tableId = tableId;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public BigInteger getCauseTableId() {
        return causeTableId;
    }

    public void setCauseTableId(BigInteger causeTableId) {
        this.causeTableId = causeTableId;
    }

    public String getCauseTableName() {
        return causeTableName;
    }

    public void setCauseTableName(String causeTableName) {
        this.causeTableName = causeTableName;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }
}
