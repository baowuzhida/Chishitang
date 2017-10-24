package Bean;

/**
 * Created by hasee on 2017/7/26.
 */
public class OrdersBean {
    private  int orders_id;
    private  int user_id;
    private double orders_price;
    private String orders_time;

    public OrdersBean(){}

    public OrdersBean(int orders_id, int user_id,  float orders_price) {
        this.orders_id = orders_id;
        this.user_id = user_id;
        this.orders_price = orders_price;
    }

    public void setOrders_id(int orders_id) {
        this.orders_id = orders_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public void setOrders_price(double orders_price) {
        this.orders_price = orders_price;
    }

    public int getOrders_id() {
        return orders_id;
    }

    public int getUser_id() {
        return user_id;
    }

    public String getOrders_time() {
        return orders_time;
    }

    public void setOrders_time(String orders_time) {
        this.orders_time = orders_time;
    }

    public double getOrders_price() {
        return orders_price;
    }

    @Override
    public String toString() {
        return "OrdersBean{" +
                "orders_id=" + orders_id +
                ", user_id=" + user_id +
                ", orders_price=" + orders_price +
                ", orders_time='" + orders_time + '\'' +
                '}';
    }
}
