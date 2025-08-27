package ca.mycustompc.calculator;

import com.ezylang.evalex.EvaluationException;
import com.ezylang.evalex.Expression;
import com.ezylang.evalex.config.ExpressionConfiguration;
import com.ezylang.evalex.parser.ParseException;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.shape.SVGPath;
import org.jetbrains.annotations.NotNull;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.prefs.Preferences;

public class Controller {

    private static final Map<Character, Character> SUPERSCRIPT_MAP = Map.ofEntries(
            Map.entry('0', '⁰'),
            Map.entry('1', '¹'),
            Map.entry('2', '²'),
            Map.entry('3', '³'),
            Map.entry('4', '⁴'),
            Map.entry('5', '⁵'),
            Map.entry('6', '⁶'),
            Map.entry('7', '⁷'),
            Map.entry('8', '⁸'),
            Map.entry('9', '⁹'),
            Map.entry('-', '⁻'),
            Map.entry('.', '∙')
    );
    private static final String lightPath = "/ca/mycustompc/calculator/light.css";
    private static final String darkPath = "/ca/mycustompc/calculator/dark.css";
    private final List<Token> tokens = new ArrayList<>();
    ExpressionConfiguration config =
            ExpressionConfiguration.builder()
                    .mathContext(new MathContext(50, RoundingMode.HALF_UP))
                    .decimalPlacesResult(15)
                    .build();
    private boolean exponentMode = false;
    private boolean isSecond = false;
    // Labels
    @FXML
    private Label answer;
    @FXML
    private Label answer1;
    @FXML
    private Label degLabel;
    @FXML
    private Label radLabel;
    // Buttons that change
    @FXML
    private Button second;
    @FXML
    private Button sin;
    @FXML
    private Button cos;
    @FXML
    private Button tan;
    @FXML
    private Button tenToTheN;
    @FXML
    private Button csc;
    @FXML
    private Button sec;
    @FXML
    private Button cot;
    @FXML
    private Button xSquared;
    @FXML
    private Button squareRoot;
    @FXML
    private Button logBaseTen;
    @FXML
    private Button naturalLog;
    @FXML
    private Button clear;
    // Main Buttons
    @FXML
    private Button zero;
    @FXML
    private Button one;
    @FXML
    private Button two;
    @FXML
    private Button three;
    @FXML
    private Button four;
    @FXML
    private Button five;
    @FXML
    private Button six;
    @FXML
    private Button seven;
    @FXML
    private Button eight;
    @FXML
    private Button nine;
    @FXML
    private Button dot;
    @FXML
    private Button plusOrMinus;
    // Other Buttons
    @FXML
    private Button leftBracket;
    @FXML
    private Button equals;
    @FXML
    private Button rightBracket;
    @FXML
    private Button subtract;
    @FXML
    private Button euler;
    @FXML
    private Button xToTheN;
    @FXML
    private Button multiply;
    @FXML
    private Button divide;
    @FXML
    private Button factorial;
    @FXML
    private Button modulo;
    @FXML
    private Button pi;
    @FXML
    private Button add;
    @FXML
    private Button backspace;
    // Theme controls
    @FXML
    private Button themeSwitch;
    @FXML
    private SVGPath moon;
    @FXML
    private SVGPath sun;
    private boolean darkMode = false;
    private Scene scene;
    private Preferences prefs;
    private boolean isRadians = false;

    public void setScene(Scene scene) {
        this.scene = scene;
        prefs = Preferences.userNodeForPackage(Controller.class);
        darkMode = prefs.getBoolean("darkmode", true);
        applyTheme();
        themeSwitch.setOnAction(e -> {
            darkMode = !darkMode;
            applyTheme();
            prefs.putBoolean("darkmode", darkMode);
        });
    }

    private void applyTheme() {
        if (scene == null) return;
        scene.getStylesheets().setAll(getClass().getResource(darkMode ? darkPath : lightPath).toExternalForm());
        sun.setVisible(darkMode);
        moon.setVisible(!darkMode);
    }

    public void secondClicked(MouseEvent mouseEvent) {
        if (isSecond) {
            second.getStyleClass().remove("specialButtons");
            isSecond = false;
            sin.setText("sin");
            cos.setText("cos");
            tan.setText("tan");
            csc.setText("csc");
            sec.setText("sec");
            cot.setText("cot");
            tenToTheN.setText("10ⁿ");
            xSquared.setText("x²");
            squareRoot.setText("√");
            naturalLog.setText("ln");
            logBaseTen.setText("log₁₀");
        } else {
            second.getStyleClass().add("specialButtons");
            isSecond = true;
            sin.setText("sin⁻¹");
            cos.setText("cos⁻¹");
            tan.setText("tan⁻¹");
            csc.setText("csc⁻¹");
            sec.setText("sec⁻¹");
            cot.setText("cot⁻¹");
            tenToTheN.setText("2ⁿ");
            xSquared.setText("x³");
            squareRoot.setText("∛");
            naturalLog.setText("eⁿ");
            logBaseTen.setText("log₂");
        }
    }

