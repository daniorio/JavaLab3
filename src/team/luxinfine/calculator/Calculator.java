package team.luxinfine.calculator;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Optional;


public class Calculator implements ActionListener
{
    private final ScriptEngine engine = new ScriptEngineManager().getEngineByName("JavaScript");
    private final JFrame frame = new JFrame("Windows Calculator");
    public final JTextField text = new JTextField(); // need public for reflection
    public JButton b1, b2, b3, b4, b5, b6, b7, b8, b9, b0, bdiv, bmul, bsub, badd, bdec, beq, bdel, bclr; // need public for reflection

    public Calculator()  {
        this.initButtons();
        this.setBounds();
        this.addToFrame();
        this.initMainFrame();
        this.addActionListeners();
    }

    private void initMainFrame() {
        this.frame.setLayout(null);
        this.frame.setVisible(true);
        this.frame.setSize(350,500);
        this.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.frame.setResizable(false);
    }

    private void addActionListeners() {
        Arrays.stream(this.getClass().getFields()).forEach(filed -> this.getFromField(filed, AbstractButton.class).ifPresent(btn -> btn.addActionListener(this)));
    }

    private <T> Optional<T> getFromField(Field field, Class<T> clazz) {
        try {
            return Optional.of(clazz.cast(field.get(this)));
        } catch(Throwable t) {}
        return Optional.empty();
    }
    private void initButtons() {
        this.b1 = new JButton("1");
        this.b2 = new JButton("2");
        this.b3 = new JButton("3");
        this.b4 = new JButton("4");
        this.b5 = new JButton("5");
        this.b6 = new JButton("6");
        this.b7 = new JButton("7");
        this.b8 = new JButton("8");
        this.b9 = new JButton("9");
        this.b0 = new JButton("0");
        this.bdiv = new JButton("/");
        this.bmul = new JButton("*");
        this.bsub = new JButton("-");
        this.badd = new JButton("+");
        this.bdec = new JButton(".");
        this.beq = new JButton("=");
        this.bdel = new JButton("Delete");
        this.bclr = new JButton("Clear");
    }

    private void setBounds() {
        this.text.setBounds(30,40,280,30);
        this.b7.setBounds(40,100,50,50);
        this.b8.setBounds(110,100,50,50);
        this.b9.setBounds(180,100,50,50);
        this.bdiv.setBounds(250,100,50,50);
        this.b4.setBounds(40,170,50,50);
        this.b5.setBounds(110,170,50,50);
        this.b6.setBounds(180,170,50,50);
        this.bmul.setBounds(250,170,50,50);
        this.b1.setBounds(40,240,50,50);
        this.b2.setBounds(110,240,50,50);
        this.b3.setBounds(180,240,50,50);
        this.bsub.setBounds(250,240,50,50);
        this.bdec.setBounds(40,310,50,50);
        this.b0.setBounds(110,310,50,50);
        this.beq.setBounds(180,310,50,50);
        this.badd.setBounds(250,310,50,50);
        this.bdel.setBounds(40,380,120,50);
        this.bclr.setBounds(180,380,120,50);
    }

    private void addToFrame() {
        Arrays.stream(this.getClass().getFields()).forEach(filed -> this.getFromField(filed, Component.class).ifPresent(frame::add));
    }

    @Override
    public void actionPerformed(ActionEvent e)
    {
        String btnText = ((AbstractButton)e.getSource()).getText();
        String txt = this.text.getText();
        switch(btnText) {
            case "=":
                if(!txt.isEmpty())
                    try {
                        this.text.setText(this.engine.eval(txt).toString());
                    } catch(Throwable ex) {
                        this.text.setText("ERROR");
                        ex.printStackTrace();
                    }
                break;
            case "Delete":
                this.text.setText(Optional.ofNullable(txt).filter(str -> str.length() != 0).map(str -> str.substring(0, str.length() - 1)).orElse(txt));
                break;
            case "Clear":
                this.text.setText("");
                break;
            default:
                this.text.setText(txt.concat(btnText));
                break;
        }
    }

    public static void main(String...s)
    {
        new Calculator();
    }
}