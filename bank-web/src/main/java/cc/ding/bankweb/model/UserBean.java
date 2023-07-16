package cc.ding.bankweb.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * UserBean
 * 封装用户信息的类
 *
 * @author dingshuai
 * @version 1.8
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserBean {
    private String username;
    private String password;
}
