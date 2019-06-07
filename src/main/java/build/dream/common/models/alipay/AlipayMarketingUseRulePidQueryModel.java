package build.dream.common.models.alipay;

import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;

public class AlipayMarketingUseRulePidQueryModel extends AlipayBasicModel {
    @NotNull
    @Length(max = 16)
    private String pid;

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    public static class Builder extends AlipayBasicModel.Builder<Builder, AlipayMarketingUseRulePidQueryModel> {
        public Builder pid(String pid) {
            instance.setPid(pid);
            return this;
        }

        @Override
        public AlipayMarketingUseRulePidQueryModel build() {
            AlipayMarketingUseRulePidQueryModel alipayMarketingUseRulePidQueryModel = super.build();
            alipayMarketingUseRulePidQueryModel.setPid(instance.getPid());
            return alipayMarketingUseRulePidQueryModel;
        }
    }

    public static Builder builder() {
        return new Builder();
    }
}
