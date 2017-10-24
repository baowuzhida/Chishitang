package Bean;

/**
 * Created by hasee on 2017/9/19.
 */
public class UserEvaluateBean {
    private int user_evaluate_id;
    private int user_id;
    private int product_id;
    private String user_evaluate;
    private UserBean userBean;

    public UserEvaluateBean(){userBean=new UserBean();}

    public int getUser_evaluate_id() {
        return user_evaluate_id;
    }

    public void setUser_evaluate_id(int user_evaluate_id) {
        this.user_evaluate_id = user_evaluate_id;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public int getProduct_id() {
        return product_id;
    }

    public void setProduct_id(int product_id) {
        this.product_id = product_id;
    }

    public String getUser_evaluate() {
        return user_evaluate;
    }

    public void setUser_evaluate(String user_evaluate) {
        this.user_evaluate = user_evaluate;
    }

    public UserBean getUserBean() {
        return userBean;
    }

    public void setUserBean(UserBean userBean) {
        this.userBean = userBean;
    }

    @Override
    public String toString() {
        return "UserEvaluate{" +
                "user_evaluate_id=" + user_evaluate_id +
                ", user_id=" + user_id +
                ", product_id=" + product_id +
                ", user_evaluate='" + user_evaluate + '\'' +
                ", userBean=" + userBean.toString() +
                '}';
    }
}
