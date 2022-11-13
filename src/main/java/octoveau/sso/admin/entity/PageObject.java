package octoveau.sso.admin.entity;

import lombok.Data;

import java.util.List;

/**
 * PageObject
 *
 * @author yifanzheng
 */
@Data
public class PageObject<T> {

    private Long total;
    private List<T> data;

    public static <T> PageObject<T> of(Long total, List<T> data) {
        PageObject<T> pageObject = new PageObject<>();
        pageObject.setData(data);
        pageObject.setTotal(total);

        return pageObject;
    }

}
