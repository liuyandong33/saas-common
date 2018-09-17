package build.dream.common.saas.domains;

import build.dream.common.basic.BasicDomain;

import java.math.BigInteger;
import java.util.Date;

public class Goods extends BasicDomain {
    /**
     * 商品名称
     */
    private String name;
    /**
     * 商品类型，1-设备，2-基础服务，3-增值服务
     */
    private BigInteger goodsTypeId;
    /**
     * 商品状态，1-正常，2-停售
     */
    private Integer status;
    /**
     * 图片路径
     */
    private String photoUrl;
    /**
     * 计量方式，1-按时间，2-按数量
     */
    private Integer meteringMode;
    /**
     * 业态，1-餐饮，2-零售
     */
    private String business;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BigInteger getGoodsTypeId() {
        return goodsTypeId;
    }

    public void setGoodsTypeId(BigInteger goodsTypeId) {
        this.goodsTypeId = goodsTypeId;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }

    public Integer getMeteringMode() {
        return meteringMode;
    }

    public void setMeteringMode(Integer meteringMode) {
        this.meteringMode = meteringMode;
    }

    public String getBusiness() {
        return business;
    }

    public void setBusiness(String business) {
        this.business = business;
    }

    public static class Builder {
        private final Goods instance = new Goods();

        public Builder name(String name) {
            instance.setName(name);
            return this;
        }

        public Builder goodsTypeId(BigInteger goodsTypeId) {
            instance.setGoodsTypeId(goodsTypeId);
            return this;
        }

        public Builder status(Integer status) {
            instance.setStatus(status);
            return this;
        }

        public Builder photoUrl(String photoUrl) {
            instance.setPhotoUrl(photoUrl);
            return this;
        }

        public Builder meteringMode(Integer meteringMode) {
            instance.setMeteringMode(meteringMode);
            return this;
        }

        public Builder business(String business) {
            instance.setBusiness(business);
            return this;
        }

        public Builder id(BigInteger id) {
            instance.setId(id);
            return this;
        }

        public Builder createTime(Date createTime) {
            instance.setCreateTime(createTime);
            return this;
        }

        public Builder createUserId(BigInteger createUserId) {
            instance.setCreateUserId(createUserId);
            return this;
        }

        public Builder lastUpdateTime(Date lastUpdateTime) {
            instance.setLastUpdateTime(lastUpdateTime);
            return this;
        }

        public Builder lastUpdateUserId(BigInteger lastUpdateUserId) {
            instance.setLastUpdateUserId(lastUpdateUserId);
            return this;
        }

        public Builder lastUpdateRemark(String lastUpdateRemark) {
            instance.setLastUpdateRemark(lastUpdateRemark);
            return this;
        }

        public Builder deleteTime(Date deleteTime) {
            instance.setDeleteTime(deleteTime);
            return this;
        }

        public Builder deleted(boolean deleted) {
            instance.setDeleted(deleted);
            return this;
        }

        public Goods build() {
            return instance;
        }
    }

    public static Builder builder() {
        return new Builder();
    }

    public static final class ColumnName extends BasicDomain.ColumnName {
        public static final String NAME = "name";
        public static final String GOODS_TYPE_ID = "goods_type_id";
        public static final String STATUS = "status";
        public static final String PHOTO_URL = "photo_url";
        public static final String METERING_MODE = "metering_mode";
        public static final String BUSINESS = "business";
    }
}