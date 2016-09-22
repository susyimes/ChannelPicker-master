
package com.susyimes.administrator.channelpicker;

import com.litesuits.orm.db.annotation.Column;
import com.litesuits.orm.db.annotation.PrimaryKey;
import com.litesuits.orm.db.enums.AssignType;

import java.io.Serializable;


public class Soul implements Serializable {

    @PrimaryKey(AssignType.AUTO_INCREMENT) @Column("_id") public int id;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
    // @NotNull
  //  @Unique
   // @Column("objectId") public String objectId;
}
