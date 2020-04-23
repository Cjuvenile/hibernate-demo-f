package cn.kgc.entity;


public class Department1 {

    private Integer deptId;
    private  String name;

/*表关联*/
    private Manager1 manager;

    public Integer getDeptId() {
        return deptId;
    }

    public void setDeptId(Integer deptId) {
        this.deptId = deptId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Manager1 getManager() {
        return manager;
    }

    public void setManager(Manager1 manager) {
        this.manager = manager;
    }
}
