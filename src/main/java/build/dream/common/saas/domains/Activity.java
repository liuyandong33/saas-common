package build.dream.common.saas.domains;

import build.dream.common.basic.BasicDomain;

import java.util.Date;

public class Activity extends BasicDomain {
    /**
     * 活动名称
     */
    private String name;
    /**
     * 开始时间
     */
    private Date startTime;
    /**
     * 结束时间
     */
    private Date endTime;
    /**
     * 活动类型，1-买A赠B活动，2-整单满减活动，3-特价商品活动
     */
    private Integer type;
    /**
     * 活动状态，1-未执行，2-执行中，3-已终止，4-已过期
     */
    private Integer status;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }
}