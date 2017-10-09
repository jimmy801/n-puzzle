import java.io.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.filechooser.*;
import javax.swing.JSlider;
import java.util.Hashtable;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.imageio.ImageIO;
import java.awt.image.ImageProducer;
import java.awt.image.CropImageFilter;
import java.awt.image.FilteredImageSource;

public class puzzle extends JFrame {
	
	JPanel bigp; // ��Ӥjpanel
	JPanel RP; // �k�b��̤jpanel �]��p2,sp
	JPanel p2; // �˦�over lb, reset btn(�p�G�����۰ʫ���A���Ӥ]�|��i�h)
	JPanel p; // �˦��Ҧ��i����btn��panel
	JPanel sp; //�ˤp�ϥ�
	
	JButton[]btn; // �Ҧ���l
	JButton[]small; // �p�ϥ���ܵP�����G������

	JButton auto; // �۰ʸ�
	JButton pause; // �Ȱ�/�}�l��
	JButton reset; // ���]��
	
	JLabel over; // ��ܹC���̫ᵲ�G��label
	JLabel step_name;
	JLabel steps;// ��ܨB��
	JLabel time_name;
	JLabel times; // ��ܮɶ�
	
	JRadioButton displaypic; // �n���n��ܹϤ�
	
	JSlider speed;
	
	ImageIcon[]pic; // �Ҧ�btn�̪��Ϥ�
	ImageIcon[]smallpic; // �p�Ϫ��Ϥ�
	
	boolean isPause = false; // ����Ȱ�
	boolean end = false; // �C�������S
	boolean setinimg = false; // ���S��Ū�϶i�t��
	boolean a = false;

	int[]num; // ������i��btn�����Ʀr
	int[]loca; // loca[i]�N��Ʀri+1�bbtn[loca[i]]
	int[]ways = new int[4]; // ways[0]~[3]�N���V���O�O�W �U �� �k
							// ways[0] = i �N��i�H���W�����Obtn[i]
							// -1�N��S���i�H���Ӥ�V���ʪ�btn

	public  int Width = 3, Height = 3, total; // ���O�N��� �e �`�Ʀ��X��
	public  int h, w, rate, sW ,sH;
	public  int space;  // �����{�b�Ů�Obtn[i]��i
	int step = 0; // �����B��
	int time = 0; // �����ɶ�
	int default_btn_w, default_btn_h; // �w�]�Ʀr�ɪ�btn���e
	int default_sbtn_w, default_sbtn_h; // �w�]�Ʀr�ɪ��p�Ϫ��e
	int FPS_INIT = 100, FPS_MIN = 50, FPS_MAX = 150;
	
	GridBagLayout g = new GridBagLayout();
	GridBagConstraints gbc = new GridBagConstraints();

	private  Timer timer = new Timer(100, new  ActionListener(){ // �p�ɾ�
		public  void  actionPerformed(ActionEvent  evn){
			if (end)timer.stop();
			time++;
			times.setText(String.valueOf((float)time / 10));
			if(time % 100 == 0)screen_modify();
		}
	});

	private int checkWin(){ // �P�_�ӧQ�S
		int tie = 0; // �O�_����
		for(int i = 0; i < total; ++i){
			if((i + 1) % Width != 0 && i + 2 == num[i] && i + 1 == num[i + 1] && i + 2 != total){tie++;i++;continue;}
			else if(num[i] != i + 1)return 0;
		}
		if(tie > 1)return 0;
		else if(tie == 1) return 1;
		return 2;
	}

