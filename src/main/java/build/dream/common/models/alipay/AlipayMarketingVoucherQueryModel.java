package build.dream.common.models.alipay;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;

public class AlipayMarketingVoucherQueryModel extends AlipayBasicModel {
    @NotNull
    @Length(max = 28)
    @JsonProperty(value = "voucher_id")
    private String voucherId;

    public String getVoucherId() {
        return voucherId;
    }

    public void setVoucherId(String voucherId) {
        this.voucherId = voucherId;
    }

    public static class Builder extends AlipayBasicModel.Builder<Builder, AlipayMarketingVoucherQueryModel> {
        public Builder voucherId(String voucherId) {
            instance.setVoucherId(voucherId);
            return this;
        }

        @Override
        public AlipayMarketingVoucherQueryModel build() {
            AlipayMarketingVoucherQueryModel alipayMarketingVoucherQueryModel = super.build();
            alipayMarketingVoucherQueryModel.setVoucherId(instance.getVoucherId());
            return alipayMarketingVoucherQueryModel;
        }
    }

    public static Builder builder() {
        return new Builder();
    }
}
