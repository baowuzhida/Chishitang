package Bean;

/**
 * Created by Baowuzhida on 2017/11/7.
 */

public class CrowdFundBean {

    private int crowdfund_id;//id
    private int builder_id;//创建用户的id
    private String crowdfund_image;//图片 最多五张最少一张
    private String crowdfund_name;//名称
    private String crowdfund_detail;//详情
    private String crowdfund_declaration;//宣言 类似于请大家支持我
    private int state;//判断审核状态 0 审核 1 通过 默认0 后台更改状态
    private double crowdfund_capital;//获得的资金
    private double crowdfund_aimcapital;//目标资金
    private int crowdfund_type;//菜品类型 0 主食 1 小菜 2 略。。。
    private int crowdfund_supporters;//支持人数 默认0 一个赞助加一个

    public CrowdFundBean(int crowdfund_id, int builder_id, String crowdfund_image, String crowdfund_name, String crowdfund_detail, String crowdfund_declaration, int state, double crowdfund_capital, double crowdfund_aimcapital, int crowdfund_type, int crowdfund_supporters) {
        this.crowdfund_id = crowdfund_id;
        this.builder_id = builder_id;
        this.crowdfund_image = crowdfund_image;
        this.crowdfund_name = crowdfund_name;
        this.crowdfund_detail = crowdfund_detail;
        this.crowdfund_declaration = crowdfund_declaration;
        this.state = state;
        this.crowdfund_capital = crowdfund_capital;
        this.crowdfund_aimcapital = crowdfund_aimcapital;
        this.crowdfund_type = crowdfund_type;
        this.crowdfund_supporters = crowdfund_supporters;
    }

    public int getCrowdfund_id() {
        return crowdfund_id;
    }

    public void setCrowdfund_id(int crowdfund_id) {
        this.crowdfund_id = crowdfund_id;
    }

    public int getBuilder_id() {
        return builder_id;
    }

    public void setBuilder_id(int builder_id) {
        this.builder_id = builder_id;
    }

    public String getCrowdfund_image() {
        return crowdfund_image;
    }

    public void setCrowdfund_image(String crowdfund_image) {
        this.crowdfund_image = crowdfund_image;
    }

    public String getCrowdfund_name() {
        return crowdfund_name;
    }

    public void setCrowdfund_name(String crowdfund_name) {
        this.crowdfund_name = crowdfund_name;
    }

    public String getCrowdfund_detail() {
        return crowdfund_detail;
    }

    public void setCrowdfund_detail(String crowdfund_detail) {
        this.crowdfund_detail = crowdfund_detail;
    }

    public String getCrowdfund_declaration() {
        return crowdfund_declaration;
    }

    public void setCrowdfund_declaration(String crowdfund_declaration) {
        this.crowdfund_declaration = crowdfund_declaration;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public double getCrowdfund_capital() {
        return crowdfund_capital;
    }

    public void setCrowdfund_capital(double crowdfund_capital) {
        this.crowdfund_capital = crowdfund_capital;
    }

    public double getCrowdfund_aimcapital() {
        return crowdfund_aimcapital;
    }

    public void setCrowdfund_aimcapital(double crowdfund_aimcapital) {
        this.crowdfund_aimcapital = crowdfund_aimcapital;
    }

    public int getCrowdfund_type() {
        return crowdfund_type;
    }

    public void setCrowdfund_type(int crowdfund_type) {
        this.crowdfund_type = crowdfund_type;
    }

    public int getCrowdfund_supporters() {
        return crowdfund_supporters;
    }

    public void setCrowdfund_supporters(int crowdfund_supporters) {
        this.crowdfund_supporters = crowdfund_supporters;
    }

    @Override
    public String toString() {
        return "CrowdFundBean{" +
                "crowdfund_id=" + crowdfund_id +
                ", builder_id=" + builder_id +
                ", crowdfund_image='" + crowdfund_image + '\'' +
                ", crowdfund_name='" + crowdfund_name + '\'' +
                ", crowdfund_detail='" + crowdfund_detail + '\'' +
                ", crowdfund_declaration='" + crowdfund_declaration + '\'' +
                ", state=" + state +
                ", crowdfund_capital=" + crowdfund_capital +
                ", crowdfund_aimcapital=" + crowdfund_aimcapital +
                ", crowdfund_type=" + crowdfund_type +
                ", crowdfund_supporters=" + crowdfund_supporters +
                '}';
    }
}
