package build.dream.common.models.data;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.constraints.NotNull;

public class ConfirmGoodsModel extends DadaBasicModel {
    @NotNull
    @JsonProperty(value = "order_id")
    private String orderId;

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public static class Builder extends DadaBasicModel.Builder<Builder, ConfirmGoodsModel> {
        public Builder orderId(String orderId) {
            instance.setOrderId(orderId);
            return this;
        }


        @Override
        public ConfirmGoodsModel build() {
            ConfirmGoodsModel confirmGoodsModel = super.build();
            confirmGoodsModel.setOrderId(instance.getOrderId());
            return confirmGoodsModel;
        }
    }

    public static Builder builder() {
        return new Builder();
    }
}