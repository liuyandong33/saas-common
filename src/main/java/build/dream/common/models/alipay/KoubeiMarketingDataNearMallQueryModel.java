package build.dream.common.models.alipay;

public class KoubeiMarketingDataNearMallQueryModel extends AlipayBasicModel {
    public static class Builder {
        private final KoubeiMarketingDataNearMallQueryModel instance = new KoubeiMarketingDataNearMallQueryModel();

        public Builder tenantId(String tenantId) {
            instance.setTenantId(tenantId);
            return this;
        }

        public Builder branchId(String branchId) {
            instance.setBranchId(branchId);
            return this;
        }

        public KoubeiMarketingDataNearMallQueryModel build() {
            KoubeiMarketingDataNearMallQueryModel koubeiMarketingDataNearMallQueryModel = new KoubeiMarketingDataNearMallQueryModel();
            koubeiMarketingDataNearMallQueryModel.setTenantId(instance.getTenantId());
            koubeiMarketingDataNearMallQueryModel.setBranchId(instance.getBranchId());
            return koubeiMarketingDataNearMallQueryModel;
        }
    }

    public static Builder builder() {
        return new Builder();
    }
}
