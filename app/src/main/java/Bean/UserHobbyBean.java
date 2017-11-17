package Bean;

/**
 * Created by Baowuzhida on 2017/11/16.
 */

public class UserHobbyBean {
    private int ub_id;
    private int user_id;
    private int ub_grain;//谷物
    private int ub_beef;//肉类
    private int ub_beans;//豆制品
    private int ub_vegetables;//蔬菜
    private int ub_fat;//脂肪

    public UserHobbyBean(){};

    public int getUb_id() {
        return ub_id;
    }

    public void setUb_id(int ub_id) {
        this.ub_id = ub_id;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public int getUb_grain() {
        return ub_grain;
    }

    public void setUb_grain(int ub_grain) {
        this.ub_grain = ub_grain;
    }

    public int getUb_beef() {
        return ub_beef;
    }

    public void setUb_beef(int ub_beef) {
        this.ub_beef = ub_beef;
    }

    public int getUb_beans() {
        return ub_beans;
    }

    public void setUb_beans(int ub_beans) {
        this.ub_beans = ub_beans;
    }

    public int getUb_vegetables() {
        return ub_vegetables;
    }

    public void setUb_vegetables(int ub_vegetables) {
        this.ub_vegetables = ub_vegetables;
    }

    public int getUb_fat() {
        return ub_fat;
    }

    public void setUb_fat(int ub_fat) {
        this.ub_fat = ub_fat;
    }
}
