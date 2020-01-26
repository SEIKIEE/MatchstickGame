import java.util.Map;
import java.util.HashMap;
import java.util.Scanner;

public class MatchstickGame {
    public static Map<String, String[]> numberAdd = new HashMap<String, String[]>();
    public static Map<String, String[]> numberSub = new HashMap<String, String[]>();
    public static String equation = new String("");

    public static void initialize() {
        numberAdd.put("0", new String[]{"8", "*", "*", "*", "*", "*"});
        numberAdd.put("1", new String[]{"7", "4", "3", "09", "8"});
        numberAdd.put("2", new String[]{"*", "8", "*", "*", "*"});
        numberAdd.put("3", new String[]{"9", "8", "*", "*", "*"});
        numberAdd.put("4", new String[]{"*", "9", "8", "*", "*"});
        numberAdd.put("5", new String[]{"69", "8", "*", "*", "*"});
        numberAdd.put("6", new String[]{"8", "*", "*", "*", "*"});
        numberAdd.put("7", new String[]{"*", "3", "09", "8", "*"});
        numberAdd.put("8", new String[]{"*", "*", "*", "*", "*"});
        numberAdd.put("9", new String[]{"8", "*", "*", "*", "*"});
        numberAdd.put("+", new String[]{"*", "*", "*", "*", "*"});
        numberAdd.put("-", new String[]{"+", "*", "*", "*", "*"});

        numberSub.put("0", new String[]{"*", "*", "7", "1", "*", "*"});
        numberSub.put("1", new String[]{"*", "*", "*", "*", "*"});
        numberSub.put("2", new String[]{"*", "*", "*", "*", "*"});
        numberSub.put("3", new String[]{"*", "7", "1", "*", "*"});
        numberSub.put("4", new String[]{"*", "1", "*", "*", "*"});
        numberSub.put("5", new String[]{"*", "*", "*", "*", "*"});
        numberSub.put("6", new String[]{"5", "*", "*", "*", "*"});
        numberSub.put("7", new String[]{"1", "*", "*", "*", "*"});
        numberSub.put("8", new String[]{"069", "235", "4", "7", "1"});
        numberSub.put("9", new String[]{"5", "4", "7", "1", "*"});
        numberSub.put("+", new String[]{"-", "*", "*", "*", "*"});
        numberSub.put("-", new String[]{"*", "*", "*", "*", "*"});
    }

    public static int generate(int MatchstickNum, int type) {
        int count = MatchstickNum * 2;  //make sure the while will end
        while ((count--) > 0 && MatchstickNum > 0) {
            //首先生成一个1-min(5,matchstickNum)以内的随机数
            int num = (int) (Math.random() * Math.min(5, MatchstickNum)); //get the change times
            int change = (int) (Math.random() * equation.length()); //get the changed index
            if (equation.charAt(change) == '=') continue;
            if (type == 1)   //add
            {
                String str = numberAdd.get(equation.substring(change, change + 1))[num];
                if (str != "*") {
                    if (str.length() != 1) {
                        int n = (int) Math.random() * str.length();
                        str = str.substring(n, n + 1);
                    }
                    equation = equation.substring(0, change) + str + equation.substring(change + 1);
                    MatchstickNum -= (num + 1);
                }
            } else    //sub
            {
                String str = numberSub.get(equation.substring(change, change + 1))[num];
                if (str != "*") {
                    if (str.length() != 1) {
                        int n = (int) (Math.random() * str.length());
                        str = str.substring(n, n + 1);
                    }
                    equation = equation.substring(0, change) + str + equation.substring(change + 1);
                    MatchstickNum -= (num + 1);
                }
            }
        }
        if (MatchstickNum != 0) return -1;
        else return 0;
    }

    public static void get(int bit, int type) {
        int big, mid, small;
        big = (int) (Math.random() * Math.pow(10, bit));
        small = (int) (Math.random() * big);
        mid = big - small;
        if (type == 1)   //On the left of the equal sign are two numbers
        {
            if (Math.random() > 0.5) {
                equation = String.valueOf(small) + "+" + String.valueOf(mid) + "=" + String.valueOf(big);   //add
            } else {
                equation = String.valueOf(big) + "-" + String.valueOf(small) + "=" + String.valueOf(mid);   //sub
            }
        } else {
            if (Math.random() > 0.5) {
                int midi = (int) (Math.random() * mid);
                if (Math.random() > 0.5) {
                    equation = String.valueOf(midi) + "+" + String.valueOf(mid - midi) + "+" + String.valueOf(small) + "=" + String.valueOf(big);
                } else {
                    equation = String.valueOf(big) + "-" + String.valueOf(mid - midi) + "-" + String.valueOf(small) + "=" + String.valueOf(midi);
                }
            } else {
                int midi = (int) (Math.random() * big);
                if (Math.random() > 0.5) {
                    equation = String.valueOf(midi) + "+" + String.valueOf(big - midi) + "-" + String.valueOf(small) + "=" + String.valueOf(mid);
                } else {
                    equation = String.valueOf(midi) + "-" + String.valueOf(small) + "+" + String.valueOf(big - midi) + "=" + String.valueOf(mid);
                }
            }
        }
    }

    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);
        int maxBit, eqlNum;
        int gameType;
        int MatchstickNum;
        initialize();
        System.out.println("输入最大数字的位数(如1位数、2位数、3位数)：");
        maxBit = input.nextInt();
        System.out.println("输入提示数(2或3)，表示等号左式数字的个数：");
        eqlNum = input.nextInt();
        System.out.println("输入题目类型编号(1移动、2移除、3添加)：");
        gameType = input.nextInt();
        System.out.println("输入火柴棒根数：");
        MatchstickNum = input.nextInt();

        get(maxBit, eqlNum - 1);
        String save = "" + equation;
        switch (gameType) {
            case 1:
                while (generate(MatchstickNum, 1) != 0 || generate(MatchstickNum, 2) != 0) {
                    get(maxBit, eqlNum - 1);
                    save = "" + equation;
                }
                break;
            case 3:
                while (generate(MatchstickNum, 2) != 0) {
                    get(maxBit, eqlNum - 1);
                    save = "" + equation;
                }
                break;
            case 2:
                while (generate(MatchstickNum, 1) != 0) {
                    generate(MatchstickNum, 1);
                    save = "" + equation;
                }
                break;
            default:
                System.exit(1);
        }

        System.out.println(equation);
        String answer;
        answer = input.nextLine();
        answer = input.nextLine();
        while (answer.equals("") == false) {
            if (answer.equals(save) == true) {
                System.out.println("right");
                break;
            } else {
                System.out.println("wrong");
                answer = input.nextLine();
            }
        }
        if (answer.equals(""))
            System.out.println(save);
    }
}
