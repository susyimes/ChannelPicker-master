package com.susyimes.administrator.channelpicker;

import com.litesuits.orm.db.annotation.Column;
import com.litesuits.orm.db.annotation.Table;

/**
 * Created by Susyimes on 2016/8/24 0024.
 */
@Table("channelob") public class ChannelOtherBean extends Soul {
    @Column("cname") public String cname;
    @Column("cid") public String cid;
    @Column("isChoose") public String isChoose;

    public String getIsChoose() {
        return isChoose;
    }

    public void setIsChoose(String isChoose) {
        this.isChoose = isChoose;
    }

    @Override

    public String toString() {
        return "ChannelOtherBean{" +
                "cname='" + cname + '\'' +
                ", cid='" + cid + '\'' +
                ", isChoose='" + isChoose + '\'' +
                '}';
    }

    public String getCid() {
        return cid;
    }

    public void setCid(String cid) {
        this.cid = cid;
    }

    public String getCname() {
        return cname;
    }

    public void setCname(String cname) {
        this.cname = cname;
    }

}
