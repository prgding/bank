package cc.ding.bankweb.page;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MyPage {
    private int currentPage;
    private int totalRowNum;
    private int pageSize;
    private int startIndex;
}