    public void radiansSwap(MouseEvent mouseEvent) {
        if (isRadians) {
            radLabel.getStyleClass().remove("activatedLabel");
            degLabel.getStyleClass().add("activatedLabel");
            isRadians = false;
        } else {
            radLabel.getStyleClass().add("activatedLabel");
            degLabel.getStyleClass().remove("activatedLabel");
            isRadians = true;
        }
    }

    public void initialize() {
        degLabel.getStyleClass().add("activatedLabel");

        leftBracket.setOnAction(this::handleButtonClick);
        equals.setOnAction(this::handleButtonClick);
        rightBracket.setOnAction(this::handleButtonClick);
        subtract.setOnAction(this::handleButtonClick);
        euler.setOnAction(this::handleButtonClick);
        xToTheN.setOnAction(this::handleButtonClick);
        multiply.setOnAction(this::handleButtonClick);
        divide.setOnAction(this::handleButtonClick);
        factorial.setOnAction(this::handleButtonClick);
        modulo.setOnAction(this::handleButtonClick);
        pi.setOnAction(this::handleButtonClick);
        add.setOnAction(this::handleButtonClick);
        sin.setOnAction(this::handleButtonClick);
        cos.setOnAction(this::handleButtonClick);
        tan.setOnAction(this::handleButtonClick);
        tenToTheN.setOnAction(this::handleButtonClick);
        csc.setOnAction(this::handleButtonClick);
        sec.setOnAction(this::handleButtonClick);
        cot.setOnAction(this::handleButtonClick);
        xSquared.setOnAction(this::handleButtonClick);
        squareRoot.setOnAction(this::handleButtonClick);
        logBaseTen.setOnAction(this::handleButtonClick);
        naturalLog.setOnAction(this::handleButtonClick);
        clear.setOnAction(this::handleButtonClick);
        backspace.setOnAction(this::handleButtonClick);

        zero.setOnAction(this::handleButtonClick);
        one.setOnAction(this::handleButtonClick);
        two.setOnAction(this::handleButtonClick);
        three.setOnAction(this::handleButtonClick);
        four.setOnAction(this::handleButtonClick);
        five.setOnAction(this::handleButtonClick);
        six.setOnAction(this::handleButtonClick);
        seven.setOnAction(this::handleButtonClick);
        eight.setOnAction(this::handleButtonClick);
        nine.setOnAction(this::handleButtonClick);
        dot.setOnAction(this::handleButtonClick);
        plusOrMinus.setOnAction(this::handleButtonClick);
        leftBracket.setOnAction(this::handleButtonClick);
    }

