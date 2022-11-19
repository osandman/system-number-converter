import com.sun.tools.javac.comp.Todo;

import java.math.BigInteger;
import java.util.InputMismatchException;
import java.util.Scanner;

class SystemNumberConverter {
    private static final String SYSTEM20_CHARS = "0123456789abcdefghij";
    private static final String DECIMAL_CHARS = "0123456789";
    private static final int MAX_NUMBER_SYS = SYSTEM20_CHARS.length();
    private static String validCharacterString;
    private String numberX;
    private String numberY;
    private int sysX, sysY;

    public void runConverter() {
        System.out.println("Это программа для конвертации числа из одной системы исчисления в другую");
        boolean flag1;
        boolean flag2 = true;
        //TODO  общий метод для отслеживания строки ввода и возможность возврата выхода из программы
        do {
            System.out.println("Введите основание исходной системы исчисления (от 2-х до " + MAX_NUMBER_SYS
                    + "): ");
            setSysX(inputInt());
            System.out.println("Введите основание системы исчисления (от 2-х до " + MAX_NUMBER_SYS
                    + ") в которую будем преобразовывать число: ");
            setSysY(inputInt());
            flag1 = true;
            do {
                int response = setNumberX(sysX);
                switch (response) {
                    case 1: //выход из этапа ввода числа
                        flag1 = false;
                        continue;
                    case 2: //вывод информации о текущих системах исчислений
                        getInfo();
                        continue;
                }
                numberY = convToY(numberX, sysX, sysY);
                printResult(sysX, sysY, numberX, numberY);
            }
            while (flag1);
        }
        while (flag2);
    }


    public void runConverter(int x, int y, String num) {
        numberY = convToY(num, x, y);
        printResult(x, y, num, numberY);
    }

    public void runConverter(String num) {
        if (!isValidCharacter(num, sysX)) {
            errorValidMessage(validCharacterString);
        } else {
            numberY = convToY(num, sysX, sysY);
            printResult(sysX, sysY, num, numberY);
        }
    }

    public void printResult(int x, int y, String numX, String numY) {
        System.out.printf("Число '%s'(%d-ичное) в %d-ичной системе исчисления = %s%n", numX, x, y, numY);
    }

    public void getInfo() {
        System.out.printf("Сейчас заданы системы: x = %d, y = %d\n", getSysX(), getSysY());
    }

    public void setSysX(String sysX) {
        try {
            this.sysX = Integer.parseInt(sysX);
        } catch (NumberFormatException ex) {
            errorValidMessage(DECIMAL_CHARS);
        }
    }

    public void setSysY(String sysY) {
        try {
            this.sysY = Integer.parseInt(sysY);
        } catch (NumberFormatException ex) {
            errorValidMessage(DECIMAL_CHARS);
        }
    }

    public void setSysX(int sysX) {
        this.sysX = sysX;
    }

    public void setSysY(int sysY) {
        this.sysY = sysY;
    }

    public int getSysX() {
        return sysX;
    }

    public int getSysY() {
        return sysY;
    }

    private boolean isValidCharacter(String num, int sysTypeX) {
        validCharacterString = SYSTEM20_CHARS.substring(0, sysTypeX);
        if (num.isEmpty()) return false;
        for (int k = 0; k < num.length(); k++) {
            if (validCharacterString.indexOf(num.charAt(k)) == -1) return false;
        }
        return true;
    }

    private String getStringFromUser() {
        Scanner scan = new Scanner(System.in);
        return scan.nextLine();
    }
    private int setNumberX(int sysX) {
        System.out.println("Введите число в " + sysX + "-ичной системе или 'q' для выбора систем исчисления");
        numberX = getStringFromUser();
        switch (numberX) {
            case "q":
                return 1;
            case "i":
                return 2;
        }
        if (!isValidCharacter(numberX, sysX)) {
            errorValidMessage(validCharacterString);
            return setNumberX(sysX);
        }
        return 0;
    }

    private int inputInt() {
        try {
            int num = Integer.parseInt(getStringFromUser());
            if (num >= 2 & num <= MAX_NUMBER_SYS) {
                return num;
            } else {
                System.out.println("Недопустимые символы, введите число (от 2-х до " + SYSTEM20_CHARS.length() + ")");
                return inputInt();
            }
        } catch (Exception e) {
            System.out.println("Недопустимые символы, введите число (от 2-х до " + SYSTEM20_CHARS.length() + ")");
            return inputInt();
        }
    }

    private void errorValidMessage(String validCharStr) {
        System.out.print("В числе недопустимые символы, ");
        String validStr = validCharStr.replaceAll("", " ").trim();
        validStr = validStr.replaceAll(" ", ", ");
        System.out.print("введите символы: ");
        System.out.println(validStr);
    }

    private BigInteger toDecimalFromX(String xNumber, int sysTypeX) {
        //метод переводит любое число из системы исчисления (от 1 до 20) в десятичное
        if (xNumber.isEmpty()) return BigInteger.valueOf(0);
        BigInteger decNum = BigInteger.valueOf(0);
        BigInteger powNum, indNum;
        BigInteger s = BigInteger.valueOf(sysTypeX);
        int index;
        for (int i = 0; i < xNumber.length(); i++) {
            index = xNumber.length() - 1 - i;
            powNum = s.pow(i);
            indNum = BigInteger.valueOf(SYSTEM20_CHARS.indexOf(xNumber.charAt(index)));
            BigInteger c = powNum.multiply(indNum);
            decNum = decNum.add(c);
        }
        return decNum;
    }

    private String toYfromDecimal(BigInteger decimalNumber, int sysTypeY) {
        String strYNumber = "";
        StringBuilder strBuild = new StringBuilder(strYNumber);
        while (decimalNumber.compareTo(BigInteger.ZERO) == 1) {
            strBuild.insert(0, SYSTEM20_CHARS.charAt(decimalNumber.remainder(BigInteger.valueOf(sysTypeY)).intValue()));
            decimalNumber = decimalNumber.divide(BigInteger.valueOf(sysTypeY));
        }
        return strBuild.toString();
    }

    private String convToY(String xNumber, int sysTypeX, int sysTypeY) {
        if (xNumber.equals("0")) return "0";
        if (sysTypeY == 10) {
            return String.valueOf(toDecimalFromX(xNumber, sysTypeX));
        } else {
            BigInteger decBigX = toDecimalFromX(xNumber, sysTypeX);
            return toYfromDecimal(decBigX, sysTypeY);
        }
    }

}

