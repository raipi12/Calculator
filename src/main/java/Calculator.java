import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Calculator {

    public String calculate(String expression) {
        String exp = expression.replace(" ", "");

        if (exp.matches("[0-9]+") || exp.isEmpty())
            return exp;
        else if (!exp.matches("^[0-9*+/()-.]+$") || !exp.matches("-?\\(?-?[0-9]+.*[0-9]+\\)?"))
            throw new IllegalArgumentException("The operation does not have a correct structure");

        String expWithoutParentheses = resolveParentheses(exp);

        char[] chars = expWithoutParentheses.toCharArray();
        List<String> answer = new ArrayList<>();

        StringBuilder number = new StringBuilder();
        String operation = "";
        String firstNum = "";
        String secondNum = "";

        boolean operationInQueue = false;

        for (int i = 0; i < chars.length; i++) {
            if (Character.isDigit(chars[i])
                    || chars[i] == '.' || chars[i] == '(' || chars[i] == ')'
                    || (chars[i] == '-' && i == 0 || chars[i - 1] == '(')) {

                number.append(chars[i]);
            } else {
                answer.add(number.toString());
                answer.add(Character.toString(chars[i]));

                if (operationInQueue) {
                    operationInQueue = false;
                    secondNum = number.toString();

                    answer.set(answer.lastIndexOf(firstNum), evaluate(firstNum, operation, secondNum));
                    answer.remove(answer.lastIndexOf(operation));
                    answer.remove(answer.lastIndexOf(secondNum));
                }
                if (chars[i] == '*' || chars[i] == '/') {
                    operationInQueue = true;

                    operation = Character.toString(chars[i]);
                    firstNum = answer.get(answer.lastIndexOf(operation) - 1);
                }
                number = new StringBuilder();
            }
            if (number.toString().equals(expWithoutParentheses)) {

                if (number.toString().contains("("))
                    answer.add(number.toString().replaceAll("(\\()(.*)(\\))", "$2"));
                else
                    answer.add(number.toString());

                break;
            }
            if (i == chars.length - 1 && number.length() > 0) {
                answer.add(number.toString());

                if (answer.get(answer.size() - 2).equals("*")
                        || answer.get(answer.size() - 2).equals("/")) {

                    firstNum = answer.get(answer.size() - 3);
                    operation = answer.get(answer.size() - 2);
                    secondNum = answer.get(answer.size() - 1);

                    answer.set(answer.size() - 3, evaluate(firstNum, operation, secondNum));
                    answer.remove(answer.size() - 2);
                    answer.remove(answer.size() - 1);
                }
            }
        }
        while (answer.size() > 1) {
            firstNum = answer.get(0);
            operation = answer.get(1);
            secondNum = answer.get(2);

            answer.set(0, evaluate(firstNum, operation, secondNum));
            answer.remove(2);
            answer.remove(1);
        }

        return answer.get(0);
    }

    public String resolveParentheses(String expression) {
        if (expression.contains("(")) {
            List<String> groups = new ArrayList<>();
            Matcher matcher = Pattern.compile("\\(-?[0-9]*[-+/*][0-9]*\\)").matcher(expression);

            while (matcher.find())
                groups.add(matcher.group());

            String result = "";
            if (groups.isEmpty())
                throw new IllegalArgumentException("The operation does not have a correct structure");

            for (String group : groups){
                String evaluated = "";
                String regex1 = "(\\()(-?)(\\w*)([+-/*])(\\w*)(\\))";
                String regex2 = "(\\()(\\w*)([+-/*])(\\w*)(\\))";

                if (group.charAt(group.indexOf("(") + 1) == '-'){
                    evaluated = evaluate(group.replaceAll(regex1, "$2$3"),
                            group.replaceAll(regex1, "$4"),
                            group.replaceAll(regex1, "$5"));
                }else {
                    evaluated = evaluate(group.replaceAll(regex2, "$2"),
                            group.replaceAll(regex2, "$3"),
                            group.replaceAll(regex2, "$4"));
                }
                char symbol = 0;

                if (evaluated.contains("-")) {
                    int index = expression.indexOf("(") - 1;
                    if (index >= 0)
                        symbol = expression.charAt(index);
                    else
                        result = expression.replace(group, "(" + evaluated + ")");

                    if (symbol == '-')
                        result = expression.replace("-" + group, "+" + evaluated.substring(1));
                    else if (symbol == '+')
                        result = expression.replace("+" + group, evaluated);
                    else
                        result = expression.replace(group, "(" + evaluated + ")");
                }else {
                    if (!result.isEmpty())
                        result = result.replace(group, evaluated);
                    else
                        result = expression.replace(group, evaluated);
                }
            }

            return result;
        } else {
            return expression;
        }
    }

    public String evaluate(String firstNum, String operation, String lastNum) {
        double result = 0;

        String regex = "(\\()(.*)(\\))";
        String group = "$2";
        String para = "(";

        if (firstNum.contains(para) )
            firstNum = firstNum.replaceAll(regex, group);
        else if (lastNum.contains(para))
            lastNum = lastNum.replaceAll(regex, group);

        switch (operation) {
            case "/":
                result += Double.parseDouble(firstNum) / Double.parseDouble(lastNum);
                break;
            case "*":
                result += Double.parseDouble(firstNum) * Double.parseDouble(lastNum);
                break;
            case "-":
                result += Double.parseDouble(firstNum) - Double.parseDouble(lastNum);
                break;
            case "+":
                result += Double.parseDouble(firstNum) + Double.parseDouble(lastNum);
                break;
        }

        return Double.toString(result);
    }
}