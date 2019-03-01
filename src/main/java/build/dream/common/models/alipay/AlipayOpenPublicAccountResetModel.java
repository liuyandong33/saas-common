package build.dream.common.models.alipay;

import build.dream.common.models.BasicModel;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;

public class AlipayOpenPublicAccountResetModel extends BasicModel {
    @NotNull
    @JsonIgnore
    private String tenantId;

    @NotNull
    @JsonIgnore
    private String branchId;

    @NotNull
    @Length(max = 64)
    @JsonProperty(value = "bind_account_no")
    private String bindAccountNo;

    @NotNull
    @Length(max = 10)
    @JsonProperty(value = "display_name")
    private String displayName;

    @NotNull
    @Length(max = 32)
    @JsonProperty(value = "agreement_id")
    private String agreementId;

    @Length(max = 10)
    @JsonProperty(value = "real_name")
    private String realName;

    @NotNull
    @Length(max = 32)
    @JsonProperty(value = "from_user_id")
    private String fromUserId;

    @Length(max = 200)
    private String remark;

    public String getTenantId() {
        return tenantId;
    }

    public void setTenantId(String tenantId) {
        this.tenantId = tenantId;
    }

    public String getBranchId() {
        return branchId;
    }

    public void setBranchId(String branchId) {
        this.branchId = branchId;
    }

    public String getBindAccountNo() {
        return bindAccountNo;
    }

    public void setBindAccountNo(String bindAccountNo) {
        this.bindAccountNo = bindAccountNo;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getAgreementId() {
        return agreementId;
    }

    public void setAgreementId(String agreementId) {
        this.agreementId = agreementId;
    }

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public String getFromUserId() {
        return fromUserId;
    }

    public void setFromUserId(String fromUserId) {
        this.fromUserId = fromUserId;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }


    public static class Builder {
        private final AlipayOpenPublicAccountResetModel instance = new AlipayOpenPublicAccountResetModel();

        public Builder tenantId(String tenantId) {
            instance.setTenantId(tenantId);
            return this;
        }

        public Builder branchId(String branchId) {
            instance.setBranchId(branchId);
            return this;
        }

        public Builder bindAccountNo(String bindAccountNo) {
            instance.setBindAccountNo(bindAccountNo);
            return this;
        }

        public Builder displayName(String displayName) {
            instance.setDisplayName(displayName);
            return this;
        }

        public Builder agreementId(String agreementId) {
            instance.setAgreementId(agreementId);
            return this;
        }

        public Builder realName(String realName) {
            instance.setRealName(realName);
            return this;
        }

        public Builder fromUserId(String fromUserId) {
            instance.setFromUserId(fromUserId);
            return this;
        }

        public Builder remark(String remark) {
            instance.setRemark(remark);
            return this;
        }

        public AlipayOpenPublicAccountResetModel build() {
            AlipayOpenPublicAccountResetModel alipayOpenPublicAccountResetModel = new AlipayOpenPublicAccountResetModel();
            alipayOpenPublicAccountResetModel.setTenantId(instance.getTenantId());
            alipayOpenPublicAccountResetModel.setBranchId(instance.getBranchId());
            alipayOpenPublicAccountResetModel.setBindAccountNo(instance.getBindAccountNo());
            alipayOpenPublicAccountResetModel.setDisplayName(instance.getDisplayName());
            alipayOpenPublicAccountResetModel.setAgreementId(instance.getAgreementId());
            alipayOpenPublicAccountResetModel.setRealName(instance.getRealName());
            alipayOpenPublicAccountResetModel.setFromUserId(instance.getFromUserId());
            alipayOpenPublicAccountResetModel.setRemark(instance.getRemark());
            return alipayOpenPublicAccountResetModel;
        }
    }

    public static Builder builder() {
        return new Builder();
    }
}