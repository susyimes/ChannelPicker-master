package com.susyimes.administrator.channelpicker;

import com.litesuits.orm.db.annotation.Column;
import com.litesuits.orm.db.annotation.Table;

/**
 * Created by Susyimes on 2016/8/24 0024.
 */
@Table("channeluserbean") public class ChannelUserBean extends Soul {
    @Column("channelusername") public String chusername;

    public String getChusername() {
        return chusername;
    }

    public void setChusername(String chusername) {
        this.chusername = chusername;
    }

    @Override
    public String toString() {
        return "ChannelUserBean{" +
                "chusername='" + chusername + '\'' +
                '}';
    }
}
