package database;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class PageController {
	   private List<Forestfire> bigList = new ArrayList<Forestfire>(); //大集合，从外界获取 
	   private List<Forestfire> smallList = new ArrayList<Forestfire>(); //小集合，返回给调用它的类
       private static int curentPageIndex = 1;        //当前页码                  
       private int countPerpage = 25;        //每页显示条数
       private int pageCount;           //总页数
       private int recordCount;           //总记录条数
       
       //初始化块，调用类的时候首先自动加载
       { 
    	   //调用查询数据库的方法，返回一个List
			try {
				Connection conn = new Connect().getConn();	
				Statement st = conn.createStatement();
				String sql = "select f.id,d.month,d.day,f.area,f.level,f.possibility,l.wd,l.money from date_factor d,forest_fires f,loss l where d.id=f.did and l.id=f.lid";
				ResultSet res = st.executeQuery(sql);
				while(res.next()) {
					Forestfire f;
					f = new Forestfire(res.getInt(1),res.getString(2), res.getString(3), res.getDouble(4), res.getDouble(5),res.getDouble(6),res.getInt(7),res.getDouble(8));
					this.bigList.add(f);
				}				
			} catch (Exception e) {
				e.printStackTrace();
			}
			
    	   //计算总页数
    	   if (bigList.size()%countPerpage==0) {
    		   this.pageCount=bigList.size()/countPerpage;
		   } else {
			   this.pageCount=(bigList.size()/countPerpage)+1;
		   }
       }
      
       public  PageController() {
    	  
	   }
       //传入指定页码的构造函数，参看第几页。
       public PageController(int curentPageIndex){
    	   this.curentPageIndex = curentPageIndex;
       }
       
       //确切的获取当前页的记录，返回一个list列表
       public List<Forestfire> setCurentPageIndex() {     
             
              return select();
       }
    
       //下一页
       public List<Forestfire> nextPage() {
    	   
            if (curentPageIndex < pageCount ) {
            	 curentPageIndex++;
//            	 System.out.println("当前页:"+curentPageIndex);
            } 
  			return select();  
       }
      //上一页
      public List<Forestfire> previousPage() {
            if (curentPageIndex > 1) {
                  curentPageIndex--;
//                  System.out.println("当前页:"+curentPageIndex);
            }
          
           return select();
      }
      
      //最后一页
      public List<Forestfire> lastPage() {
           curentPageIndex = pageCount;
           return select();
      }
      
      //当前一页
      public int nowPage() {
//           curentPageIndex = pageCount;
           return curentPageIndex;
      }
      
      //此方法供以上方法调用，根据当前页，筛选记录
      public List<Forestfire> select(){
          
          recordCount=bigList.size();
          for(int i=(curentPageIndex-1)*countPerpage; i<curentPageIndex*countPerpage&&i<recordCount; i++){
                 smallList.add(bigList.get(i));
          } 
        
          return smallList;
      }
     
}