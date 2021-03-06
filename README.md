﻿# Lisp解释器的模仿实现:LIVA
<p align="center">
  <img src="https://raw.githubusercontent.com/lsj9383/LispJava/master/icon/title.jpg?raw=true" alt="SICP"/>
</p>
将java作为`元语言`(Metalingustic)，模仿LISP的`Scheme方言`，并将该新方言命名为Liva。<br>
虽然lisp是一个很老的语言，但实际上，大部分语言的处理器，在其深处都包含了一个小的lisp求值器。

## 一、快速使用
解释器的使用相当简洁，只需要引用`LispInterpreter package`即可。要运行解释器，使用static method : Interpreter.DriverLoop()。<br>
这是一个是用的例子(也是当前工程的使用demo):
```java
package Client;

import LispInterpreter.*;

public class Project {
	static void Run(){
		Interpreter.DriverLoop();
	}
	
	public static void main(String[] args){
		Run();
		System.out.println("project done!");
	}
}
```
<br>执行后如下图:<br>
<img src="https://raw.githubusercontent.com/lsj9383/LispJava/master/icon/welcome.png?raw=true" alt="SICP"/>
<br>Interpreter提供了两种静态方法，供客户调用:<br>
* DriverLoop();  这个方法是启用Eval-Apply循环，用户可以在控制台输入lisp表达式，执行将得到结果。
* Eval(Express exp, Environment env); 用户可以自己调用Eval方法，执行指定的lisp express。
* final Environment GlobalEnv();  获得解释器的全局环境，便于Eval使用。返回的环境是不可变的。

## 二、树形解析
Lisp语言的语法采用`s-expression`, 是一种结构化数据，更具体一点可以称为`抽象语法树`。<br>
例如:(* 2 (+ 3 4)) 可以表现为如下的`抽象语法树`
<p align="left">
  <img src="https://raw.githubusercontent.com/lsj9383/LispJava/master/icon/se-tree.png?raw=true" alt="SICP"/>
</p>
这样的数据概念简单，不易混淆，解释器也非常容易对其进行解析。<br>
对复合表达式的求值，就是对其中的子表达式进行求值，这样的一个递归过程，将会得到如此的树形结构。

## 三、表达式
### 1.表达式数据
lisp表达式采用S-Expression。用户向解释器输入表达式的过程，本质上是将字符串转换为表达式的过程。这是因为用户向控制台输入的均是表达式。<br>
对字符串的解析，是将字符串进行拆分的过程，拆分的方法为：将字符串划分为多个Symbol，每个Symbol之间以分隔符间隔，分隔符包括`空格`与`换行`。<br>
Symbol包括：'(', ')', 任何数字， 任何符号，以及一对括号中的所有东西即'(*)'.
### 2.表达式类与接口
```java
public class Express {
	private ArrayList<String> subexps = new ArrayList<String>();
	private ExpressType type = ExpressType.NULL;

	public Express(String s){
		/* code, 分割字符串，*/
	}
	public ArrayList<String> GetSubExps(){
		/* code, 获得表达式各个sybmol的分割 */
	}
	
	public ExpressType Type(){
		/* code, 获得表达式的类型 */
	}
	
	@Override
	public String toString(){
		/* code */
	}
	
	/* 以下是类内私有方法 */
	....
}
```
表达式提供4个public方法，其中有一个是构造函数，另一个是打印该表达式的方法，另外两个是获得表达式信息的。
### 3.表达式类型
表达式类型由一个枚举体来进行描述：
```java
enum ExpressType{
	NULL,
	NUMBER,
	STRING,
	VARIABLE,
	QUOTED,
	ASSIGNMENT,
	DEFINITION,
	IF,
	COND,
	OR,
	AND,
	LAMBDA,
	BEGIN,
	APPLICATION}
```
* NULL,  该表达式是初始化类型，不能进行求解。
* NUMBER,		数值表达式，返回数值。
* STRING,		字符串表达式，未实现
* VARIABLE,		变量符号，在环境中寻找变量的约束。
* QUOTED,		引号表达式，将表达式中的类容作为符号保存，未实现。
* ASSIGNMENT,	赋值式，修改指定符号的约束。
* DEFINITION,	定义式，增加指定符号的约束。
* IF,			IF条件表达式，未实现。
* COND,			COND表达式，未实现。
* OR,			OR表达式。
* AND,			AND表达式。
* LAMBDA,		lambda表达式，求解生成过程。
* BEGIN；		begin表达式，未实现。
* APPLICATION;	组合式，需要使用apply进行应用。

