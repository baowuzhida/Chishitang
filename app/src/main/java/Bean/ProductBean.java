package Bean;

/**
 * Created by hasee on 2017/8/9.
 */
public class ProductBean {

    private int product_id;
    private String product_name;
    private String product_details;
    private String product_image,product_imgaddress;
    private String product_address;
    private double product_price;
    private int product_type;
    private int amount;

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public ProductBean() {
        super();
    }

    public String getProduct_imgaddress() {
        return product_imgaddress;
    }

    public void setProduct_imgaddress(String product_imgaddress) {
        this.product_imgaddress = product_imgaddress;
    }

    @Override
    public String toString() {
        return "ProductBean{" +
                "product_id=" + product_id +
                ", product_name='" + product_name + '\'' +
                ", product_details='" + product_details + '\'' +
                ", product_image='" + product_image + '\'' +
                ", product_address='" + product_address + '\'' +
                ", product_type=" + product_type +
                ", product_price=" + product_price +
                '}';
    }

    public double getProduct_price() {
        return product_price;
    }

    public void setProduct_price(double product_price) {
        this.product_price = product_price;
    }

    public ProductBean(int product_id) {
        this.product_id = product_id;
    }

    public void setProduct_id(int product_id) {
        this.product_id = product_id;
    }

    public void setProduct_name(String product_name) {
        this.product_name = product_name;
    }

    public void setProduct_details(String product_details) {
        this.product_details = product_details;
    }

    public void setProduct_image(String product_image) {
        this.product_image = product_image;
    }

    public void setProduct_address(String product_address) {
        this.product_address = product_address;
    }

    public void setProduct_type(int product_type) {
        this.product_type = product_type;
    }

    public int getProduct_id() {
        return product_id;
    }

    public String getProduct_name() {
        return product_name;
    }

    public String getProduct_details() {
        return product_details;
    }

    public String getProduct_image() {
        return product_image;
    }

    public String getProduct_address() {
        return product_address;
    }

    public int getProduct_type() {
        return product_type;
    }
}
