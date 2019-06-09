package build.dream.common.models.umpay;

import build.dream.common.constants.Constants;
import build.dream.common.constraints.InList;
import build.dream.common.models.BasicModel;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;

public class DownloadSettleFileModel extends BasicModel {
    @NotNull
    @InList(value = {Constants.RSA})
    private String signType = Constants.RSA;

    @NotNull
    @Length(max = 8)
    private String merId;

    @NotNull
    @Length(max = 3)
    private String version = Constants.UM_PAY_VERSION;

    /**
     * 对账日期	商户的对账报表和对账报表均以此日期为准，格式为YYYYMMDD
     */
    @NotNull
    @Length(min = 8, max = 8)
    private String settleDate;

    @NotNull
    private String merchantPrivateKey;

    @NotNull
    private String platformPublicKey;

    public String getSignType() {
        return signType;
    }

    public void setSignType(String signType) {
        this.signType = signType;
    }

    public String getMerId() {
        return merId;
    }

    public void setMerId(String merId) {
        this.merId = merId;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getSettleDate() {
        return settleDate;
    }

    public void setSettleDate(String settleDate) {
        this.settleDate = settleDate;
    }

    public String getMerchantPrivateKey() {
        return merchantPrivateKey;
    }

    public void setMerchantPrivateKey(String merchantPrivateKey) {
        this.merchantPrivateKey = merchantPrivateKey;
    }

    public String getPlatformPublicKey() {
        return platformPublicKey;
    }

    public void setPlatformPublicKey(String platformPublicKey) {
        this.platformPublicKey = platformPublicKey;
    }

    public static class Builder {
        private final DownloadSettleFileModel instance = new DownloadSettleFileModel();

        public Builder signType(String signType) {
            instance.setSignType(signType);
            return this;
        }

        public Builder merId(String merId) {
            instance.setMerId(merId);
            return this;
        }

        public Builder version(String version) {
            instance.setVersion(version);
            return this;
        }

        public Builder settleDate(String settleDate) {
            instance.setSettleDate(settleDate);
            return this;
        }

        public Builder merchantPrivateKey(String merchantPrivateKey) {
            instance.setMerchantPrivateKey(merchantPrivateKey);
            return this;
        }

        public Builder platformPublicKey(String platformPublicKey) {
            instance.setPlatformPublicKey(platformPublicKey);
            return this;
        }

        public DownloadSettleFileModel build() {
            DownloadSettleFileModel downloadSettleFileModel = new DownloadSettleFileModel();
            downloadSettleFileModel.setSignType(instance.getSignType());
            downloadSettleFileModel.setMerId(instance.getMerId());
            downloadSettleFileModel.setVersion(instance.getVersion());
            downloadSettleFileModel.setSettleDate(instance.getSettleDate());
            return downloadSettleFileModel;
        }
    }

    public static Builder builder() {
        return new Builder();
    }
}
