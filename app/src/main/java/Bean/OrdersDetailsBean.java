package Bean;

/**
 * Created by hasee on 2017/8/9.
 */
public class OrdersDetailsBean {
    private int orders_detail_id;
    private int orders_id;
    private ProductBean productBean;
    private int product_amount;
    public OrdersDetailsBean(){productBean=new ProductBean();}

    public void setOrders_detail_id(int orders_detail_id) {
        this.orders_detail_id = orders_detail_id;
    }

    public void setOrders_id(int orders_id) {
        this.orders_id = orders_id;
    }

    public void setProductBean(ProductBean productBean) {
        this.productBean = productBean;
    }

    public void setProduct_amount(int product_amount) {
        this.product_amount = product_amount;
    }

    public int getOrders_detail_id() {
        return orders_detail_id;
    }

    public int getOrders_id() {
        return orders_id;
    }

    public ProductBean getProductBean() {
        return productBean;
    }

    public int getProduct_amount() {
        return product_amount;
    }
}
