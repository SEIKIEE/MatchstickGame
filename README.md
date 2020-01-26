# MatchstickGame
火柴棒游戏

# 火柴棒游戏

# 1 实验目的和要求

- 火柴棒游戏

  - 1. 用户从命令行输入最大数字的位数（如1位数、2位数、3位数）；
  2. 用户从命令行输入提示数（2或3），表示等号左边数字的个数；
    3. 用户从命令行输入题目类型编号（移动、移除、添加），以及火柴棒根数；
    4. 系统随机自动生成火柴棒游戏，并展示（直接用数字展示）；
    5. 用户输入答案，系统验证是否正确；若正确，则提示正确；若错误，则让用户继续输入；
    6. 若用户直接回车，则显示正确答案。
  
  - 例子：移动一根火柴棒，使等式成立。

    2（最大二位数）

    2（左边两个数）

    1（表示移动）1（表示一根）
    
    答案：6+9=15。

# 2 实验内容和原理

1. 随机生成符合用户输入要求的题目；
2. 对用户输入的答案进行验证。
3. 根据用户需要，给出题目答案

# 3 实验环境

macOS 10.14.5

Intellij IDEA CE

java version "12.0.2"

# 4 实验思路

## 4.1 等式生成

全局变量equation代表生成的题目。

```java
public static String equation = new String("");
```

### 4.1.1 方法说明

等式生成的方法为==public static void get(int bit, int type)==。

bit: 最大数字的位数（如1位数、2位数、3位数）。

type: 提示数（2或3），表示等号左边数字的个数。

生成的等式存放在全局变量equation中。

### 4.1.2 思路说明

根据用户输入的最大数字的位数随机生成一个数字，所有等式中出现的数字都会小于等于这个数字。1位数，可以确定最大数字为9，2位数可以确定最大数字为99，3位数可以确定最大数字为999，以此类推。随机数生成采用Math类中的random方法。

随后，在确定最大数字的位数和等式等号左边的数字的个数后，根据等号左边的数字个数，采用随机数生成器生成一个比最大数字小的数字，将一个整数拆分为两个整数的和。big = mid+small。

```java
int big, mid, small;
big = (int) (Math.random() * Math.pow(10, bit));
small = (int) (Math.random() * big);
mid = big - small;
```

分情况讨论。

如果等号左边是两个数字，随机生成加法或者减法运算。如果把最大的数字放在等号右边，就变成加法运算；如果把最大的数字放在等号左边，就变成减法运算。

```java
 if (Math.random() > 0.5) {
                equation = String.valueOf(small) + "+" + String.valueOf(mid) + "=" + String.valueOf(big);   //add
            } else {
                equation = String.valueOf(big) + "-" + String.valueOf(small) + "=" + String.valueOf(mid);   //sub
            }
```

如果等号左边是三个数字，就需要进行下一步拆分。三个数字中间需要两个运算符分割，可能的组合为

>++
>
>--
>
>+-
>
>-+

++和--可以归为一类，需要将small或者mid进行下一步拆分。选择将mid进一步拆分为两个整数之和，mid = midi + (mid-midi)。随机生成++=或者--=组合。同样的，如果把最大的数字放在等号右边，就变成加法运算；如果把最大的数字放在等号左边，就变成减法运算。

```java
int midi = (int) (Math.random() * mid);
if (Math.random() > 0.5) {
                    equation = String.valueOf(midi) + "+" + String.valueOf(mid - midi) + "+" + String.valueOf(small) + "=" + String.valueOf(big);
                } else {
                    equation = String.valueOf(big) + "-" + String.valueOf(mid - midi) + "-" + String.valueOf(small) + "=" + String.valueOf(midi);
                }
```

+-和-+可以归为一类，将big再一次拆分为两个整数的和，big = midi + (big-midi)。根据A+B=C+D的形式，随机拆分为A+B-C=D或者A-C+B=D两种形式。

```java
int midi = (int) (Math.random() * big);
                if (Math.random() > 0.5) {
                    equation = String.valueOf(midi) + "+" + String.valueOf(big - midi) + "-" + String.valueOf(small) + "=" + String.valueOf(mid);
                } else {
                    equation = String.valueOf(midi) + "-" + String.valueOf(small) + "+" + String.valueOf(big - midi) + "=" + String.valueOf(mid);
                }
```