	private  void EndThings(int result){ // �C������������
		if (result == 0){ 
			btn[space].setIcon(null); 
			return; 
		}
		else if (result == 1)over.setText("�L��");
		else over.setText("����");
		for (int i = 0; i < total; ++i){
			btn[i].setEnabled(false);
			if (setinimg && displaypic.isSelected()){
				btn[i].setIcon(pic[i]);
				if (result == 2)btn[i].setDisabledIcon(pic[i]);
				else btn[i].setDisabledIcon(null);
			}
			else btn[i].setText(String.valueOf(num[i]));
			if(result == 2)btn[i].setBackground(Color.YELLOW);
			//btn[i].setBorderPainted(false);
		}
			
		end = true;
		pause.setEnabled(false);
		auto.setEnabled(false);
		speed.setEnabled(false);
		speed.setVisible(false);
		reset.requestFocus();
	}

	private void btn_set_num(){ // ����ܹϤ����٭�Ʀr
		for (int i = 0; i < total; ++i){
			small[i].setIcon(null);
			small[i].setDisabledIcon(null);
			small[i].setText(String.valueOf(i + 1));
			btn[i].setIcon(null);
			btn[i].setText(String.valueOf(num[i]));
		}
		btn[space].setText("");
		btn[space].setDisabledIcon(null);
		p.setPreferredSize(new Dimension(default_btn_w, default_btn_h ));
		sp.setPreferredSize(new Dimension(default_sbtn_w , default_sbtn_h));
		EndThings(checkWin());
		screen_modify();
	}

	private  void btn_set_pic(){ // ����ܹϤ����٭�Ϥ�
		if (displaypic.isSelected() && setinimg){
			for (int i = 0; i < total; ++i){
				small[i].setIcon(smallpic[i]);
				small[i].setDisabledIcon(smallpic[i]);
				small[i].setText("");
				if (i == loca[total - 1]){ btn[i].setText(""); btn[i].setIcon(null); btn[i].setDisabledIcon(null);/*continue;*/ }
				btn[loca[i]].setIcon(pic[i]);
				btn[loca[i]].setText("");
			}
			EndThings(checkWin());

			p.setPreferredSize(new Dimension(w * Width, h * Height));
			sp.setPreferredSize(new Dimension(w * Width / rate, h * Height / rate));
			screen_modify();
		}
	}

	private  void SetTheImage(String path){ // �]�w+���ιϤ�
		if (end)Reset();
		if(step == 0)time = 0;
		timer.stop();
		try{
			ImageIcon icon = new ImageIcon(path);
			int icW = icon.getIconWidth(), icH = icon.getIconHeight();
			int tempW = icW, tempH = icH;
			rate = 3;
			for (int i = 100; i > 0; --i){
				if (tempW * (float)(1 + 1 / rate) > sW || tempH * (float)(1 + 1 / rate) > sH){ tempW = icW*i / 10; tempH = icH*i / 10; }
				else break;
			}
			Image tempImg = icon.getImage().getScaledInstance(tempW,
				tempH, icon.getImage().SCALE_DEFAULT);
			w = tempW / Width; h = tempH / Height;
			int shrink_x = tempW % Width, shrink_y = tempH % Height;
			for (int i = 0; i < total; ++i){
				icon = new ImageIcon(tempImg);
				ImageProducer imageproducer = icon.getImage().getSource();
				CropImageFilter cropimagefilter = new CropImageFilter(shrink_x, shrink_y, w, h);
				icon = new ImageIcon(Toolkit.getDefaultToolkit().createImage(new FilteredImageSource(imageproducer, cropimagefilter)));
				pic[i] = icon;
				smallpic[i] = new ImageIcon(icon.getImage().getScaledInstance(w / rate,
					h / rate, icon.getImage().SCALE_DEFAULT));
				if (i % Width == Width - 1){ shrink_y += h; shrink_x = tempW % Width; }
				else shrink_x += w;
			}
			setinimg = true;
			displaypic.setEnabled(true);
			btn_set_pic();
			if(!isPause)timer.start();
		}
		catch (Exception e){
			javax.swing.JOptionPane.showMessageDialog(null, "���J���ɿ��~");
		}
	}

