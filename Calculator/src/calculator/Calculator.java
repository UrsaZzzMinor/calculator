package calculator;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JPanel;
import javax.swing.LayoutStyle.ComponentPlacement;
import java.awt.GridBagLayout;
import javax.swing.JTextField;
import javax.swing.JButton;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.event.ActionListener;
import java.math.BigDecimal;
import java.awt.event.ActionEvent;
import javax.swing.SwingConstants;

public class Calculator {
	
	
	public void clickBut(String s){
		textFieldExpression.setText(textFieldExpression.getText()+s);
	}
	public static Res eval(final String str) {
	    return new Object() {
	        int pos = -1;
	        Character ch;
	        String s ="";

	        void nextChar() {
	            ch = (char) ((++pos < str.length()) ? str.charAt(pos) : -1);
	        }

	        boolean eat(int charToEat) {
	            while (ch == ' ') nextChar();
	            if (ch == charToEat) {
	                nextChar();
	                return true;
	            }
	            return false;
	        }

	        Res parse() {
	            nextChar();
	            double x = parseExpression();
	            if (pos < str.length()) s = "Unexpected: " + ch;
	            return new Res(x, s);
	        }

	        // Grammar:
	        // expression = term | expression `+` term | expression `-` term
	        // term = factor | term `*` factor | term `/` factor
	        // factor = `+` factor | `-` factor | `(` expression `)`
	        //        | number | functionName factor | factor `^` factor

	        double parseExpression() {
	            double x = parseTerm();
	            for (;;) {
	                if      (eat('+')) x = BigDecimal.valueOf(x).add(BigDecimal.valueOf(parseTerm())).doubleValue(); // addition
	                else if (eat('-')) x -= parseTerm(); // subtraction
	                else return x;
	            }
	        }

	        double parseTerm() {
	            double x = parseFactor();
	            if ((x>2147483647) || (x<-2147483647)){
	            	s = "Out of Bounds";
	            }
	            for (;;) {
	                if      (eat('*')) x = BigDecimal.valueOf(x).multiply(BigDecimal.valueOf(parseTerm())).doubleValue(); // multiplication
	                else if (eat('/')) {
	                	double x1=parseFactor();
	                	if (x1!=0){
	                		x /= x1;
	                	}else { 
	                		s = "Division by zero";
	                	}
	                }
	                else return x;
	            }
	        }

	        double parseFactor() {
	            if (eat('+')) return parseFactor(); // unary plus
	            if (eat('-')) return -parseFactor(); // unary minus
	            

	            double x;
	            int startPos = this.pos;
	            if (eat('(')) { // parentheses
	                x = parseExpression();
	                eat(')');
	            } else if ((ch >= '0' && ch <= '9') || ch == '.') { // numbers
	                while ((ch >= '0' && ch <= '9') || ch == '.') nextChar();
	                x = Double.parseDouble(str.substring(startPos, this.pos));
	            } else if (ch >= 'a' && ch <= 'z') { // functions
	                while (ch >= 'a' && ch <= 'z') nextChar();
	                String func = str.substring(startPos, this.pos);
	                x = parseFactor();
	                if (func.equals("sqrt")) x = Math.sqrt(x);
	                else if (func.equals("sin")) x = Math.sin(Math.toRadians(x));
	                else if (func.equals("cos")) x = Math.cos(Math.toRadians(x));
	                else if (func.equals("tan")) x = Math.tan(Math.toRadians(x));
	                else if (func.equals("abs")) x = Math.abs(x);
	                else if (func.equals("sqr")) x = Math.pow(x, 2);
	                else s = "Unknown function: " + func;
	            } else {
	            	s = "Unexpected: " + ch;
	            	x = 0;
	            }

	            return x;
	        }
	    }.parse();
	}
	

