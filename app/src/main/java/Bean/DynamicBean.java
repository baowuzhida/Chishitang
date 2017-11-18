package Bean;

import java.util.Date;

/**
 * Created by Administrator on 2017/11/13.
 */
public class DynamicBean {
    private  int d_id;
    private String d_content;
    private UserBean userBean;
    private String d_image;
    private String d_url;
    private String d_time;

    public DynamicBean(){this.userBean=new UserBean();}

    public int getD_id() {
        return d_id;
    }

    @Override
    public String toString() {
        return "DynamicBean{" +
                "d_id=" + d_id +
                ", d_content='" + d_content + '\'' +
                ", userBean=" + userBean.toString() +
                ", d_image='" + d_image + '\'' +
                ", d_url='" + d_url + '\'' +
                ", d_time=" + d_time +
                '}';
    }

    public void setD_id(int d_id) {
        this.d_id = d_id;
    }

    public String getD_content() {
        return d_content;
    }

    public void setD_content(String d_content) {
        this.d_content = d_content;
    }

    public UserBean getUserBean() {
        return userBean;
    }

    public void setUserBean(UserBean userBean) {
        this.userBean=userBean;
    }

    public String getD_image() {
        return d_image;
    }

    public void setD_image(String d_image) {
        this.d_image = d_image;
    }

    public String getD_url() {
        return d_url;
    }

    public void setD_url(String d_url) {
        this.d_url = d_url;
    }

    public String getD_time() {
        return d_time;
    }

    public void setD_time(String d_time) {
        this.d_time = d_time;
    }


}
