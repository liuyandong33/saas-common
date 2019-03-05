package build.dream.common.models.alipay;

import build.dream.common.models.BasicModel;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;

public class AlipayOpenAgentCreateModel extends BasicModel {
    @NotNull
    @JsonIgnore
    private String tenantId;

    @NotNull
    @JsonIgnore
    private String branchId;

    @NotNull
    @JsonProperty(value = "contact_info")
    private ContactInfo contactInfo;

    @Length(max = 40)
    @JsonProperty(value = "order_ticket")
    private String orderTicket;

    public String getTenantId() {
        return tenantId;
    }

    public void setTenantId(String tenantId) {
        this.tenantId = tenantId;
    }

    public void setBranchId(String branchId) {
        this.branchId = branchId;
    }

    public String getBranchId() {
        return branchId;
    }

    public ContactInfo getContactInfo() {
        return contactInfo;
    }

    public void setContactInfo(ContactInfo contactInfo) {
        this.contactInfo = contactInfo;
    }

    public String getOrderTicket() {
        return orderTicket;
    }

    public void setOrderTicket(String orderTicket) {
        this.orderTicket = orderTicket;
    }

    public static class Builder {
        private final AlipayOpenAgentCreateModel instance = new AlipayOpenAgentCreateModel();

        public Builder tenantId(String tenantId) {
            instance.setTenantId(tenantId);
            return this;
        }

        public Builder branchId(String branchId) {
            instance.setBranchId(branchId);
            return this;
        }

        public Builder contactInfo(ContactInfo contactInfo) {
            instance.setContactInfo(contactInfo);
            return this;
        }

        public Builder orderTicket(String orderTicket) {
            instance.setOrderTicket(orderTicket);
            return this;
        }

        public AlipayOpenAgentCreateModel build() {
            AlipayOpenAgentCreateModel alipayOpenAgentCreateModel = new AlipayOpenAgentCreateModel();
            alipayOpenAgentCreateModel.setTenantId(instance.getTenantId());
            alipayOpenAgentCreateModel.setBranchId(instance.getBranchId());
            alipayOpenAgentCreateModel.setContactInfo(instance.getContactInfo());
            alipayOpenAgentCreateModel.setOrderTicket(instance.getOrderTicket());
            return alipayOpenAgentCreateModel;
        }
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class ContactInfo extends BasicModel {
        @NotNull
        @Length(max = 64)
        @JsonProperty(value = "contact_name")
        private String contactName;

        @NotNull
        @Length(max = 16)
        @JsonProperty(value = "contact_mobile")
        private String contactMobile;

        @NotNull
        @Length(max = 64)
        @JsonProperty(value = "contact_email")
        private String contactEmail;

        public String getContactName() {
            return contactName;
        }

        public void setContactName(String contactName) {
            this.contactName = contactName;
        }

        public String getContactMobile() {
            return contactMobile;
        }

        public void setContactMobile(String contactMobile) {
            this.contactMobile = contactMobile;
        }

        public String getContactEmail() {
            return contactEmail;
        }

        public void setContactEmail(String contactEmail) {
            this.contactEmail = contactEmail;
        }
    }
}
