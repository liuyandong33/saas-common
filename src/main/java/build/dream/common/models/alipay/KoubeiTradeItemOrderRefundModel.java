package build.dream.common.models.alipay;

import build.dream.common.models.BasicModel;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.List;

public class KoubeiTradeItemOrderRefundModel extends AlipayBasicModel {
    @NotNull
    @Length(max = 64)
    @JsonProperty(value = "order_no")
    private String orderNo;

    @NotNull
    @Length(max = 64)
    @JsonProperty(value = "out_request_no")
    private String outRequestNo;

    @Length(max = 256)
    private String reason;

    @NotEmpty
    @JsonProperty(value = "refund_infos")
    private List<RefundInfo> refundInfos;

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public String getOutRequestNo() {
        return outRequestNo;
    }

    public void setOutRequestNo(String outRequestNo) {
        this.outRequestNo = outRequestNo;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public List<RefundInfo> getRefundInfos() {
        return refundInfos;
    }

    public void setRefundInfos(List<RefundInfo> refundInfos) {
        this.refundInfos = refundInfos;
    }

    public static class Builder {
        private final KoubeiTradeItemOrderRefundModel instance = new KoubeiTradeItemOrderRefundModel();

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

        public Builder orderNo(String orderNo) {
            instance.setOrderNo(orderNo);
            return this;
        }

        public Builder outRequestNo(String outRequestNo) {
            instance.setOutRequestNo(outRequestNo);
            return this;
        }

        public Builder reason(String reason) {
            instance.setReason(reason);
            return this;
        }

        public Builder refundInfos(List<RefundInfo> refundInfos) {
            instance.setRefundInfos(refundInfos);
            return this;
        }

        public KoubeiTradeItemOrderRefundModel build() {
            KoubeiTradeItemOrderRefundModel koubeiTradeItemOrderRefundModel = new KoubeiTradeItemOrderRefundModel();
            koubeiTradeItemOrderRefundModel.setTenantId(instance.getTenantId());
            koubeiTradeItemOrderRefundModel.setBranchId(instance.getBranchId());
            koubeiTradeItemOrderRefundModel.setReturnUrl(instance.getReturnUrl());
            koubeiTradeItemOrderRefundModel.setNotifyUrl(instance.getNotifyUrl());
            koubeiTradeItemOrderRefundModel.setAuthToken(instance.getAuthToken());
            koubeiTradeItemOrderRefundModel.setOrderNo(instance.getOrderNo());
            koubeiTradeItemOrderRefundModel.setOutRequestNo(instance.getOutRequestNo());
            koubeiTradeItemOrderRefundModel.setReason(instance.getReason());
            koubeiTradeItemOrderRefundModel.setRefundInfos(instance.getRefundInfos());
            return koubeiTradeItemOrderRefundModel;
        }
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class RefundInfo extends BasicModel {
        @NotNull
        @Length(max = 32)
        @JsonProperty(value = "item_order_no")
        private String itemOrderNo;

        @NotNull
        @DecimalMin(value = "0.01")
        @DecimalMax(value = "999999999")
        private BigDecimal amount;

        public String getItemOrderNo() {
            return itemOrderNo;
        }

        public void setItemOrderNo(String itemOrderNo) {
            this.itemOrderNo = itemOrderNo;
        }

        public BigDecimal getAmount() {
            return amount;
        }

        public void setAmount(BigDecimal amount) {
            this.amount = amount;
        }
    }
}
