package saga.com.mypractise.model;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Property;
import org.greenrobot.greendao.annotation.Unique;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Created by lp on 2018/6/25.
 * greendao 的实体
 */

@Entity
public class Friend {
    //不能用int
    @Id(autoincrement = true)
    private Long id;
    //昵称
    @Unique
    private String name;
    //商品价格
    @Property(nameInDb = "school")
    private String school;
    @Generated(hash = 1897477955)
    public Friend(Long id, String name, String school) {
        this.id = id;
        this.name = name;
        this.school = school;
    }
    @Generated(hash = 287143722)
    public Friend() {
    }
    public Long getId() {
        return this.id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getName() {
        return this.name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getSchool() {
        return this.school;
    }
    public void setSchool(String school) {
        this.school = school;
    }
}