    @FXML
    private void handleButtonClick(javafx.event.ActionEvent event) {
        Button source = (Button) event.getSource();
        String buttonText = source.getText();

        if (!Objects.equals(buttonText, "C")) clear.setText("CE");

        if (exponentMode) {
            if (buttonText.equals("±")) {
                togglePlusMinus();
                answer.setText(getDisplayString());
                return;
            }

            if (buttonText.matches("[-0-9.]")) {
                addToken(buttonText, buttonText);
                answer.setText(getDisplayString());
                return;
            }

            stopExponent();
        }

        switch (buttonText) {
            case "⌫":
                backspace();
                break;
            case "0":
                addToken("0", "0");
                break;
            case "1":
                addToken("1", "1");
                break;
            case "2":
                addToken("2", "2");
                break;
            case "3":
                addToken("3", "3");
                break;
            case "4":
                addToken("4", "4");
                break;
            case "5":
                addToken("5", "5");
                break;
            case "6":
                addToken("6", "6");
                break;
            case "7":
                addToken("7", "7");
                break;
            case "8":
                addToken("8", "8");
                break;
            case "9":
                addToken("9", "9");
                break;
            case ".":
                addToken(".", ".");
                break;
            case "±":
                togglePlusMinus();
                break;
            case "(":
                addToken("(", "(");
                break;
            case ")":
                addToken(")", ")");
                break;
            case "-":
                addToken("-", "-");
                break;
            case "e":
                addToken("e", "e");
                break;
            case "xⁿ":
                startExponent();
                break;
            case "×":
                addToken("×", "*");
                break;
            case "÷":
                addToken("÷", "/");
                break;
            case "n!":
                addToken("!(", "fact(", true);
                break;
            case "mod":
                addToken("%", "%");
                break;
            case "π":
                addToken("π", "PI");
                break;
            case "+":
                addToken("+", "+");
                break;
            case "sin":
                addToken("sin(", isRadians ? "sinr(" : "sin(", true);
                break;
            case "sin⁻¹":
                addToken("sin⁻¹(", isRadians ? "asinr(" : "asin(", true);
                break;
            case "cos":
                addToken("cos(", isRadians ? "cosr(" : "cos(", true);
                break;
            case "cos⁻¹":
                addToken("cos⁻¹(", isRadians ? "acosr(" : "acos(", true);
                break;
            case "tan":
                addToken("tan(", isRadians ? "tanr(" : "tan(", true);
                break;
            case "tan⁻¹":
                addToken("tan⁻¹(", isRadians ? "atanr(" : "atan(", true);
                break;
            case "10ⁿ":
                addToken("10", "10");
                startExponent();
                break;
            case "2ⁿ":
                addToken("2", "2");
                startExponent();
                break;
            case "csc":
                addToken("csc(", isRadians ? "cscr(" : "csc(", true);
                break;
            case "csc⁻¹":
                addToken("csc⁻¹(", isRadians ? "asinr(1/" : "asin(1/", true);
                break;
            case "sec":
                addToken("sec(", isRadians ? "secr(" : "sec(", true);
                break;
            case "sec⁻¹":
                addToken("sec⁻¹(", isRadians ? "acosr(1/" : "acos(1/", true);
                break;
            case "cot":
                addToken("cot(", isRadians ? "cotr(" : "cot(", true);
                break;
            case "cot⁻¹":
                addToken("cot⁻¹(", isRadians ? "atanr(1/" : "atan(1/", true);
                break;
            case "x²":
                applyPowerToPrevious(2);
                break;
            case "x³":
                applyPowerToPrevious(3);
                break;
            case "√":
                addToken("√(", "sqrt(", true);
                break;
            case "∛":
                addToken("∛(", "(e^(1/3))^log(", true);
                break;
            case "log₁₀":
                addToken("log₁₀(", "log10(", true);
                break;
            case "log₂":
                addToken("log₂(", "(1/log(2))*log(", true);
                break;
            case "ln":
                addToken("ln(", "log(", true);
                break;
            case "eⁿ":
                addToken("e", "e");
                startExponent();
                break;
            case "CE":
                clear.setText("C");
                answer.setText("0");
                answer1.setText("");
                clearAll();
                return;
            case "C":
                answer.setText("0");
                answer1.setText("");
                return;
            case "=":
                try {
                    String formatted = getString();
                    answer.setText(formatted);
                    answer1.setText(getDisplayString() + "=");
                    return;
                } catch (Exception e) {
                    answer.setText("Error");
                    System.out.println(getInternalString());
                    clearAll();
                    return;
                }
        }
        answer.setText(getDisplayString());
    }

    @NotNull
    private String getString() throws EvaluationException, ParseException {

        Expression expression = new Expression(getInternalString(), config);
        BigDecimal result = expression.evaluate().getNumberValue();

        String formatted;

        if (result.abs().compareTo(new BigDecimal("1000000000000000")) >= 0 ||
                (result.signum() != 0 && result.abs().compareTo(new BigDecimal("0.000000000000001")) < 0)) {
            formatted = result.round(new MathContext(15, RoundingMode.HALF_UP)).toEngineeringString();
        } else {
            formatted = result.stripTrailingZeros().toPlainString();
        }

        if (formatted.length() > 15) {
            if (formatted.contains("E")) {
                return formatted;
            } else if (formatted.contains(".")) {
                int integerDigits = formatted.indexOf(".");
                int decimalsAllowed = Math.max(0, 15 - integerDigits - 1);
                BigDecimal rounded = result.setScale(decimalsAllowed, RoundingMode.HALF_UP);
                formatted = rounded.stripTrailingZeros().toPlainString();

                if (formatted.length() > 15) {
                    formatted = formatted.substring(0, 15);
                }
            } else {
                formatted = formatted.substring(0, 15);
            }
        }

        return formatted;
    }

    private boolean isNumericToken(String s) {
        if (s == null || s.isEmpty()) return false;
        if (s.equals(".")) return true;
        return s.matches("-?\\d+(\\.\\d+)?");
    }

    private String toSuperscript(String input) {
        StringBuilder sb = new StringBuilder();
        for (char c : input.toCharArray()) {
            sb.append(SUPERSCRIPT_MAP.getOrDefault(c, c));
        }
        return sb.toString();
    }

