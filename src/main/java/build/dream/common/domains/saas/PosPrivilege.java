package build.dream.common.domains.saas;

import build.dream.common.basic.BasicDomain;
import build.dream.common.constants.Constants;

import java.math.BigInteger;

public class PosPrivilege extends BasicDomain {
    public static final String TABLE_NAME = "pos_privilege";
    /**
     * 权限编码
     */
    private String privilegeCode;
    /**
     * 权限名称
     */
    private String privilegeName;
    /**
     * 服务名称
     */
    private String serviceName = Constants.VARCHAR_DEFAULT_VALUE;
    /**
     * controller name
     */
    private String controllerName = Constants.VARCHAR_DEFAULT_VALUE;
    /**
     * action name
     */
    private String actionName = Constants.VARCHAR_DEFAULT_VALUE;
    /**
     * 父级权限ID
     */
    private BigInteger parentId;
    /**
     * 备注
     */
    private String remark = Constants.VARCHAR_DEFAULT_VALUE;

    public String getPrivilegeCode() {
        return privilegeCode;
    }

    public void setPrivilegeCode(String privilegeCode) {
        this.privilegeCode = privilegeCode;
    }

    public String getPrivilegeName() {
        return privilegeName;
    }

    public void setPrivilegeName(String privilegeName) {
        this.privilegeName = privilegeName;
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public String getControllerName() {
        return controllerName;
    }

    public void setControllerName(String controllerName) {
        this.controllerName = controllerName;
    }

    public String getActionName() {
        return actionName;
    }

    public void setActionName(String actionName) {
        this.actionName = actionName;
    }

    public BigInteger getParentId() {
        return parentId;
    }

    public void setParentId(BigInteger parentId) {
        this.parentId = parentId;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public static class Builder extends BasicDomain.Builder<Builder, PosPrivilege> {
        public Builder privilegeCode(String privilegeCode) {
            instance.setPrivilegeCode(privilegeCode);
            return this;
        }

        public Builder privilegeName(String privilegeName) {
            instance.setPrivilegeName(privilegeName);
            return this;
        }

        public Builder serviceName(String serviceName) {
            instance.setServiceName(serviceName);
            return this;
        }

        public Builder controllerName(String controllerName) {
            instance.setControllerName(controllerName);
            return this;
        }

        public Builder actionName(String actionName) {
            instance.setActionName(actionName);
            return this;
        }

        public Builder parentId(BigInteger parentId) {
            instance.setParentId(parentId);
            return this;
        }

        public Builder remark(String remark) {
            instance.setRemark(remark);
            return this;
        }

        @Override
        public PosPrivilege build() {
            PosPrivilege posPrivilege = super.build();
            posPrivilege.setPrivilegeCode(instance.getPrivilegeCode());
            posPrivilege.setPrivilegeName(instance.getPrivilegeName());
            posPrivilege.setServiceName(instance.getServiceName());
            posPrivilege.setControllerName(instance.getControllerName());
            posPrivilege.setActionName(instance.getActionName());
            posPrivilege.setParentId(instance.getParentId());
            posPrivilege.setRemark(instance.getRemark());
            return posPrivilege;
        }
    }

    public static Builder builder() {
        return new Builder();
    }

    public static final class ColumnName extends BasicDomain.ColumnName {
        public static final String PRIVILEGE_CODE = "privilege_code";
        public static final String PRIVILEGE_NAME = "privilege_name";
        public static final String SERVICE_NAME = "service_name";
        public static final String CONTROLLER_NAME = "controller_name";
        public static final String ACTION_NAME = "action_name";
        public static final String PARENT_ID = "parent_id";
        public static final String REMARK = "remark";
    }

    public static final class FieldName extends BasicDomain.FieldName {
        public static final String PRIVILEGE_CODE = "privilegeCode";
        public static final String PRIVILEGE_NAME = "privilegeName";
        public static final String SERVICE_NAME = "serviceName";
        public static final String CONTROLLER_NAME = "controllerName";
        public static final String ACTION_NAME = "actionName";
        public static final String PARENT_ID = "parentId";
        public static final String REMARK = "remark";
    }
}