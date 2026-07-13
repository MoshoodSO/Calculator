package com.example.calculator;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {

    private TextView displayTextView;
    private final StringBuilder inputStringBuilder = new StringBuilder();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        
        View root = findViewById(R.id.main);
        if (root != null) {
            ViewCompat.setOnApplyWindowInsetsListener(root, (v, insets) -> {
                Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
                v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
                return insets;
            });
        }

        displayTextView = findViewById(R.id.display_text_view);

        setNumericOnClickListener();
        setOperatorOnClickListener();

        findViewById(R.id.button_clear).setOnClickListener(v -> {
            inputStringBuilder.setLength(0);
            displayTextView.setText("0");
        });

        findViewById(R.id.button_backspace).setOnClickListener(v -> {
            if (inputStringBuilder.length() > 0) {
                inputStringBuilder.deleteCharAt(inputStringBuilder.length() - 1);
                displayTextView.setText(inputStringBuilder.length() == 0 ? "0" : inputStringBuilder.toString());
            }
        });

        findViewById(R.id.button_equals).setOnClickListener(v -> {
            if (inputStringBuilder.length() > 0) {
                String result = evaluate(inputStringBuilder.toString());
                displayTextView.setText(result);
                inputStringBuilder.setLength(0);
                if (!result.equals("Error")) {
                    inputStringBuilder.append(result);
                }
            }
        });
    }

    private void setNumericOnClickListener() {
        View.OnClickListener listener = v -> {
            Button button = (Button) v;
            String text = button.getText().toString();
            if (text.equals(".")) {
                if (canAddDot()) {
                    if (inputStringBuilder.length() == 0) {
                        inputStringBuilder.append("0");
                    }
                    inputStringBuilder.append(text);
                }
            } else {
                if (inputStringBuilder.toString().equals("0")) {
                    inputStringBuilder.setLength(0);
                }
                inputStringBuilder.append(text);
            }
            displayTextView.setText(inputStringBuilder.toString());
        };

        int[] ids = {
                R.id.button_0, R.id.button_1, R.id.button_2, R.id.button_3, R.id.button_4,
                R.id.button_5, R.id.button_6, R.id.button_7, R.id.button_8, R.id.button_9,
                R.id.button_dot
        };

        for (int id : ids) {
            View b = findViewById(id);
            if (b != null) b.setOnClickListener(listener);
        }
    }

    private boolean canAddDot() {
        if (inputStringBuilder.length() == 0) return true;
        for (int i = inputStringBuilder.length() - 1; i >= 0; i--) {
            char c = inputStringBuilder.charAt(i);
            if (isOperator(c)) return true;
            if (c == '.') return false;
        }
        return true;
    }

    private void setOperatorOnClickListener() {
        View.OnClickListener listener = v -> {
            Button button = (Button) v;
            String op = button.getText().toString();
            if (inputStringBuilder.length() > 0) {
                char lastChar = inputStringBuilder.charAt(inputStringBuilder.length() - 1);
                if (isOperator(lastChar)) {
                    inputStringBuilder.setCharAt(inputStringBuilder.length() - 1, op.charAt(0));
                } else if (lastChar != '.') {
                    inputStringBuilder.append(op);
                }
                displayTextView.setText(inputStringBuilder.toString());
            }
        };

        int[] ids = {
                R.id.button_plus, R.id.button_minus, R.id.button_multiply, R.id.button_divide
        };

        for (int id : ids) {
            View b = findViewById(id);
            if (b != null) b.setOnClickListener(listener);
        }
    }

    private boolean isOperator(char c) {
        return c == '+' || c == '-' || c == '*' || c == '/';
    }

    private String evaluate(String expression) {
        try {
            double result = eval(expression);
            if (Double.isInfinite(result) || Double.isNaN(result)) {
                return "Error";
            }
            if (result == (long) result) {
                return String.valueOf((long) result);
            } else {
                return String.valueOf(result);
            }
        } catch (Exception e) {
            return "Error";
        }
    }

    public static double eval(final String str) {
        return new Object() {
            int pos = -1, ch;

            void nextChar() {
                ch = (++pos < str.length()) ? str.charAt(pos) : -1;
            }

            boolean eat(int charToEat) {
                while (ch == ' ') nextChar();
                if (ch == charToEat) {
                    nextChar();
                    return true;
                }
                return false;
            }

            double parse() {
                nextChar();
                double x = parseExpression();
                if (pos < str.length()) throw new RuntimeException("Unexpected: " + (char) ch);
                return x;
            }

            double parseExpression() {
                double x = parseTerm();
                for (; ; ) {
                    if (eat('+')) x += parseTerm();
                    else if (eat('-')) x -= parseTerm();
                    else return x;
                }
            }

            double parseTerm() {
                double x = parseFactor();
                for (; ; ) {
                    if (eat('*')) x *= parseFactor();
                    else if (eat('/')) {
                        double divisor = parseFactor();
                        if (divisor == 0) throw new ArithmeticException("Division by zero");
                        x /= divisor;
                    } else return x;
                }
            }

            double parseFactor() {
                if (eat('+')) return parseFactor();
                if (eat('-')) return -parseFactor();

                double x;
                int startPos = this.pos;
                if (eat('(')) {
                    x = parseExpression();
                    eat(')');
                } else if ((ch >= '0' && ch <= '9') || ch == '.') {
                    while ((ch >= '0' && ch <= '9') || ch == '.') nextChar();
                    String sub = str.substring(startPos, this.pos);
                    if (sub.equals(".")) throw new RuntimeException("Invalid number");
                    x = Double.parseDouble(sub);
                } else {
                    throw new RuntimeException("Unexpected: " + (char) ch);
                }

                return x;
            }
        }.parse();
    }
}