import cn.rbf.date.DateUtil;
import java.util.Arrays;
import java.util.Comparator;

/**
 * @author Morgan
 * @date 2021/4/2 10:37
 */
public class UtilTest {

  public static void main(String[] args) {
    UtilTest.test1();
  }

  static void test1() {
    Integer t1 = DateUtil.stringTimestamp("2018", "yyyy");
    Integer t2 = DateUtil.stringTimestamp("2018-07-13", "yyyy-MM-dd");
    System.out.println(t2 - t1);

  }

}
