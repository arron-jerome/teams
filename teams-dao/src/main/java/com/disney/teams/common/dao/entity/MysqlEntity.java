package com.disney.teams.common.dao.entity;


import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import java.sql.Timestamp;

@MappedSuperclass
public abstract class MysqlEntity extends LongEntity {
    private static final long serialVersionUID = -5844236379869480340L;

    //标识
    @Id
    @GeneratedValue
    private Long id;
    public static final String ID_PROPERTY = "id";
    public static final String ID_COLUMN = "id";

    @Column(name = CREATE_TIME_COLUMN)
    private Timestamp createTime;
    public static final String CREATE_TIME_PROPERTY = "createTime";
    public static final String CREATE_TIME_COLUMN = "create_time";

    @Column(name = UPDATE_TIME_COLUMN)
    private Timestamp updateTime;
    public static final String UPDATE_TIME_PROPERTY = "updateTime";
    public static final String UPDATE_TIME_COLUMN = "update_time";

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }

    public Timestamp getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Timestamp createTime) {
        this.createTime = createTime;
    }

    public Timestamp getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Timestamp updateTime) {
        this.updateTime = updateTime;
    }
}
