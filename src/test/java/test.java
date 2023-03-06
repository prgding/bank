import org.apache.ibatis.javassist.ClassPool;
import org.apache.ibatis.session.SqlSession;
import org.junit.Test;
import pojo.User;
import utils.SqlSessionUtil;

public class test {
	@Test
	public void testSelect() {
		SqlSession sqlSession = SqlSessionUtil.open();
		User user = sqlSession.selectOne("select", "act001");
		System.out.println(user);
	}
	@Test
	public void testGenerate(){
		ClassPool pool = ClassPool.getDefault();
		pool.makeClass("dao.impl.ActDaoImpl");
	}
}
