package cn.kgc.entity;
/*双向多对多*/
import java.util.HashSet;
import java.util.Set;

public class Item {
    private Integer id;
    private String name;


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