## 4.2 等式变换

将生成的正确的等式修改为题目，修改全局变量equation，equation存放最终生成的题目。

### 4.2.1 方法说明

等式变换的方法为==public static int generate(int MatchstickNum, int type)==。

MatchstickNum: 需要变动的火柴棒根数。

type: 题目类型编号（1移动、2移除、3添加）。

返回-1说明变换失败，返回0说明变换成果。

### 4.2.2 思路说明

根据数字的八段数码管的表示方法可以得到，数字最少需要的火柴棒为2根，数组最多需要的火柴棒的根数为7根。因此，一个数字最多可以添加5根火柴棒，可以列出每个数字分别添加1根、2根、3根、4根、5根火柴棒后能组成的数字。例如，数字1由两根火柴棒组成，添加1根后可以变为7，添加2根后可以变为4，添加3根后可以未变3，添加4根后可以变为0或9，添加5根后可以变为8；数字0由6根火柴棒组成，添加1根可以变为8，添加2根以及上不能变为任何数字。用Map存放数字之间的变换关系，String数组对应分别添加1到5根火柴棒，如果不能变换为任何数字，则用“*”表示。运算符号➖添加一根火柴棒可变为➕。对应关系为numberAdd。

同样的，数字也可以减少1到5根火柴棒变换为另一个数字。运算符号➕减少一根火柴棒可变为➖。对应关系为numberSub。

```java
		public static Map<String, String[]> numberAdd = new HashMap<String, String[]>();
    public static Map<String, String[]> numberSub = new HashMap<String, String[]>();

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
```

首先随机生成一个num = [0, min(5,matchstickNum)-1]的随机数作为对某一个运算数或者运算符一次变换需要改变的火柴棒的数量。生成一个change = [0,equation.length()-1]的随机数，作为代改变数字字符在字符串中的索引。判断是否为‘=’，是则直接进入下一次循环。

如果需要进行添加，定位到numberAdd.get(equation.substring(change, change + 1))[num]，查看是否为“*”。若否，判断该字符串的长度是否为1。如果字符串的长度不为1，则说明equation[change]添加num根火柴棒可变为多个数字，随机选择一个数字，将equation[change]修改为这个数字。如果字符串的长度为1，说明equation[change]添加num根火柴棒可变为单个数字，直接将equation[change]修改为这个数字。最后将matchstickNum减去num，进入下一个循环，直至matchstickNum为0。当matchstickNum为 0时，matchstickNum次变换已经完成。

如果需要进行移除，将上述步骤中的numberAdd修改为numberSub。

如果需要进行移动，先添加matchstickNum根火柴棒，再移除matchstickNum根火柴棒。

为了循环可以顺利结束，设置计数器为count = MatchstickNum * 2，进行倒计时，保证程序总能顺利退出循环。根据循环结束后是否顺利完成MatchstickNum 次变换，返回-1或者0。

```java
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
```

## 4.3 main

读取用户输入，随机生成符合用户输入要求的题目，对用户输入的答案进行验证，输出正确的答案。

```java
int maxBit, eqlNum;					//最大数字位数和等号左边数字个数
int gameType;								//题目类型编号（移动、移除、添加）
int MatchstickNum;					//火柴棒个数
String save = ""+equation;	//答案
```

用循环，生成满足条件的题目。

```java
get(maxBit, eqlNum - 1);
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
                break;
        }
```

输出题目，用户输入答案，系统验证是否正确；若正确，则提示正确；若错误，则让用户继续输入；若用户直接回车，则显示正确答案。输入要求，等式两端和中间都没有多余的空格。

```java
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
```

# 5 实验结果



# 6 心得

nextInt的时候光标停留在原先那一行，再进行nextline读取的时候，会读入回车。

第一次考虑的时候，选择将修改n根火柴棒理解为多次改变1根火柴棒，发现缺少一个数字添加多根火柴棒后出现的多种情况，变化的情况有限；再加上在游戏模式选择为移动的时候，容易出现在同一个数字上进行移除和增加。写好之后就放弃了。第二次考虑将一个数字的多种变化用map存放起来，将移动分解为增加和删除。比第一次考虑的要简洁一点。