	private JFrame frame;
	private JTextField textFieldExpression;
	private JTextField textFieldResult;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Calculator window = new Calculator();
					window.frame.setVisible(true);
					window.frame.setLocationRelativeTo(null);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public Calculator() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 500, 300);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		JPanel panelText = new JPanel();
		
		JPanel panelButtons = new JPanel();
		GroupLayout groupLayout = new GroupLayout(frame.getContentPane());
		groupLayout.setHorizontalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(Alignment.TRAILING, groupLayout.createSequentialGroup()
					.addContainerGap()
					.addGroup(groupLayout.createParallelGroup(Alignment.TRAILING)
						.addComponent(panelButtons, Alignment.LEADING, GroupLayout.PREFERRED_SIZE, 464, Short.MAX_VALUE)
						.addComponent(panelText, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, 464, Short.MAX_VALUE))
					.addContainerGap())
		);
		groupLayout.setVerticalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup()
					.addComponent(panelText, GroupLayout.PREFERRED_SIZE, 89, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(panelButtons, GroupLayout.PREFERRED_SIZE, 152, GroupLayout.PREFERRED_SIZE)
					.addContainerGap(215, Short.MAX_VALUE))
		);
		
		textFieldExpression = new JTextField();
		textFieldExpression.setHorizontalAlignment(SwingConstants.RIGHT);
		textFieldExpression.setColumns(10);
		
		textFieldResult = new JTextField();
		textFieldResult.setHorizontalAlignment(SwingConstants.RIGHT);
		textFieldResult.setColumns(10);
		GroupLayout gl_panelText = new GroupLayout(panelText);
		gl_panelText.setHorizontalGroup(
			gl_panelText.createParallelGroup(Alignment.LEADING)
				.addComponent(textFieldExpression, Alignment.TRAILING, GroupLayout.DEFAULT_SIZE, 423, Short.MAX_VALUE)
				.addComponent(textFieldResult, GroupLayout.DEFAULT_SIZE, 423, Short.MAX_VALUE)
		);
		gl_panelText.setVerticalGroup(
			gl_panelText.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panelText.createSequentialGroup()
					.addComponent(textFieldExpression, GroupLayout.PREFERRED_SIZE, 42, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(textFieldResult, GroupLayout.DEFAULT_SIZE, 41, Short.MAX_VALUE))
		);
		panelText.setLayout(gl_panelText);
		GridBagLayout gbl_panelButtons = new GridBagLayout();
		gbl_panelButtons.columnWidths = new int[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
		gbl_panelButtons.rowHeights = new int[]{0, 0, 0, 0, 0, 0};
		gbl_panelButtons.columnWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
		gbl_panelButtons.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
		panelButtons.setLayout(gbl_panelButtons);
		
		JButton buttonOne = new JButton("1");
		buttonOne.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				clickBut("1");
			}
		});
		GridBagConstraints gbc_buttonOne = new GridBagConstraints();
		gbc_buttonOne.insets = new Insets(0, 0, 5, 5);
		gbc_buttonOne.gridx = 1;
		gbc_buttonOne.gridy = 1;
		panelButtons.add(buttonOne, gbc_buttonOne);
		
		JButton buttonTwo = new JButton("2");
		buttonTwo.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				clickBut("2");
			}
		});
		GridBagConstraints gbc_buttonTwo = new GridBagConstraints();
		gbc_buttonTwo.insets = new Insets(0, 0, 5, 5);
		gbc_buttonTwo.gridx = 2;
		gbc_buttonTwo.gridy = 1;
		panelButtons.add(buttonTwo, gbc_buttonTwo);
		
		JButton buttonThree = new JButton("3");
		buttonThree.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				clickBut("3");
			}
		});
		GridBagConstraints gbc_buttonThree = new GridBagConstraints();
		gbc_buttonThree.insets = new Insets(0, 0, 5, 5);
		gbc_buttonThree.gridx = 3;
		gbc_buttonThree.gridy = 1;
		panelButtons.add(buttonThree, gbc_buttonThree);
		
		JButton buttonFour = new JButton("4");
		buttonFour.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				clickBut("4");
			}
		});
		GridBagConstraints gbc_buttonFour = new GridBagConstraints();
		gbc_buttonFour.insets = new Insets(0, 0, 5, 5);
		gbc_buttonFour.gridx = 4;
		gbc_buttonFour.gridy = 1;
		panelButtons.add(buttonFour, gbc_buttonFour);
		
		JButton buttonOpBracket = new JButton("(");
		buttonOpBracket.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				clickBut("(");
			}
		});
		GridBagConstraints gbc_buttonOpBracket = new GridBagConstraints();
		gbc_buttonOpBracket.insets = new Insets(0, 0, 5, 5);
		gbc_buttonOpBracket.gridx = 6;
		gbc_buttonOpBracket.gridy = 1;
		panelButtons.add(buttonOpBracket, gbc_buttonOpBracket);
		
		JButton buttonClBracket = new JButton(")");
		buttonClBracket.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				clickBut(")");
			}
		});
		GridBagConstraints gbc_buttonClBracket = new GridBagConstraints();
		gbc_buttonClBracket.insets = new Insets(0, 0, 5, 5);
		gbc_buttonClBracket.gridx = 7;
		gbc_buttonClBracket.gridy = 1;
		panelButtons.add(buttonClBracket, gbc_buttonClBracket);
		
		JButton buttonFive = new JButton("5");
		buttonFive.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				clickBut("5");
			}
		});
		
		JButton buttonSin = new JButton("sin");
		buttonSin.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				clickBut("sin");
			}
		});
		GridBagConstraints gbc_buttonSin = new GridBagConstraints();
		gbc_buttonSin.insets = new Insets(0, 0, 5, 5);
		gbc_buttonSin.gridx = 8;
		gbc_buttonSin.gridy = 1;
		panelButtons.add(buttonSin, gbc_buttonSin);
		GridBagConstraints gbc_buttonFive = new GridBagConstraints();
		gbc_buttonFive.insets = new Insets(0, 0, 5, 5);
		gbc_buttonFive.gridx = 1;
		gbc_buttonFive.gridy = 2;
		panelButtons.add(buttonFive, gbc_buttonFive);
		
		JButton buttonSix = new JButton("6");
		buttonSix.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				clickBut("6");
			}
		});
		GridBagConstraints gbc_buttonSix = new GridBagConstraints();
		gbc_buttonSix.insets = new Insets(0, 0, 5, 5);
		gbc_buttonSix.gridx = 2;
		gbc_buttonSix.gridy = 2;
		panelButtons.add(buttonSix, gbc_buttonSix);
		
		JButton buttonSeven = new JButton("7");
		buttonSeven.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				clickBut("7");
			}
		});
		GridBagConstraints gbc_buttonSeven = new GridBagConstraints();
		gbc_buttonSeven.insets = new Insets(0, 0, 5, 5);
		gbc_buttonSeven.gridx = 3;
		gbc_buttonSeven.gridy = 2;
		panelButtons.add(buttonSeven, gbc_buttonSeven);
		
		JButton buttonEight = new JButton("8");
		buttonEight.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				clickBut("8");
			}
		});
		GridBagConstraints gbc_buttonEight = new GridBagConstraints();
		gbc_buttonEight.insets = new Insets(0, 0, 5, 5);
		gbc_buttonEight.gridx = 4;
		gbc_buttonEight.gridy = 2;
		panelButtons.add(buttonEight, gbc_buttonEight);
		
		JButton buttonPlus = new JButton("+");
		buttonPlus.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				clickBut("+");
			}
		});
		GridBagConstraints gbc_buttonPlus = new GridBagConstraints();
		gbc_buttonPlus.insets = new Insets(0, 0, 5, 5);
		gbc_buttonPlus.gridx = 6;
		gbc_buttonPlus.gridy = 2;
		panelButtons.add(buttonPlus, gbc_buttonPlus);
		
		JButton buttonMinus = new JButton("-");
		buttonMinus.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				clickBut("-");
			}
		});
		GridBagConstraints gbc_buttonMinus = new GridBagConstraints();
		gbc_buttonMinus.insets = new Insets(0, 0, 5, 5);
		gbc_buttonMinus.gridx = 7;
		gbc_buttonMinus.gridy = 2;
		panelButtons.add(buttonMinus, gbc_buttonMinus);
		
		JButton buttonNine = new JButton("9");
		buttonNine.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				clickBut("9");
			}
		});
		
		JButton buttonCos = new JButton("cos");
		buttonCos.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				clickBut("cos");
			}
		});
		GridBagConstraints gbc_buttonCos = new GridBagConstraints();
		gbc_buttonCos.insets = new Insets(0, 0, 5, 5);
		gbc_buttonCos.gridx = 8;
		gbc_buttonCos.gridy = 2;
		panelButtons.add(buttonCos, gbc_buttonCos);
		GridBagConstraints gbc_buttonNine = new GridBagConstraints();
		gbc_buttonNine.insets = new Insets(0, 0, 5, 5);
		gbc_buttonNine.gridx = 1;
		gbc_buttonNine.gridy = 3;
		panelButtons.add(buttonNine, gbc_buttonNine);
		
		JButton buttonZero = new JButton("0");
		buttonZero.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				clickBut("0");
			}
		});
		GridBagConstraints gbc_buttonZero = new GridBagConstraints();
		gbc_buttonZero.insets = new Insets(0, 0, 5, 5);
		gbc_buttonZero.gridx = 2;
		gbc_buttonZero.gridy = 3;
		panelButtons.add(buttonZero, gbc_buttonZero);
		
		JButton buttonDot = new JButton(".");
		buttonDot.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				clickBut(".");
			}
		});
		GridBagConstraints gbc_buttonDot = new GridBagConstraints();
		gbc_buttonDot.insets = new Insets(0, 0, 5, 5);
		gbc_buttonDot.gridx = 3;
		gbc_buttonDot.gridy = 3;
		panelButtons.add(buttonDot, gbc_buttonDot);
		
		JButton buttonEquals = new JButton("=");
		buttonEquals.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Res result = eval(textFieldExpression.getText());
				if (result.s.isEmpty()){
					if (!(result.d % 1 == 0)){
						textFieldResult.setText(result.d.toString());
					} else{
						textFieldResult.setText(String.valueOf(result.d.intValue()));
					}
				} else textFieldResult.setText(result.s);
			}
		});
		GridBagConstraints gbc_buttonEquals = new GridBagConstraints();
		gbc_buttonEquals.insets = new Insets(0, 0, 5, 5);
		gbc_buttonEquals.gridx = 4;
		gbc_buttonEquals.gridy = 3;
		panelButtons.add(buttonEquals, gbc_buttonEquals);
		
		JButton buttonMultiply = new JButton("*");
		buttonMultiply.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				clickBut("*");
			}
		});
		GridBagConstraints gbc_buttonMultiply = new GridBagConstraints();
		gbc_buttonMultiply.insets = new Insets(0, 0, 5, 5);
		gbc_buttonMultiply.gridx = 6;
		gbc_buttonMultiply.gridy = 3;
		panelButtons.add(buttonMultiply, gbc_buttonMultiply);
		
		JButton buttonDivision = new JButton("/");
		buttonDivision.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				clickBut("/");
			}
		});
		GridBagConstraints gbc_buttonDivision = new GridBagConstraints();
		gbc_buttonDivision.insets = new Insets(0, 0, 5, 5);
		gbc_buttonDivision.gridx = 7;
		gbc_buttonDivision.gridy = 3;
		panelButtons.add(buttonDivision, gbc_buttonDivision);
		
		JButton buttonTan = new JButton("tan");
		buttonTan.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				clickBut("tan");
			}
		});
		GridBagConstraints gbc_buttonTan = new GridBagConstraints();
		gbc_buttonTan.insets = new Insets(0, 0, 5, 5);
		gbc_buttonTan.gridx = 8;
		gbc_buttonTan.gridy = 3;
		panelButtons.add(buttonTan, gbc_buttonTan);
		
		JButton buttonSqr = new JButton("sqr");
		buttonSqr.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				clickBut("sqr");
			}
		});
		
		JButton button = new JButton("<-");
		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (!textFieldExpression.getText().isEmpty()){
					String x = textFieldExpression.getText();
					textFieldExpression.setText(x = x.substring(0, x.length()-1));
				}
			}
		});
		GridBagConstraints gbc_button = new GridBagConstraints();
		gbc_button.gridwidth = 2;
		gbc_button.insets = new Insets(0, 0, 0, 5);
		gbc_button.gridx = 1;
		gbc_button.gridy = 4;
		panelButtons.add(button, gbc_button);
		
		JButton btnC = new JButton("c");
		btnC.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				textFieldResult.setText("");
				textFieldExpression.setText("");
			}
		});
		GridBagConstraints gbc_btnC = new GridBagConstraints();
		gbc_btnC.gridwidth = 2;
		gbc_btnC.insets = new Insets(0, 0, 0, 5);
		gbc_btnC.gridx = 3;
		gbc_btnC.gridy = 4;
		panelButtons.add(btnC, gbc_btnC);
		GridBagConstraints gbc_buttonSqr = new GridBagConstraints();
		gbc_buttonSqr.insets = new Insets(0, 0, 0, 5);
		gbc_buttonSqr.gridx = 6;
		gbc_buttonSqr.gridy = 4;
		panelButtons.add(buttonSqr, gbc_buttonSqr);
		
		JButton buttonSqrt = new JButton("sqrt");
		buttonSqrt.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				clickBut("sqrt");
			}
		});
		GridBagConstraints gbc_buttonSqrt = new GridBagConstraints();
		gbc_buttonSqrt.insets = new Insets(0, 0, 0, 5);
		gbc_buttonSqrt.gridx = 7;
		gbc_buttonSqrt.gridy = 4;
		panelButtons.add(buttonSqrt, gbc_buttonSqrt);
		
		JButton buttonAbs = new JButton("abs");
		buttonAbs.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				clickBut("abs");
			}
		});
		GridBagConstraints gbc_buttonAbs = new GridBagConstraints();
		gbc_buttonAbs.insets = new Insets(0, 0, 0, 5);
		gbc_buttonAbs.gridx = 8;
		gbc_buttonAbs.gridy = 4;
		panelButtons.add(buttonAbs, gbc_buttonAbs);
		frame.getContentPane().setLayout(groupLayout);
	}
	
}
