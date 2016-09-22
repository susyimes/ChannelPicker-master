package com.susyimes.administrator.channelpicker;

import com.litesuits.orm.db.annotation.Column;
import com.litesuits.orm.db.annotation.Table;

/**
 * Created by Susyimes on 2016/8/24 0024.
 */
@Table("channelbean") public class ChannelBean extends Soul {
    @Column("channelname") public String chname;

    public String getChname() {
        return chname;
    }

    public void setChname(String chname) {
        this.chname = chname;
    }

    @Override
    public String toString() {
        return "ChannelBean{" +
                "chname='" + chname + '\'' +
                '}';
    }
}