	private void changeBtns(int n){ // ���Ubtn��A�Υ洫2��btn���e�F�첾�ʮĪG
		if (!end && !isPause){
			step++;
			steps.setText(String.valueOf(step));
			if (setinimg && displaypic.isSelected()){
				btn[space].setIcon((ImageIcon)btn[n].getIcon());
				btn[n].setIcon(null);
			}
			else{
				btn[space].setText(btn[n].getText());
				btn[n].setText("");
			}
			loca[num[n] - 1] = space;
			loca[total - 1] = n;
			btn[space].setEnabled(true);
			btn[space].requestFocus();
			num[space] = num[n];
			num[n] = total;
			space = n;
			btn[space].setEnabled(false);
			ways[0] = space / Width < Height - 1 ? space + Width : -1;
			ways[1] = space / Width > 0 ? space - Width : -1;
			ways[2] = (space + 1) % Width != 0 ? space + 1 : -1;
			ways[3] = space % Width != 0 ? space - 1 : -1;
			EndThings(checkWin());
		}
		screen_modify();
	}

	private void btnCheck(int n){ // �T�{�ӫ��s�O�_���F����
		if (end)return;
		for (int i = 0; i < 4; ++i){ // �p�G�L���b4�Ӥ�V���~�i��
			if (ways[i] == n){
				changeBtns(n);
				return;
			}
		}
	}

	private void Reset(){ // ���U���s�}�l��btn
		time = 0;
		for (int i = 0; i < total; ++i){
			boolean b = false;
			int r = (int)(total * Math.random() + 1);
			num[i] = r;
			for (int j = 0; j < i; ++j){
				if (num[j] == num[i]){
					i--; b = true; break;
				}
			}
			if (b)continue;
			loca[r - 1] = i;
			if (!displaypic.isEnabled() || !displaypic.isSelected())
				btn[i].setText(String.valueOf(num[i]));
			else { btn[i].setIcon(pic[r - 1]); btn[i].setText(""); }
			
			btn[i].setEnabled(true);
			btn[i].setBackground(null);
			btn[i].setDisabledIcon(null);
			
			if(r == total){
				space = i;
				btn[i].setText("");
				btn[i].setEnabled(false);
				btn[i].setIcon(null);
				btn[i].setBackground(null);
				btn[i].setEnabled(false);
				ways[0] = space / Width < Height - 1 ? space + Width : -1;
				ways[1] = space / Width > 0 ? space - Width : -1;
				ways[2] = (space + 1) % Width != 0 ? space + 1 : -1;
				ways[3] = space % Width != 0 ? space - 1 : -1;
			}
			//btn[i].setBorderPainted(true); // �p�G�n��ιϤ��Ƭݭn���n��btn��ɨ���
		}
		over.setText(" ");
		steps.setText("0");
		times.setText("0");
		end = false;
		isPause = false;
		pause.setEnabled(true);
		pause.setText("�Ȱ�");
		auto.setEnabled(true);
		a = false;
		step = 0;
		speed.setValue(FPS_INIT);
		speed.setEnabled(false);
		speed.setVisible(false);
		time = 0;
		timer.start();
	}

	private void SetSize(){ // �ϥβĤG�ӵ����ΨӨM�w�n���X���X��
		Form2 frm2 = new Form2(this);
		int tl = Width, tw = Height;
		if (Width < 2 || Height < 2){ Width = tl > 1 ? tl : 3; Height = tw > 1 ? tw : 3; }
		total = Width * Height;
		setinimg = false;
	}
	
	private void pause_or_play(){ // ���Upause��
		if(!end && !a){
			if (!isPause){
				timer.stop();
				for (int i = 0; i < total; ++i){
					if (i == space)continue;
					btn[i].setEnabled(false);
				}
				isPause = true;
				auto.setEnabled(false);
				pause.setText("�~��");
			}
			else{
				timer.start();
				for (int i = 0; i < total; ++i){
					if (i == space)continue;
					btn[i].setEnabled(true);
				}
				isPause = false;
				auto.setEnabled(true);
				pause.setText("�Ȱ�");
			}
		}
	}

