package info.sjd.model;

import lombok.*;
import java.math.BigDecimal;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Product {

    private String code;
    private String name;
    private BigDecimal price;
    private BigDecimal initPrice;
    private String avialability;
    private String url;

}
