package cc.ding.bankweb.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * MoneyBean
 * 封装余额的类
 *
 * @author dingshuai
 * @version 1.8
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MoneyBean {
    private BigDecimal balance;
}
