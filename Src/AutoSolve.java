class AutoSolve{
	private int Rw, Rh, T;
	public int Rsize;
	private int shrinkW, shrinkH, shrinkWTmp, shrinkHTmp;
	private int space;
	private int steps[];
	public int local[], num[];
	
	public AutoSolve(int Width, int Height, int[] in){
		Rw = shrinkW = Width;
		Rh = shrinkH = Height;
		Rsize = 0;
		T = Rw * Rh;
		num = new int[T];
		local = new int[T];
		steps = new int[100000];
		for(int i = 0; i < 100000; ++i)steps[i] = -1;
		
		for(int i = 0; i < T; ++i){
			num[i] = in[i];
			local[num[i] - 1] = i;
			if(num[i] == T)
				space = i;
		}
	}	
	
	public void spaceMoveL(){
		int tmpSpace;
		
		tmpSpace = local[num[space] - 1];
		local[num[space] - 1] = local[num[space - 1] - 1];
		local[num[space - 1] - 1] = tmpSpace;
		
		tmpSpace = num[space];
		num[space] = num[space - 1];
		num[space - 1] = tmpSpace;
		
		steps[Rsize++] = num[space];
		space -= 1;
	}
	public void spaceMoveU(){
		int tmpSpace;
		
		tmpSpace = local[num[space] - 1];
		local[num[space] - 1] = local[num[space - Rw] - 1];
		local[num[space - Rw] - 1] = tmpSpace;
		
		tmpSpace = num[space];
		num[space] = num[space - Rw];
		num[space - Rw] = tmpSpace;
		
		steps[Rsize++] = num[space];
		space -= Rw;
	}	
	public void spaceMoveR(){
		int tmpSpace;
		
		tmpSpace = local[num[space] - 1];
		local[num[space] - 1] = local[num[space + 1] - 1];
		local[num[space + 1] - 1] = tmpSpace;
		
		tmpSpace = num[space];
		num[space] = num[space + 1];
		num[space + 1] = tmpSpace;
		
		steps[Rsize++] = num[space];
		space += 1;
	}
	public void spaceMoveD(){
		int tmpSpace;
		
		tmpSpace = local[num[space] - 1];
		local[num[space] - 1] = local[num[space + Rw] - 1];
		local[num[space + Rw] - 1] = tmpSpace;
		
		tmpSpace = num[space];
		num[space] = num[space + Rw];
		num[space + Rw] = tmpSpace;
		
		steps[Rsize++] = num[space];
		space += Rw;
	}	
	
	public void btnMoveL(int index){
		if(space == local[index - 1] + Rw){
			spaceMoveL();
			spaceMoveU();
		}
		else{
			while(space / Rw != local[index - 1] / Rw){
				if(space / Rw > local[index - 1] / Rw){
					if(space == local[index - 1] + Rw){
						if(space % Rw != Rw - 1 && (index - 1 - shrinkWTmp) % shrinkW != 1)
							spaceMoveR();
						else spaceMoveL();
					}
					if((space / Rw) - 1 == local[index - 1] / Rw &&
						space - Rw + 1 != local[index - 1])
						spaceMoveR();
					spaceMoveU();
				}
				else{
					if(space == local[index - 1] - Rw){
						if(space % Rw != Rw - 1)
							spaceMoveR();
						else spaceMoveL();
					}
					spaceMoveD();
				}
			}
			while(space % Rw != (local[index - 1] % Rw) - 1){
				while(space % Rw > (local[index - 1] % Rw) + 1)
					spaceMoveL();
				if(space % Rw == (local[index - 1] % Rw) + 1){
					if(space / Rw < Rh - 1){
						spaceMoveD();											//Rotate
						spaceMoveL();
						spaceMoveL();
						spaceMoveU();
					}
					else{
						spaceMoveU();
						spaceMoveL();
						spaceMoveL();
						spaceMoveD();
					}
				}
				while(space % Rw < local[index - 1] % Rw - 1)
					spaceMoveR();
			}
		}
		spaceMoveR();
	}
	public void btnMoveU(int index){
		if(local[index - 1] / Rw < space / Rw){
			while(local[index - 1] / Rw + 1 < space / Rw)
				spaceMoveU();
			if(local[index - 1] + Rw == space){
				boolean ruul = (space % Rw != Rw - 1);
				if(ruul)spaceMoveR();
				else spaceMoveL();
				spaceMoveU();
				spaceMoveU();
				if(ruul)spaceMoveL();
				else spaceMoveR();
			}
			else if(local[index - 1] % Rw > space % Rw){
				while((local[index - 1] % Rw) - 1 > space % Rw)
					spaceMoveR();
				if(space % Rw + 2 == Rw){
					spaceMoveU();
					spaceMoveU();
					spaceMoveR();
				}
				else{
					spaceMoveR();
					spaceMoveR();
					spaceMoveU();
					spaceMoveU();
					spaceMoveL();
				}
			}
			else{
				while((local[index - 1] % Rw) + 1 > space % Rw)
					spaceMoveL();
				spaceMoveU();
				spaceMoveU();
				spaceMoveL();
			}
		}
		else if(local[index - 1] / Rw > space / Rw){
			while((local[index - 1] / Rw) - 1 > space / Rw)
				spaceMoveD();
			while(local[index - 1] % Rw > space % Rw)
				spaceMoveR();
			while(local[index - 1] % Rw < space % Rw)
				spaceMoveL();
		}
		else{
			if(local[index - 1] % Rw < space % Rw){
				while ((local[index - 1] % Rw) + 1 < space % Rw)
					spaceMoveL();
				spaceMoveU();
				spaceMoveL();
			}
			else if(local[index - 1] % Rw > space % Rw){
				while((local[index - 1] % Rw) - 1 > space % Rw)
					spaceMoveR();
				if(space / Rw != ((index - 1) / Rw) + 1 || space / Rw == Rh - 1){
					spaceMoveU();
					spaceMoveR();
				}
				else if(space / Rw != Rh - 1){
					spaceMoveD();
					spaceMoveR();
					spaceMoveR();
					spaceMoveU();
					spaceMoveU();
					spaceMoveL();
				}
			}
		}
		spaceMoveD();
	}
	public void btnMoveR(int index){
		while(space / Rw != local[index - 1] / Rw){
			if(space / Rw > local[index - 1] / Rw){
				if(space == local[index - 1] + Rw){
					if(space % Rw != Rw - 1)
						spaceMoveR();
					else spaceMoveL();
				}
				spaceMoveU();
			}
			else{
				if(space == local[index - 1] - Rw){
					if(space % Rw != Rw - 1)
						spaceMoveR();
					else spaceMoveL();
				}
				spaceMoveD();
			}
		}
		while(space % Rw != (local[index - 1] % Rw) + 1){
			if(space % Rw == (index - 1) % Rw){
				if(space % Rw < Rw - 1)
					spaceMoveR();
				else spaceMoveL();
			}
			while(space % Rw < (local[index - 1] % Rw) - 1)
				spaceMoveR();
			if(space % Rw == (local[index - 1] % Rw) - 1){
				if( space / Rw < Rh - 1){
					spaceMoveD();											//Rotate
					spaceMoveR();
					spaceMoveR();
					spaceMoveU();
				}
				else{
					spaceMoveU();
					spaceMoveR();
					spaceMoveR();
					spaceMoveD();
				}
			}
			while(space % Rw > local[index - 1] % Rw + 1)
				spaceMoveL();
		}
		spaceMoveL();
	}
	public void btnMoveD(int index){
		while(space / Rw != (local[index - 1] / Rw) + 1){
			if(space / Rw > (local[index - 1] / Rw) + 1){
				while(space % Rw <= (index - 1) % Rw)
					spaceMoveR();
				spaceMoveU();
			}
			else if(space / Rw == local[index - 1] / Rw)
				spaceMoveD();
			else{
				if(space == local[index - 1] - Rw){
					if (space % Rw != Rw - 1)
						spaceMoveR();
					else spaceMoveL();
				}
				spaceMoveD();
			}
		}
		while(space % Rw != local[index - 1] % Rw){
			if(space % Rw > local[index - 1] % Rw)
				spaceMoveL();
			else if(space % Rw < local[index - 1] % Rw)
				spaceMoveR();
		}
		spaceMoveU();
	}
	
	public void normalV(int index){
		int xDis, yDis;
		while(local[index - 1] != index - 1){			
			xDis = local[index - 1] % Rw - (index - 1) % Rw;
			yDis = local[index - 1] / Rw - (index - 1) / Rw;
			if(xDis == yDis){
				if(((space - (local[index - 1] - 1)) % Rw) < ((space - local[index - 1]) / Rw) + 1)
					btnMoveL(index);
				else btnMoveU(index);
			}
			else if(xDis < yDis && xDis != 0){
				if(xDis > 0) btnMoveL(index);
				else btnMoveR(index);
			}
			else{
				if(yDis > 0) btnMoveU(index);
				else if(yDis < 0) btnMoveD(index);
				else btnMoveL(index);
			}			
		}
	}
	public void specialV(int index){
		int xDis, yDis, cPlus1;
		while(local[index - 1] != index - 1){
			yDis = local[index - 1] / Rw - (index - 1) / Rw;			
			if(yDis == 0) btnMoveL(index);
			else if(yDis < 0) btnMoveD(index);
			else btnMoveU(index);
		}
		if(local[index - 1 + Rw] != index + Rw - 1){
			boolean r = true;
			while(local[index - 1 + Rw] != index + Rw + 1){
				if(local[index - 1 + Rw] == index + Rw - 1)return;
				yDis = local[index - 1 + Rw] / Rw - (index - 1 + Rw) / Rw;	
				if(yDis > 0) btnMoveU(index + Rw);
				else if(yDis == 0){
					xDis = local[index - 1] % Rw;
					cPlus1 = (index + Rw) % Rw;
					if(xDis == cPlus1 && index - 1 + Rw == space)
						spaceMoveR();
					else if(xDis != cPlus1 && index - 1 + Rw != space){
						if(xDis < (index + Rw + 1) % Rw && local[index - 1 + Rw] % Rw != Rw - 1 && r)
							btnMoveR(index + Rw);
						else{
							btnMoveL(index + Rw);
							r = false;
						}
					}
					else btnMoveL(index + Rw);
				}
				else btnMoveD(index + Rw);
			}
			while(space != index + Rw){
				if(space == local[index + Rw - 1] - Rw){
					spaceMoveL();
					spaceMoveD();
				}
				else if(space == local[index + Rw - 1] + 1){
					spaceMoveU();
					spaceMoveL();
					spaceMoveL();
					spaceMoveD();
				}
				else{
					while(space % Rw > local[index + Rw - 1])
						spaceMoveL();
					while(space / Rw < Rh - 1)
						spaceMoveD();
				}
			}
			spaceMoveL();
			spaceMoveU();
			spaceMoveR();
			spaceMoveD();
			spaceMoveR();
			spaceMoveU();
			spaceMoveL();
			spaceMoveL();
			spaceMoveD();
			spaceMoveR();			
		}
	}
	
	public void getVTPos(int index){
		if(index / Rw < Rh - 2) normalV(index);
		else specialV(index);
	}
	public void getHSPos(int index){
		int xDis, yDis, rPlus2;
		while(local[index - 1] != index - 1){
			xDis = local[index - 1] % Rw - (index - 1) % Rw;
			yDis = local[index - 1] / Rw - (index - 1) / Rw;
				
			if(index % Rw > 0 && index % Rw != Rw - 1){					//not last two line
				if(yDis >= 0) normalV(index);
				else{
					if(xDis <= Rw - 2) btnMoveR(index);
					else{
						if(xDis == yDis){
							if((space - local[index - 1] + 1) % Rw < (space - local[index - 1]) / Rw + 1)
								btnMoveR(index);
							else btnMoveU(index);
						}
						else if(xDis < yDis && xDis > 0)
							btnMoveR(index);
						else{
							if(yDis > 0) btnMoveU(index);
							else btnMoveR(index);
						}
					}
				}
			}
			else if(index % Rw == Rw - 1)
				normalV(index);
			else{
				if(xDis == 0){
					if(local[index - 1] - Rw == index / Rw + 1)
						spaceMoveD();
					else if(index - 1 != space && local[index - 1] / Rw != (index - 1) / Rw + 1){
						while(index - 1 + Rw * 2 != local[index - 1]){
							rPlus2 = (index - 1) / Rw + 2;
							if(local[index - 1] / Rw < rPlus2)
								btnMoveD(index);
							else btnMoveU(index);
						}
						if(space != local[index - 1] - Rw){
							while(space % Rw < local[index - 1] % Rw - 1)
								spaceMoveR();
							while(space / Rw > local[index - 1] / Rw &&
								local[index - 1] != space - Rw)
								spaceMoveU();
							while(space / Rw < local[index - 1] / Rw &&
								local[index - 1] != space + Rw)
								spaceMoveD();
							if(space == local[index - 1] - 1){
								spaceMoveU();
								spaceMoveR();
							}
							else{								
								spaceMoveL();
								spaceMoveU();
								spaceMoveU();
								spaceMoveR();
							}
							spaceMoveU();
							spaceMoveL();
							spaceMoveD();
							spaceMoveR();
							spaceMoveD();
							spaceMoveL();
							spaceMoveU();
							spaceMoveU();
							spaceMoveR();
							spaceMoveD();
						}
					}
					else if(local[index - 1] == index - 1 + Rw){
						if(space != index - 1)btnMoveD(index);
						else spaceMoveD();
					}
					else if(local[index - 1] / Rw > (index - 1 + Rw) / Rw)
						btnMoveU(index);
				}
				else{
					while(local[index - 1] != index - 2 + Rw){
						xDis = local[index - 1] % Rw - (index - 2 + Rw) % Rw;
						yDis = local[index - 1] / Rw - (index - 2 + Rw) / Rw;
						if(xDis == yDis){
							if((space - (local[index - 1] + 1)) % Rw < (space - local[index - 1]) / Rw + 1)
								btnMoveR(index);
							else btnMoveU(index);
						}
						else if(xDis < yDis && xDis > 0) btnMoveR(index);
						else{
							if(yDis > 0) btnMoveU(index);
							else btnMoveR(index);
						}
					}
					while(space != index + Rw - 3){
						if(space == index + 2 * Rw - 2){
							spaceMoveL();
							spaceMoveU();							
						}
						else if(space == index + Rw - 1){
							spaceMoveD();
							spaceMoveL();						
							spaceMoveL();
							spaceMoveU();
						}
						else{
							while(space / Rw < (index - 2 + Rw) / Rw)
								spaceMoveD();
							while(space % Rw > (index - 2 + Rw) % Rw + 1)
								spaceMoveL();
						}
					}
					spaceMoveU();
					spaceMoveR();
					spaceMoveD();
					spaceMoveR();
					spaceMoveU();
					spaceMoveL();
					spaceMoveL();
					spaceMoveD();
				}
			}
		}		
	}
	
	public int[] Solve(){
		shrinkHTmp = 0;
		shrinkWTmp = 0;
		int shrinkHStart = 0, shrinkWStart = 0;
		
		while(shrinkH > 3 || shrinkW > 3){
			if(shrinkH > 3){
				for(int i = shrinkHStart; i < shrinkHStart + shrinkW; ++i)
					getHSPos(i + 1);
				shrinkH--;
				shrinkHTmp++;
				shrinkHStart += Rw;
				if(shrinkW > 3)shrinkHStart++;
			}
			if(shrinkW > 3){
				for(int i = shrinkWStart; i < T - shrinkW; i += Rw)
					getVTPos(i + 1);
				shrinkW--;
				shrinkWTmp++;
				shrinkWStart++;
				if(shrinkH > 3)shrinkWStart += Rw;
			}
		}
		int tmp = Rsize;
		Rsize = 0;
		while(steps[Rsize++] != -1);
		if(tmp != Rsize)System.out.println("???");
		
		return steps;
	}
}