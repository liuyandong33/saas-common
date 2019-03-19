package build.dream.common.models.alipay;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

public class AlipayOpenAgentOfflinePaymentSignModel extends AlipayBasicModel {
    @NotNull
    @Length(max = 25)
    @JsonProperty(value = "batch_no")
    private String batchNo;

    @NotNull
    @Length(max = 15)
    @JsonProperty(value = "mcc_code")
    private String mccCode;

    @NotNull
    @DecimalMin(value = "0.38")
    @DecimalMax(value = "3")
    private BigDecimal rate;

    @Length(max = 32)
    @JsonProperty(value = "business_license_no")
    private String businessLicenseNo;

    @JsonProperty(value = "long_term")
    private Boolean longTerm;

    @Length(min = 10, max = 10)
    @JsonProperty(value = "date_limitation")
    private String dateLimitation;

    public String getBatchNo() {
        return batchNo;
    }

    public void setBatchNo(String batchNo) {
        this.batchNo = batchNo;
    }

    public BigDecimal getRate() {
        return rate;
    }

    public void setRate(BigDecimal rate) {
        this.rate = rate;
    }

    public String getBusinessLicenseNo() {
        return businessLicenseNo;
    }

    public void setBusinessLicenseNo(String businessLicenseNo) {
        this.businessLicenseNo = businessLicenseNo;
    }

    public Boolean getLongTerm() {
        return longTerm;
    }

    public void setLongTerm(Boolean longTerm) {
        this.longTerm = longTerm;
    }

    public String getDateLimitation() {
        return dateLimitation;
    }

    public void setDateLimitation(String dateLimitation) {
        this.dateLimitation = dateLimitation;
    }

    public static class Builder {
        private final AlipayOpenAgentOfflinePaymentSignModel instance = new AlipayOpenAgentOfflinePaymentSignModel();

        public Builder tenantId(String tenantId) {
            instance.setTenantId(tenantId);
            return this;
        }

        public Builder branchId(String branchId) {
            instance.setBranchId(branchId);
            return this;
        }

        public Builder returnUrl(String returnUrl) {
            instance.setReturnUrl(returnUrl);
            return this;
        }

        public Builder notifyUrl(String notifyUrl) {
            instance.setNotifyUrl(notifyUrl);
            return this;
        }

        public Builder authToken(String authToken) {
            instance.setAuthToken(authToken);
            return this;
        }

        public Builder batchNo(String batchNo) {
            instance.setBatchNo(batchNo);
            return this;
        }

        public Builder rate(BigDecimal rate) {
            instance.setRate(rate);
            return this;
        }

        public Builder businessLicenseNo(String businessLicenseNo) {
            instance.setBusinessLicenseNo(businessLicenseNo);
            return this;
        }

        public Builder longTerm(Boolean longTerm) {
            instance.setLongTerm(longTerm);
            return this;
        }

        public Builder dateLimitation(String dateLimitation) {
            instance.setDateLimitation(dateLimitation);
            return this;
        }

        public AlipayOpenAgentOfflinePaymentSignModel build() {
            AlipayOpenAgentOfflinePaymentSignModel alipayOpenAgentOfflinePaymentSignModel = new AlipayOpenAgentOfflinePaymentSignModel();
            alipayOpenAgentOfflinePaymentSignModel.setTenantId(instance.getTenantId());
            alipayOpenAgentOfflinePaymentSignModel.setBranchId(instance.getBranchId());
            alipayOpenAgentOfflinePaymentSignModel.setReturnUrl(instance.getReturnUrl());
            alipayOpenAgentOfflinePaymentSignModel.setNotifyUrl(instance.getNotifyUrl());
            alipayOpenAgentOfflinePaymentSignModel.setAuthToken(instance.getAuthToken());
            alipayOpenAgentOfflinePaymentSignModel.setBatchNo(instance.getBatchNo());
            alipayOpenAgentOfflinePaymentSignModel.setRate(instance.getRate());
            alipayOpenAgentOfflinePaymentSignModel.setBusinessLicenseNo(instance.getBusinessLicenseNo());
            alipayOpenAgentOfflinePaymentSignModel.setLongTerm(instance.getLongTerm());
            alipayOpenAgentOfflinePaymentSignModel.setDateLimitation(instance.getDateLimitation());
            return alipayOpenAgentOfflinePaymentSignModel;
        }
    }

    public static Builder builder() {
        return new Builder();
    }
}