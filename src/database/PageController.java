package database;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class PageController {
	   private List<Forestfire> bigList = new ArrayList<Forestfire>(); //�󼯺ϣ�������ȡ 
	   private List<Forestfire> smallList = new ArrayList<Forestfire>(); //С���ϣ����ظ�����������
       private static int curentPageIndex = 1;        //��ǰҳ��                  
       private int countPerpage = 25;        //ÿҳ��ʾ����
       private int pageCount;           //��ҳ��
       private int recordCount;           //�ܼ�¼����
       
       //��ʼ���飬�������ʱ�������Զ�����
       { 
    	   //���ò�ѯ���ݿ�ķ���������һ��List
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
			
    	   //������ҳ��
    	   if (bigList.size()%countPerpage==0) {
    		   this.pageCount=bigList.size()/countPerpage;
		   } else {
			   this.pageCount=(bigList.size()/countPerpage)+1;
		   }
       }
      
       public  PageController() {
    	  
	   }
       //����ָ��ҳ��Ĺ��캯�����ο��ڼ�ҳ��
       public PageController(int curentPageIndex){
    	   this.curentPageIndex = curentPageIndex;
       }
       
       //ȷ�еĻ�ȡ��ǰҳ�ļ�¼������һ��list�б�
       public List<Forestfire> setCurentPageIndex() {     
             
              return select();
       }
    
       //��һҳ
       public List<Forestfire> nextPage() {
    	   
            if (curentPageIndex < pageCount ) {
            	 curentPageIndex++;
//            	 System.out.println("��ǰҳ:"+curentPageIndex);
            } 
  			return select();  
       }
      //��һҳ
      public List<Forestfire> previousPage() {
            if (curentPageIndex > 1) {
                  curentPageIndex--;
//                  System.out.println("��ǰҳ:"+curentPageIndex);
            }
          
           return select();
      }
      
      //���һҳ
      public List<Forestfire> lastPage() {
           curentPageIndex = pageCount;
           return select();
      }
      
      //��ǰһҳ
      public int nowPage() {
//           curentPageIndex = pageCount;
           return curentPageIndex;
      }
      
      //�˷��������Ϸ������ã����ݵ�ǰҳ��ɸѡ��¼
      public List<Forestfire> select(){
          
          recordCount=bigList.size();
          for(int i=(curentPageIndex-1)*countPerpage; i<curentPageIndex*countPerpage&&i<recordCount; i++){
                 smallList.add(bigList.get(i));
          } 
        
          return smallList;
      }
     
}