	private void screen_modify(){ // �վ�ù��j�p
		puzzle.this.pack();
		if(!setinimg || !displaypic.isSelected()){
			int FrameWidth = puzzle.this.getWidth(), FrameHeight = puzzle.this.getHeight();
			if (sW < FrameWidth){ FrameWidth = sW; puzzle.this.setSize(new Dimension(FrameWidth, FrameHeight)); }
			if (sH < FrameHeight){ FrameHeight = sH; puzzle.this.setSize(new Dimension(FrameWidth, FrameHeight)); }
		}
	}
	
	private void init_btn(){
		num = new int[total]; // �C��Ʀr
		loca = new int[total]; // �C��Ʀr
		btn = new JButton[total]; // �Ʀr�s
		small = new JButton[total]; // �p��
		pic = new ImageIcon[total];
		smallpic = new ImageIcon[total];
		int font_size = 22;
		for (int i = 0; i < total; ++i){
			boolean b = false;
			int r = (int)(total * Math.random() + 1);
			num[i] = r;
			for (int j = 0; j < i; ++j){ // �קK�H���Ʀr����
				if (num[j] == num[i]){
					i--; b = true; break;
				}
			}
			if (b)continue; // �����Ƥ����U��
			loca[r - 1] = i;
			small[i] = new JButton(String.valueOf(i + 1));
			small[i].setEnabled(false); // �p�ϥܬҤ��i��
			sp.add(small[i]);
			if(r != total)btn[i] = new JButton(String.valueOf(num[i])); // ���H���Ʀr�]��btn���r
			else {
				btn[i] = new JButton("");
				btn[i].setEnabled(false);
				space = i;
				ways[0] = space / Width < Height - 1 ? space + Width : -1;
				ways[1] = space / Width > 0 ? space - Width : -1;
				ways[2] = (space + 1) % Width != 0 ? space + 1 : -1;
				ways[3] = space % Width != 0 ? space - 1 : -1;
			}
			btn[i].setFont(new Font("Arial", Font.BOLD, font_size));
			btn[i].enableInputMethods(false); 

			final int n = i; // �n�D�q�lfun�I�s��fun������final���A
			btn[i].addMouseListener(new MouseAdapter(){ // ���UbtnĲ�o
				public void mousePressed(MouseEvent e) {
					if(!a)btnCheck(n); // �T�{��btn�Y��Q���|���K����
				}
			});
			btn[i].addKeyListener(new KeyMoniter()); // ���U��V��Ĳ�o
			p.add(btn[i]); // �ƪ���
		}

		// �ƪ���
		p.setLayout(new GridLayout(Height, Width));
		p2.setLayout(g);
		sp.setLayout(new GridLayout(Height, Width));
		bigp.add(p, FlowLayout.LEFT);

		gbc.gridx = 0; gbc.gridy = 0;
		gbc.gridwidth = 2;
		gbc.insets = new Insets(0, 0, 5, 0); // ���O�O�W���U�k�ҭn���}���ƶq
		g.setConstraints(over, gbc);
		p2.add(over);

		gbc.gridx = 0; gbc.gridy = 1;
		gbc.gridwidth = 1;
		g.setConstraints(step_name, gbc);
		p2.add(step_name);
		gbc.gridx = 1; gbc.gridy = 1;
		gbc.gridwidth = 1;
		g.setConstraints(steps, gbc);
		p2.add(steps);

		gbc.gridx = 0; gbc.gridy = 2;
		gbc.gridwidth = 1;
		g.setConstraints(time_name, gbc);
		p2.add(time_name);
		gbc.gridx = 1; gbc.gridy = 2;
		gbc.gridwidth = 1;
		g.setConstraints(times, gbc);
		p2.add(times);

		gbc.gridx = 0; gbc.gridy = 3;
		gbc.gridwidth = 1;
		g.setConstraints(displaypic, gbc);
		p2.add(displaypic);

		gbc.gridx = 0; gbc.gridy = 4;
		gbc.gridwidth = 2;
		g.setConstraints(pause, gbc);
		p2.add(pause);
		
		gbc.gridx = 0; gbc.gridy = 5;
		gbc.gridwidth = 2;
		g.setConstraints(auto, gbc);
		p2.add(auto);

		gbc.gridx = 0; gbc.gridy = 6;
		gbc.gridwidth = 2;
		g.setConstraints(speed, gbc);
		p2.add(speed);

		gbc.gridx = 0; gbc.gridy = 7;
		gbc.gridwidth = 2;
		g.setConstraints(reset, gbc);
		p2.add(reset);

		gbc.gridx = 0; gbc.gridy = 0;
		gbc.insets = new Insets(5, 0, 25, 0); // ���O�O�W���U�k�ҭn���}���ƶq
		g.setConstraints(p2, gbc);
		RP.add(p2);

		JScrollPane jScrollPane = new JScrollPane(bigp);
		jScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
		jScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);

