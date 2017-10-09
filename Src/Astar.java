public class Astar{							//Astar Search
	private int local[];
	private int goal[];
	private SmallMap current;
	private int W, H, T;
	private int resultSize;
	public int count; 
	
	public Astar(int Width, int Height, int[] in){
		W = Width;
		H = Height;
		T = W * H;
		resultSize = 0;
		count = 0;
		local = new int[T];
		goal = new int[T];		
		current = new SmallMap(T);
		
		for(int i = 0; i < T; ++i){
			goal[i] = i;
			current.Map[i] = in[i];
			local[current.Map[i] - 1] = i;
			if(current.Map[i] == T)
				current.space = i;
		}		
	}
	
	public boolean MatchGoal(){
		for (int i = 0; i < T - 1; ++i)
			if (current.Map[i] != i + 1)return false;		
		return true;
	}
	
	public boolean Solvable(){
		int sum = 0;
		for(int i = 0; i < T - 1; ++i){
			for(int j = local[i] + 1; j < T; ++j)
				if(i + 1 > current.Map[j])sum++;
		}
		return (sum % 2 == 0);
	}
	
	public int h(SmallMap pass){
		for(int i = 0; i < T; ++i)
			local[pass.Map[i] - 1] = i;
		
		int count = 0;
		for(int i = 0; i < T - 1; ++i)	//Manhattan
			count += Math.abs(local[i] % W - i % W) + Math.abs(local[i] / W - i / W);
			//if(current.Map[i] != i + 1)count++;
		return count;
	}
	
	public void ReverseSM(int Size, SmallMap[] T){
		SmallMap []r = new SmallMap[Size];
		for(int i = 0; i < Size; ++i)r[i] = T[i];
		for(int i = 0, j = Size - 1; i < Size; ++i, --j)
			T[i] = r[j];
	}
	
	public SmallMap[] GenerateStep(){
		int tmpSpace;
		SmallMap[] step = new SmallMap[]{ new SmallMap(T), new SmallMap(T), new SmallMap(T), new SmallMap(T) };
		
		//Left
		if((current.space % W != 0) && (current.moveBtn != current.Map[current.space - 1])){			
			for(int i = 0; i < T; ++i)step[0].Map[i] = current.Map[i];
			step[0].moveBtn = step[0].Map[current.space - 1];
			tmpSpace = step[0].Map[current.space];
			step[0].Map[current.space] = step[0].Map[current.space - 1];
			step[0].Map[current.space - 1] = tmpSpace;
			step[0].space = current.space - 1;			
		}else step[0] = null;
		//UP
		if((current.space / W > 0) && (current.moveBtn != current.Map[current.space - W])){
			for(int i = 0; i < T; ++i)step[1].Map[i] = current.Map[i];
			step[1].moveBtn = step[1].Map[current.space - W];
			tmpSpace = step[1].Map[current.space];
			step[1].Map[current.space] = step[1].Map[current.space - W];
			step[1].Map[current.space - W] = tmpSpace;
			step[1].space = current.space - W;
		}else step[1] = null;
		//Right
		if((current.space % W != W - 1) && (current.moveBtn != current.Map[current.space + 1])){
			for(int i = 0; i < T; ++i)step[2].Map[i] = current.Map[i];
			step[2].moveBtn = step[2].Map[current.space + 1];
			tmpSpace = step[2].Map[current.space];
			step[2].Map[current.space] = step[2].Map[current.space + 1];
			step[2].Map[current.space + 1] = tmpSpace;
			step[2].space = current.space + 1;			
		}else step[2] = null;
		//Down
		if((current.space / W < H - 1) && (current.moveBtn != current.Map[current.space + W])){
			for(int i = 0; i < T; ++i)step[3].Map[i] = current.Map[i];
			step[3].moveBtn = step[3].Map[current.space + W];
			tmpSpace = step[3].Map[current.space];
			step[3].Map[current.space] = step[3].Map[current.space + W];
			step[3].Map[current.space + W] = tmpSpace;
			step[3].space = current.space + W;
		}else step[3] = null;
		
		return step;
	}
	
	public SmallMap[] AstarResult(){
		Heap open = new Heap()/*, close*/;
		current.f = h(current);
		open.Insert(current);
		int times = 2000000;
		if(Solvable() == false)
			return null;
		while(true){
			count++;
			current = open.Remove();	
			if(count == times){
				System.out.println(times);				
				times += 2000000;
			}
			if(count >= 1000000)
				return null;
			//try{
			//	Thread.sleep(5000);
			//}catch(Exception e){}
			if(MatchGoal()){
				SmallMap[] solution = new SmallMap[100];
				SmallMap s = current;
				solution[resultSize++] = s;
				
				
				for(int i = 0; i < T; ++i){
					System.out.print(current.Map[i] + "\t");
					if( i % W == W - 1) System.out.println("");
				}System.out.println("==========================");
				
				while(s.preSM != null){					
					s = s.preSM;
					solution[resultSize++] = s;

					for(int i = 0; i < T; ++i){
						System.out.print(s.Map[i] + "\t");
						if( i % W == W - 1) System.out.println("");
					}System.out.println("==========================");
				}
				
				System.out.println("Total counts are: " + count + "\nTotal Steps are:" + resultSize);
				
				ReverseSM(resultSize, solution);
				return solution;
			}
			else{
				for(SmallMap s : GenerateStep()){				
					if(s == null) continue;
					s.preSM = current;
					s.g = current.g + 1;
					s.f = h(s) + s.g;
					open.Insert(s);
				}
			}			
		}
	}
		
	public int getResultSize(){
		return resultSize;
	}
}

class Heap{
	static private int maxSize;
	private SmallMap heap[];
	private int currentSize;
	
	public Heap(){
		maxSize = 30000000;			//bigger can 500000000
		currentSize = 1;
		heap = new SmallMap[maxSize];
	}
	
	public boolean IsEmpty(){
		return currentSize == 1;
	}
	
	public int getCurrentSize(){
		return currentSize;
	}
	
	public void Insert(SmallMap a){		
		if(currentSize == maxSize){
			System.out.println("over maxSize!");
			System.exit(0);
			return;
		}
		
		int i = currentSize++;
		while((i != 1) && (a.f < heap[i / 2].f)){
			heap[i] = heap[i / 2];
			i /= 2;
		}
		heap[i] = a;		
	}
	
	public SmallMap Remove(){
		if(IsEmpty() == true){
			System.out.println("under minSize!");
			System.exit(0);			
		}
		
		SmallMap reItem = heap[1], tmpItem = heap[--currentSize];
		int p = 1, c = 2;		
		while(c <= currentSize){
			if((c < currentSize) && (heap[c].f > heap[c + 1].f))c++;
			if(tmpItem.f <= heap[c].f)break;
			heap[p] = heap[c];
			p = c;
			c *= 2;
		}
		heap[p] = tmpItem;
		return reItem;
	}
}