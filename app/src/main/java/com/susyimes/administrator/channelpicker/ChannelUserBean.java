package com.susyimes.administrator.channelpicker;

import com.litesuits.orm.db.annotation.Column;
import com.litesuits.orm.db.annotation.Table;

/**
 * Created by Susyimes on 2016/8/24 0024.
 */
@Table("channeluserbean") public class ChannelUserBean extends Soul {
    @Column("cname") public String cname;

    public String getCname() {
        return cname;
    }

    public void setCname(String cname) {
        this.cname = cname;
    }

    @Override
    public String toString() {
        return "ChannelUserBean{" +
                "cname='" + cname + '\'' +
                '}';
    }
}
