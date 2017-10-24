package Bean;

/**
 * Created by AYD on 2016/11/22.
 * <p>
 * 购物车
 */
public class ShoppingCartBean {

    private int id;
    private int product_id;
    private String imageUrl;
    private String shoppingName;
    private String detail;
    private int address;
    private double price;
    private int count;

    public boolean isChoosed;
    public boolean isCheck = false;

    public int getProduct_id() {
        return product_id;
    }

    public void setProduct_id(int product_id) {
        this.product_id = product_id;
    }

    public ShoppingCartBean() {
    }

    public ShoppingCartBean(int id, String shoppingName, String detail,
                            int address, double price, int count,int product_id) {
        this.id = id;
        this.product_id = product_id;
        this.shoppingName = shoppingName;
        this.detail = detail;
        this.address = address;
        this.price = price;
        this.count = count;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public boolean isChoosed() {
        return isChoosed;
    }

    public void setChoosed(boolean choosed) {
        isChoosed = choosed;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getShoppingName() {
        return shoppingName;
    }

    public void setShoppingName(String shoppingName) {
        this.shoppingName = shoppingName;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public int getAddress() {
        return address;
    }

    public void setAddress(int address) {
        this.address = address;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

}
