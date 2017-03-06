package database;
	
	class Forestfire {
		public int id;
		public String month;
		public String day;
		public double area;
		public double level;
		public double possibility;
		public int wd; //wounded + deadth
//		public int deadth;
		public double money;
		
		Forestfire(int id,String month,String day,double area,double level,double possibility,int wd,double money){
			this.id = id;
			this.month = month;
			this.day = day;
			this.area = area;
			this.level = level;
			this.possibility = possibility;
			this.wd = wd;
//			this.deadth = deadth;
			this.money = money;
		}

	}