package opensearchtest;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import jakarta.persistence.*;
import util.Log;

public class JPAUtil {
	
	private static Logger logger = LoggerFactory.getLogger(JPAUtil.class);

	EntityManager em;
	public void init()
	{
		EntityManagerFactory emf = Persistence.createEntityManagerFactory("PERSISTENCE");
	    em = emf.createEntityManager();

	}
	
	public List<Map<String,Object>> dynamicSelect(String sql,Map<String,Object> bindingMap,int rownum)
	{
		 Log.log("\t===== dynamicSelect START!! "+ "#sql="+ sql+ "#bindingMap="+ bindingMap);
		List<Map<String,Object>> result = new ArrayList<>();
		
//		List<Tuple> list = em.createNativeQuery("select tname as name,tabtype as detail from tab where tname like :prefix", Tuple.class)
//				.setParameter( "prefix", prefix)
//				.getResultList();
		Query query = em.createNativeQuery(sql, Tuple.class);
		if(rownum > 0) query.setMaxResults(rownum);
		Iterator<String> it = bindingMap.keySet().iterator();
		while(it.hasNext()) 
		{
			String key = it.next();
			Object value = bindingMap.get(key);
			query.setParameter(key, value);
		}
		List<Tuple> list = query.getResultList();
		
		Log.log("\t===== dynamicSelect createNativeQuery !! "+ "#list="+ list.size());
		for(int i=0;i<list.size();i++)
		{
			Tuple tuple = list.get(i);
			//System.out.println("=== "+ "#i="+ i +"#tuple="+  ToStringBuilder.reflectionToString(tuple, ToStringStyle.JSON_STYLE));
			Map<String,Object> map = new LinkedHashMap<>();
			result.add(map);
			Iterator<TupleElement<?>> it2 = tuple.getElements().iterator();
			while(it2.hasNext())
			{
				String key = it2.next().getAlias();
				Object value = tuple.get(key);
				if(value instanceof String) map.put(key, ((String)value));
				else map.put(key, value);
				//System.out.println("=== "+ "#i="+ i +"#key="+  key +"#value="+ value);
			}
		}
		return result;
	}

	public int dynamicUpdate(String sql,Map<String,Object> bindingMap)
	{
		Log.log("\t===== dynamicUpdate START!! "+ "#sql="+ sql+ "#bindingMap="+ bindingMap);
		em.getTransaction().begin();
		
		Query query = em.createNativeQuery(sql);
		Iterator<String> it = bindingMap.keySet().iterator();
		while(it.hasNext()) 
		{
			String key = it.next();
			Object value = bindingMap.get(key);
			query.setParameter(key, value);
		}
		int result = query.executeUpdate();
		
		Log.log("\t===== dynamicUpdate END !! "+ "#result="+ result);

		em.getTransaction().commit();
		return result;
	}	
	
	
	public static void main(String[] args) {
		
		JPAUtil jpa = new JPAUtil();
		jpa.init();
		
		
//		for(int i=0;i<5;i++)
//		{
//			Map<String,Object> bindingMap =  new LinkedHashMap<String, Object>();
//			bindingMap.put("id", "id---"+i);
//			bindingMap.put("name", "name---"+i);
//			bindingMap.put("address", "address---"+i);
//			String sql = "insert into testuser (id,name,address) values (:id,:name,:address)";
//			int result = jpa.dynamicUpdate(sql,bindingMap);
//			Log.log("----- result # "+ result);
//		}
		
		String sql = "select * from testuser where id like :id";
		Map<String,Object> bindingMap =  new LinkedHashMap<String, Object>();
		bindingMap.put("id", "id%");
		List<Map<String, Object>> list = jpa.dynamicSelect(sql,bindingMap, 0);
		Log.log("----- list # "+ list);
		

	}
}