		gbc.gridx = 0; gbc.gridy = 2;
		gbc.insets = new Insets(5, 0, 0, 0);
		g.setConstraints(sp, gbc);
		RP.add(sp);
		bigp.add(RP);
		add(jScrollPane);
	}
	
	void open_Dialog(){ // �}�ҹϤ�
		JFileChooser jfc = new JFileChooser();
		jfc.setCurrentDirectory(new File(System.getProperty("user.home") + System.getProperty("file.separator") + "Pictures"));
		jfc.setFileFilter(new FileNameExtensionFilter(
			"Images", "jpg", "gif", "png", "bmp"));
		int n = jfc.showOpenDialog(puzzle.this);
		if (n == JFileChooser.APPROVE_OPTION) {
			File file = jfc.getSelectedFile();
			SetTheImage(file.getAbsolutePath());
			pack();
		}
	}
	
	void ResetSize_detail(){
		puzzle.this.getContentPane().removeAll();
		Init();
		Reset();
		puzzle.this.getContentPane().revalidate();
		puzzle.this.pack();
		if(!setinimg || !displaypic.isSelected())screen_modify();
		reset.requestFocus();
		default_btn_w = p.getWidth(); default_btn_h = p.getHeight();
		default_sbtn_w = sp.getWidth(); default_sbtn_h = sp.getHeight();
	}
	
	private void Init(){ // ��l�ŧi�A���MReset function�g�@�_��]�O�]��
						// ���ǪF��n��l�A�ӳo�̪�l�L��Reset�N���ݭn�b��l�@���F

		Dimension screenSize = java.awt.Toolkit.getDefaultToolkit().getScreenSize();	// ���o�ù��ؤo
		sW = (int)screenSize.getWidth() - 80; sH = (int)screenSize.getHeight() - 80;

		isPause = false;

		p = new JPanel(); // �˦��Ҧ��i����btn��panel
		sp = new JPanel(); //�ˤp�ϥ�
		RP = new JPanel(null); // �k�b��̤jpanel �]��p2,sp
		p2 = new JPanel(); // �˦�over lb, reset btn(�p�G�����۰ʫ���A���Ӥ]�|��i�h)
		bigp = new JPanel();

		reset = new JButton("���s�}�l"); // ���]��
		pause = new JButton("�Ȱ�");
		auto = new JButton("�۰ʸ��D");

		over = new JLabel(" "); // ��ܹC���̫ᵲ�G��label
		step_name = new JLabel("�ϥΨB��: ");
		steps = new JLabel("0");//��ܨB��
		time_name = new JLabel("�ϥήɶ�: ");
		times = new JLabel("0"); // ��ܮɶ�
		
		displaypic = new JRadioButton("��ܹϤ�", true);

		displaypic.setEnabled(false);
		reset.enableInputMethods(false); 
		displaypic.enableInputMethods(false); 
		pause.enableInputMethods(false); 

		JMenuBar jmb = new JMenuBar();
		setJMenuBar(jmb);  // �s�W�U�Ԧ��\���
		JMenu file = new JMenu("�ɮ�(F)"); // �Ĥ@�ӿ��
		file.setMnemonic(KeyEvent.VK_F);
		JMenuItem item;
		file.add(item = new JMenuItem("�}��(O) ctrl + O", KeyEvent.VK_O));
		item.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent evt) {
				if(!a)open_Dialog();
			} });
		file.add(item = new JMenuItem("���](R) ctrl + R", KeyEvent.VK_S));
		item.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent evt) {
				if(!a)ResetSize_detail();
			}
		});
		file.addSeparator();  // ���j�u
		file.add(item = new JMenuItem("����(X) ctrl +X", KeyEvent.VK_X));
		item.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent evt) {
				dispose();System.exit(0);
		} });
		jmb.add(file); // �s�Wfile���
		
		speed = new JSlider(FPS_MIN, FPS_MAX, FPS_INIT);
		speed.setVisible(false);
		speed.setEnabled(false);
		speed.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent event) {
				T.setDelay(FPS_MAX + FPS_MIN - speed.getValue());
			}
		});
		Hashtable<Integer, JLabel> labelTable = new Hashtable<Integer, JLabel>();
		labelTable.put( new Integer( FPS_MIN ), new JLabel("Slow") );
		labelTable.put( new Integer( FPS_MAX ), new JLabel("Fast") );
		speed.setLabelTable( labelTable );
		speed.setPaintLabels(true);
		speed.addKeyListener(new KeyMoniter());
		
		// �ƪ���
		g = new GridBagLayout();
		gbc = new GridBagConstraints();
		RP.setLayout(g);
		gbc.fill = GridBagConstraints.VERTICAL;
		
		SetSize(); // ���]�w�n���X���X��
		
		init_btn();

		timer.start();
		
		displaypic.addKeyListener(new KeyMoniter());
		displaypic.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				if (displaypic.isSelected() && displaypic.isEnabled())btn_set_pic();
				else { btn_set_num(); pack(); }
			}
		});

		pause.addKeyListener(new KeyMoniter());
		pause.addMouseListener(new MouseAdapter(){
			public void mousePressed(MouseEvent e) {
				if(!a)pause_or_play();
			}
		});

		auto.addKeyListener(new KeyMoniter());
		auto.addMouseListener(new MouseAdapter(){
			public void mousePressed(MouseEvent e) {
				if(!a)solve();
			}
		});
		
		// reset btn �[�J��L�A�ƹ��ƥ�
		reset.addKeyListener(new KeyMoniter());
		reset.addMouseListener(new MouseAdapter(){
			public void mousePressed(MouseEvent e) {
				if(!a)Reset();
			}
		});
	}

	public puzzle(){
		super("N-Puzzle");
		Init();
		timer.setInitialDelay(0);
		setVisible(true);
		pack();
		
		default_btn_w = p.getWidth(); default_btn_h = p.getHeight();
		default_sbtn_w = sp.getWidth(); default_sbtn_h = sp.getHeight();
		
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		addWindowListener(new WindowAdapter(){
			public void windowClosing(WindowEvent event) {
				timer.stop();
				puzzle.this.dispose();
				System.exit(0);
			} });
		setResizable(false);
	}
	
	Timer T;
	Astar auto_result;
	AutoSolve first;
	SmallMap[] ans;
	boolean needF;
	int[] ca;
	
	void solve(){
		if(!end && !a && !isPause){
			speed.setVisible(true);
			speed.setEnabled(true);
			speed.setPreferredSize(new Dimension(auto.getWidth(), auto.getHeight()));
			screen_modify();
			a = true;
			auto.setEnabled(false);
			pause.setEnabled(false);
			reset.setEnabled(false);
			auto_result = new Astar(Width, Height, num);
			ans = auto_result.AstarResult();
				
			needF = false;
			if(ans == null && auto_result.count == 0){ // �L��
				EndThings(1); 
				a = false; 
				reset.setEnabled(true);
				return;
			}
			else if(ans == null && auto_result.count != 0){
				System.out.println("Catch Request!");
				needF = true;
			}			
			if(needF){
				first = new AutoSolve(Width, Height, num);
				ca = first.Solve();
				
				for(int i = 0; i < total; ++i){
					System.out.print(first.num[i] + "\t");
					if( i % Width == Width - 1) System.out.println("");
				}System.out.println("==========================");
					
				auto_result = new Astar(Width, Height, first.num);
				ans = auto_result.AstarResult();
				System.out.println(first.Rsize);
			}
			
			ActionListener act = new ActionListener(){
				int index = 1;
				boolean aC = false;
				public void actionPerformed(ActionEvent e){
					if(needF == false){
						changeBtns(loca[ans[index++].moveBtn - 1]);
						if(index == auto_result.getResultSize())
						{T.stop(); a = false; reset.setEnabled(true);}
					}
					else{						
						if(aC == false){
							changeBtns(loca[ca[index - 1] - 1]);
							index++;
							if(index == first.Rsize){
								aC = true;
								index = 1;
							}
						}
						else{
							changeBtns(loca[ans[index++].moveBtn - 1]);
							if(index == auto_result.getResultSize())
							{T.stop(); a = false; reset.setEnabled(true);}	
						}						
					}
				}
			};
			T = new Timer(FPS_INIT, act);
			T.start();
		}
	}
	
	public static void main(String args[]) {
		SwingUtilities.invokeLater(new Runnable(){
			public void run(){
				new puzzle();
			}
		});
	}

	public class KeyMoniter extends KeyAdapter{
		public void keyPressed(KeyEvent e){
			int keyCode = e.getKeyCode();
			switch (keyCode){
			// ����V����T�{�Ӥ�V�i���ʦA��
			case KeyEvent.VK_UP:
				if (ways[0] >= 0 && !a)changeBtns(ways[0]);
				else if(a)speed.setValue(speed.getValue() + 10);
				break;
			case KeyEvent.VK_DOWN:
				if (ways[1] >= 0 && !a)changeBtns(ways[1]);
				else if(a)speed.setValue(speed.getValue() - 10);
				break;
			case KeyEvent.VK_LEFT:
				if (ways[2] >= 0 && !a)changeBtns(ways[2]);
				else if(a)speed.setValue(speed.getValue() - 10);
				break;
			case KeyEvent.VK_RIGHT:
				if (ways[3] >= 0 && !a)changeBtns(ways[3]);
				else if(a)speed.setValue(speed.getValue() + 10);
				break;
			// ��R���s�}�l
			case KeyEvent.VK_R:
				if(((e.getModifiers() & KeyEvent.CTRL_MASK) != 0) && !a)ResetSize_detail(); // ctrl + R���]�j�p
				else if(!a) Reset();
				break;
			// ��P �}�l / �~��
			case KeyEvent.VK_P:
				if(!a)pause_or_play();
				break;
			// �� D ��� / ����� �Ϥ�
			case KeyEvent.VK_D:
				if (displaypic.isEnabled())
				{
					displaypic.setSelected(!displaypic.isSelected());
					if (displaypic.isSelected())btn_set_pic();
					else btn_set_num();
				}
				break;
			// �� A �۰ʸ�
			case KeyEvent.VK_A:
				if(!a)solve();
				break;
			// �� ctrl + O �}�ҹϤ�
			case KeyEvent.VK_O:
				if(((e.getModifiers() & KeyEvent.CTRL_MASK) != 0) && !a)open_Dialog();
				break;
			// �� ctrl + X ����
			case KeyEvent.VK_X:
				if((e.getModifiers() & KeyEvent.CTRL_MASK) != 0)System.exit(1);
				break;
			}
		}
		public void keyReleased(KeyEvent e) {}
		public void keyTyped(KeyEvent e) {}
	}
}