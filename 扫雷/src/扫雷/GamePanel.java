package 扫雷;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JPanel;
import javax.swing.BorderFactory;
import javax.swing.JLabel;


public class GamePanel extends JPanel{
		private static final long serialVersionUID = 1L;
		
		private int rows;
		private int cols;
		//界面长宽
		
		private int bombCount;
		//炸弹数量
		
		private final int BLOCKWIDTH = 20;
		private final int BLOCKHEIGHT = 20;
		//方格长度宽度
		
		private JLabel[][] labels;
		private MyButton[][] buttons;
		private final int[][] offset = {{-1, -1}, {0, -1}, {1, -1}, {1, 0}, {1, 1}, {0, 1}, {-1, 1}, {-1, 0}};
		// 存储界面中每一个方格的绘制信息
		
		public GamePanel(int rows, int cols) {
			this.rows = rows;
			this.cols = cols;
			this.bombCount = rows * cols /10;
			this.labels = new JLabel[rows][cols];
			this.buttons = new MyButton[rows][cols];
			this.setLayout(null);
			this.initButtons();
			this.initLabels();//
			
		}
		
		// 绘制边框
		private void initLabels() {
				for (int i = 0; i < this.rows; i++) {
					for (int j=0; j< this.cols; j++) {
						JLabel l = new JLabel("", JLabel.CENTER);
						l.setBounds(j * BLOCKWIDTH, i * BLOCKHEIGHT, BLOCKWIDTH, BLOCKHEIGHT);
						// 方格边界
						
						l.setBorder(BorderFactory.createLineBorder(Color.GRAY));
						// 绘制方格边框
						
						l.setOpaque(true);
						// 设置方格为透明,便于我们填充颜色
						
						l.setBackground(Color.RED);
						// 背景填充为黄色
						
						this.add(l);
						// 将方格存到类变量中,方便公用	
						
						labels[i][j] = l;
					}
				}
				randomBomb();
				writeNumber();
		}
		
		//作出窗口大小，放在一个array里面；index0为长，1为宽
		public int[] returnSize() {
			int []a  = {this.cols * BLOCKWIDTH + 20, this.rows*BLOCKHEIGHT + 40};
			return a;
		}
		
		//生成炸弹
		private void randomBomb() {
			for (int i = 0; i < this.bombCount; i++) {
				//生成随机数表示x坐标
				int rRow = (int) (Math.random() * this.rows);
				//生成随机数表示y坐标
				int rCol = (int) (Math.random() * this.cols);
				// 根据坐标确定JLabel的位置,并显示*
				this.labels[rRow][rCol].setText("*");
				// 设置背景颜色
				this.labels[rRow][rCol].setBackground(Color.DARK_GRAY);
				// 设置*的颜色
				this.labels[rRow][rCol].setForeground(Color.RED);
			}
	
		}
		
		// 初始化按钮，覆盖JLABEL
		private void initButtons() {
			//循环产生每个按钮
			for(int i = 0; i < rows; i++){
				for (int j = 0; j< cols; j++) {
					MyButton btn = new MyButton();
					//设置和每个Label一样的大小
					btn.setBounds(j * BLOCKWIDTH, i * BLOCKHEIGHT, BLOCKWIDTH, BLOCKHEIGHT);
					this.add(btn);
					buttons[i][j] = btn;
					btn.row = i;
					btn.col = j;
					//*************重点*************//
					//给按钮添加监听器，注册点击事件
					// (单机按钮时,将执行内部类ActionListener()中的actionPerformed(ActionEvent e)方法)
					btn.addActionListener(new ActionListener() {
						@Override
						public void actionPerformed(ActionEvent e ) {
							// TODO Auto-generated method stub
							open((MyButton)e.getSource());
						}
					});
				}
				
			}
		}
		
		private boolean verify (int row, int col) {
			return row >= 0 && row < this.rows && col >= 0 && col < this.cols;
		}
		
		// 单机按钮时的重点方法！
		private void open(MyButton btn) {
			//当点击的时候把按钮设置为不可见
			btn.setVisible(false);
			
			//判断中，是否为数字还是空
			switch(labels[btn.row][btn.col].getText()) {
			
				case "*":
					for (int i = 0; i < rows; i++) {
						for (int j = 0; j < cols; j++) {
							buttons[i][j].setVisible(false);
						}
					}
					break;
					
				case "" :
					for (int[] off: offset) {
						int newRow = btn.row + off[0];
						int newCol = btn.col + off[1];
						if (verify(newRow, newCol)) {
							MyButton sButton = buttons[newRow][newCol];
							if (sButton.isVisible()) {
								open(sButton);
							}
						}
					}
				default:
			}
		}
		
		private void writeNumber() {
			for (int  i = 0; i < this.rows; i++) {
				for (int j = 0; j < this.cols; j++) {
					// 如果是炸弹,不标注任何数字
					if (labels[i][j].getText().equals("*")) {
						continue;
					}
					// 如果不是炸弹,遍历它周围的八个方块,将炸弹的总个数标注在这个方格上
					// 方块周围的8个方块中炸弹个数
					int bombCount = 0;
					// 通过偏移量数组循环遍历8个方块
					for (int[] off: offset) {
						int row = i + off[1];
						int col = j + off[0];
						// 判断是否越界,是否为炸弹
						if (verify(row, col) && labels[row][col].getText().equals("*")) {
							bombCount++;
						}
					}
					// 如果炸弹的个数不为0,标注出来
					if (bombCount > 0) {
						labels[i][j].setText(String.valueOf(bombCount));
					}
				}
			}
		}


				
}