    private void refreshExponentDisplay(Token t) {
        if (t.internal == null || !t.internal.startsWith("^")) return;
        if (t.internal.length() == 1) {
            t.display = "";
        } else {
            String exponentPart = t.internal.substring(1);
            t.display = toSuperscript(exponentPart);
        }
    }

    private void addToken(String display, String internal) {
        addToken(display, internal, false);
    }

    private void addToken(String display, String internal, boolean atomic) {
        if (exponentMode && display.matches("[0-9.-]+")) {
            if (tokens.isEmpty() || !tokens.getLast().internal.startsWith("^")) {
                tokens.add(new Token("", "^", false));
            }
            Token last = tokens.getLast();
            last.internal += internal;
            refreshExponentDisplay(last);
            return;
        }
        tokens.add(new Token(display, internal, atomic));
    }

    private void togglePlusMinus() {
        if (tokens.isEmpty()) return;

        if (exponentMode) {
            Token last = tokens.getLast();
            if (!last.internal.startsWith("^")) {
                last = new Token("", "^");
                tokens.add(last);
            }
            if (last.internal.startsWith("^-")) {
                last.internal = "^" + last.internal.substring(2);
            } else if (last.internal.startsWith("^")) {
                last.internal = "^-" + last.internal.substring(1);
            }
            refreshExponentDisplay(last);
            return;
        }

        int i = tokens.size() - 1;
        while (i >= 0 && isNumericToken(tokens.get(i).internal)) {
            i--;
        }
        int start = i + 1;
        if (start >= tokens.size()) return;
        Token first = tokens.get(start);
        if (first.internal.startsWith("-")) {
            first.internal = first.internal.substring(1);
            if (!first.display.isEmpty() && first.display.charAt(0) == '-') {
                first.display = first.display.substring(1);
            }
        } else {
            first.internal = "-" + first.internal;
            first.display = "-" + first.display;
        }
    }

    private void backspace() {
        if (tokens.isEmpty()) return;

        Token last = tokens.getLast();

        if (last.atomic) {
            tokens.removeLast();
            if (last.internal != null && last.internal.startsWith("^")) {
                exponentMode = false;
            }
            return;
        }

        if (!last.internal.isEmpty()) {
            last.internal = last.internal.substring(0, last.internal.length() - 1);

            if (last.internal.startsWith("^")) {
                if (last.internal.equals("^")) {
                    tokens.removeLast();
                    exponentMode = false;
                } else {
                    refreshExponentDisplay(last);
                }
            } else {
                if (!last.display.isEmpty()) {
                    last.display = last.display.substring(0, last.display.length() - 1);
                }
                if (last.internal.isEmpty()) {
                    tokens.removeLast();
                }
            }
        } else {
            tokens.removeLast();
            exponentMode = false;
        }
    }

    private void clearAll() {
        tokens.clear();
        exponentMode = false;
    }

    private void applyPowerToPrevious(int power) {
        if (tokens.isEmpty()) return;

        int end = tokens.size() - 1;
        int start = end;

        if (tokens.get(end).internal.equals(")")) {
            int depth = 0;
            for (int j = end; j >= 0; j--) {
                if (tokens.get(j).internal.equals(")")) depth++;
                else if (tokens.get(j).internal.equals("(")) {
                    depth--;
                    if (depth == 0) {
                        start = j;
                        break;
                    }
                }
            }
        } else if (isNumericToken(tokens.get(end).internal)) {
            int j = end;
            while (j >= 0 && isNumericToken(tokens.get(j).internal)) {
                j--;
            }
            start = j + 1;
        }

        tokens.add(start, new Token("", "(", true));

        startExponent();
        addToken(String.valueOf(power), String.valueOf(power));
        stopExponent();
        addToken("", ")", true);
    }

    private String getDisplayString() {
        StringBuilder sb = new StringBuilder();
        for (Token t : tokens) {
            if (t.internal != null && t.internal.startsWith("^")) {
                if (t.internal.length() > 1) {
                    sb.append(toSuperscript(t.internal.substring(1)));
                }
            } else {
                sb.append(t.display);
            }
        }
        return sb.toString();
    }

    private String getInternalString() {
        StringBuilder sb = new StringBuilder();
        for (Token t : tokens) sb.append(t.internal);
        return sb.toString();
    }

    private void startExponent() {
        if (exponentMode) return;
        exponentMode = true;
        tokens.add(new Token("", "^"));
    }

    private void stopExponent() {
        exponentMode = false;
    }

    private static class Token {
        String display;
        String internal;
        boolean atomic;

        Token(String display, String internal) {
            this(display, internal, false);
        }

        Token(String display, String internal, boolean atomic) {
            this.display = display;
            this.internal = internal;
            this.atomic = atomic;
        }
    }
}