## 四、eval-apply基本循环的实现
<p align="center">
  <img src="https://raw.githubusercontent.com/lsj9383/LispJava/master/icon/eval-apply.png?raw=true" alt="SICP"/>
</p>

eval和apply是元语言与新语言之间的接口.即，将新语言的表达式传递给eval，元语言会解析新语言表达式，计算得到结果。
简单说来，lisp的求值模型是：
* 1) 求值一个表达式，若该表达式为复合式，首先求值子表达式，而后将运算符作用到子表达式的值上。-----eval
* 2) 当一个符合过程应用于一集实际参数时，我们在一个新的环境里求值这个过程。-----apply

### 4.1 eval
在eval中，描述了新语言的所有表达式与求值方式。其输入是一个新表达式和表达式所处的环境。注意，新表达式通常是一集字符串。
```java
static Data Eval(Express exp, Environment env){
	switch(exp.Type()){
	case NUMBER:		return new Data(Double.valueOf(exp.GetSubExps().get(0)));
	case VARIABLE:		return env.lookup_variable_value(exp.GetSubExps().get(0));
	case ASSIGNMENT:	EvalAssignment(exp, env);		return null;	/* operation without data */
	case DEFINITION:	EvalDefinition(exp, env);		return null;	/* operation without data */
	case IF:			return EvalIf(exp, env);
	case OR:			return EvalOr(exp, env);
	case AND:			return EvalAnd(exp, env);
	case LAMBDA:		return new Procedure(Lambda.Variables(exp), Lambda.Body(exp), env );
	case APPLICATION:	return Apply(Eval(operator(exp), env), ListOfValues(operands(exp), env));
	default:			return null;
	}
}
```
### 4.2 apply
apply描述了一个过程如何作用。

* 1) 对于基本过程，其实现方式已经由`元语言`在底层实现。
* 2) 对于复合过程，其实现方式是将`<body>`提取出来，并根据输入参数创建新环境，再对这个`<body>`进行求值。`<body>`通常是一组表达式。

```java
public static Data Eval(Express exp, Environment env){
	switch(exp.Type()){
	case NUMBER:		return EvalSelf(exp, env);
	case VARIABLE:		return EvalVariable(exp, env);
	case QUOTED:		return EvalQuoted(exp, env);
	case ASSIGNMENT:	return EvalAssignment(exp, env);	/* operation without data and return null*/
	case DEFINITION:	return EvalDefinition(exp, env);	/* operation without data and return null*/
	case IF:			return EvalIf(exp, env);
	case OR:			return EvalOr(exp, env);
	case AND:			return EvalAnd(exp, env);
	case LAMBDA:		return EvalLambda(exp, env);
	case BEGIN:			return EvalBegin(exp, env);
	case APPLICATION:	return Apply(Eval(operator(exp), env), ListOfValues(operands(exp), env));
	default:			return null;
	}
}
```
### 4.3 另外
Data, 数据类型，是Scheme中所用到的所有数据类型的总和。是实现解释性语言的类型无关的关键。

## 五、基本过程

基本过程`primitive procedure`, 是由`元语言`java底层所提供的计算以及相关数据结构，所有的复合过程最后都会转换为基本过程的执行，对复合过程的求值本质上就是试图提取内部的基本过程。

基本过程的实现方案：
* 1.数据类型：本质上都是一种Data，并且都有统一的调用接口。因此是Data的继承，并实现了`基本过程`接口。
* 2.数据本质: 虽然`基本过程`是一个类，但是本身不允许其存在多个，因为它只提供一种操作。因此采用单例模式。

```java
interface Primitive{
	Data Call(ArrayList<Data> args);
}

class Opera extends Data implements Primitive{
	Opera obj = null;
	
	private Opera{}
	public static Opera Single(){
		if(obj==null){
			obj = new Opera;
		}
		return obj;
	}
	
	@Override
	public Data Call(ArrayList<Data> args){
		//code and extract answer to ans.
		return new Data(ans);
	}
}
```

