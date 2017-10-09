class SmallMap{
	public int Map[];
	public int g, f;
	public SmallMap preSM;
	public int moveBtn;
	public int space;
	
	public SmallMap(int Size){
		Map = new int[Size];
		g = 0;
		f = 0;
		preSM = null;
		space = 0;		
	}
}