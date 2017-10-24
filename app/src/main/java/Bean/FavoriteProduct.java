package Bean;

/**
 * Created by hasee on 2017/9/7.
 */
public class FavoriteProduct {
    private int fproduct_id;
    private ProductBean product;
    private int number_votes;
    private String time;

    public FavoriteProduct(){
        product=new ProductBean();
    }

    public int getFproduct_id() {
        return fproduct_id;
    }

    public void setFproduct_id(int fproduct_id) {
        this.fproduct_id = fproduct_id;
    }

    public ProductBean getProduct() {
        return product;
    }


    public void setProduct(ProductBean product) {
        this.product = product;
    }

    public int getNumber_votes() {
        return number_votes;
    }

    public void setNumber_votes(int number_votes) {
        this.number_votes = number_votes;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
    @Override
    public String toString() {
        return "FavoriteProduct{" +
                "fproduct_id=" + fproduct_id +
                ", product=" + product +
                ", number_votes=" + number_votes +
                ", time='" + time + '\'' +
                '}';
    }
}