基本过程清单：
*   =   : Equ;		谓词，判断操作数是否相等.
*   <   : Less;		谓词，判断操作数是否升序排列.
*   >   : Great;	谓词，判断操作数是否降续排列.
* null? : isNull;	谓词，判断操作数是否为null对象.
*   +   : Add;		操作，将所有操作数相加.
*   -   : Sub;		操作，将第一个操作数减去其他的操作数.
*   *   : Mul;		操作，将所有操作数相乘.
*   /   : Div;		操作，将第一个操作数除以其他的操作数.
*  car  : Car;		操作，将输入的序对提取第一个数据对象
*  cdr  : Cdr;		操作，将输入的序对提取第二个数据对象
* set-car!:SetCar;	操作，将输入的序对的第一个数据对象修改约束.
* set-cdr!:SetCar;	操作，将输入的序对的第二个数据对象修改约束.
*  cons : Cons;		数值，将输入的两个参数组成一个序对.
*  list : List;		数值，将输入的数据组成序列.
* LoadImage		: load-image	数值，读取指定路径的图像.
* SaveImage		: save-image	数值，将图像保存到指定路径.
* DisplayImage	: display-image	数值，另开线程显示图像.

### 图像基本过程
liva有自己的图像实现框架，有自己特定的图像保存格式。

* 图像基本过程的使用
    * 加载图像(load-image (quoted file-path))
    * 保存图像(save-image image (quoted file-path))
    * 显示图像(display-image image)	
* 图像数据格式
* 文件基本过程

## 六、数据类型
### 6.1 lisp所有数据的种类
* NULL,      无数据
* NUMBER,    数值类书籍(暂时均用double)
* BOOLEAN,   true or false
* CONS,      序对数据
* STRING,    字符串数据，暂未实现
* QUOTED,    引号数据(符号数据)，未实现
* PRIMITIVE, 基本过程
* PROCEDURE, 复合过程
<br>
需要注意的是，过程也是一种数据。所有的数据类型用一个枚举体表述
```java
enum DataType{
	NULL,
	NUMBER,
	STRING,
	QUOTED,
	BOOLEAN,
	CONS,
	PRIMITIVE,
	PROCEDURE
}
```
### 6.2 抽象数据类
这是所有数据类的父类，它其中的type数据标志了数据具体类型，并且这个数据类型将在构造体中进行设置。
```java
/* 抽象数据类 */
abstract public class Data {
	protected DataType type = DataType.NULL;
	
	public Data(){}
	public DataType Type(){return type;}
	public Object GetContent(){return "PRIMITIVE";};
}
```

## 七、初始全局环境

通过Interpreter的静态初始化，来进行加载。由于一个工程里面，最多只能有一个Interpreter类，因此该初始化使用的是静态初始化。
```java
private static Frame InitialFrame = null;
private static Environment global_env = null;
private static Map<String, Data> PrimitiveMap = new TreeMap<>();

static{
	PrimitiveMap.put("eq?"	, IsEq.Single());
	PrimitiveMap.put("="	, Equ.Single());
	PrimitiveMap.put("<"	, Less.Single());
	PrimitiveMap.put(">"	, Great.Single());
	PrimitiveMap.put("null?", isNull.Single());
	PrimitiveMap.put("pair?", IsPair.Single());
	PrimitiveMap.put("+"	, Add.Single());
	PrimitiveMap.put("-"	, Sub.Single());
	PrimitiveMap.put("*"	, Mul.Single());
	PrimitiveMap.put("/"	, Div.Single());
	PrimitiveMap.put("car"	, Car.Single());
	PrimitiveMap.put("cdr"	, Cdr.Single());
	PrimitiveMap.put("set-car!"	, SetCar.Single());
	PrimitiveMap.put("set-cdr!"	, SetCdr.Single());
	PrimitiveMap.put("remainder", Remainder.Single());
	PrimitiveMap.put("int"		, Integer.Single());
	PrimitiveMap.put("load-image"	, LoadImage.Single());
	PrimitiveMap.put("display-image", DisplayImage.Single());
	PrimitiveMap.put("save-image"	, SaveImage.Single());
	PrimitiveMap.put("cons"			, Cons.Single());
	PrimitiveMap.put("list"			, List.Single());
	PrimitiveMap.put("true"			, new BooleanData(true));
	PrimitiveMap.put("false"		, new BooleanData(false));
	PrimitiveMap.put("nil"			, NullData.Single());
	InitialFrame = new Frame(PrimitiveMap);
	global_env = new Environment(InitialFrame, null);		//初始化全局环境
}
```
