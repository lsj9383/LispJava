#Lisp-Scheme解释器
<p align="center">
  <img src="https://raw.githubusercontent.com/lsj9383/LispJava/master/icon/title.jpg?raw=true" alt="SICP"/>
</p>
将java作为`元语言`(Metalingustic)，对lisp的`Scheme`的子集作实现。<br>
虽然lisp是一个很老的语言，但实际上，大部分语言的处理器，在其深处都包含了一个小的lisp求值器。

##一、快速使用
##二、表达式
##三、eval-apply基本循环的实现
<p align="center">
  <img src="https://raw.githubusercontent.com/lsj9383/LispJava/master/icon/eval-apply.png?raw=true" alt="SICP"/>
</p>
eval和apply是元语言与新语言之间的接口.即，将新语言的表达式传递给eval，元语言会解析新语言表达式，计算得到结果。<br>
简单说来，lisp的求值模型是：<br>
1).求值一个表达式，若该表达式为复合式，首先求值子表达式，而后将运算符作用到子表达式的值上。-----eval<br>
2).当一个符合过程应用于一集实际参数时，我们在一个新的环境里求值这个过程。-----apply<br>
###1.eval
在eval中，描述了新语言的所有表达式与求值方式。其输入是一个新表达式和表达式所处的环境。注意，新表达式通常是一集字符串。
```java
static Data Eval(Express exp, Environment env){
	switch(exp.Type()){
	case NUMBER:		return new Data(Double.valueOf(exp.GetSubExps().get(0)));
	case VARIABLE:		return env.lookup_variable_value(exp.GetSubExps().get(0));
	case ASSIGNMENT:	EvalAssignment(exp, env);		return null;	/* operation without data */
	case DEFINITION:	EvalDefinition(exp, env);		return null;	/* operation without data */
	case IF:			return EvalIf(exp, env);
	case LAMBDA:		return new Procedure(Lambda.Variables(exp), Lambda.Body(exp), env );
	case APPLICATION:	return Apply(Eval(operator(exp), env), ListOfValues(operands(exp), env));
	default:			return null;
	}
}
```
###2.apply
apply描述了一个过程如何作用。<br>
1).对于基本过程，其实现方式已经由`元语言`在底层实现。<br>
2).对于复合过程，其实现方式是将`<body>`提取出来，并根据输入参数创建新环境，再对这个`<body>`进行求值。`<body>`通常是一组表达式。
```java
static Data Apply(Data procedure, ArrayList<Data> args){
	if (procedure.Type() == DataType.PRIMITIVE){ /*基础过程*/
		return ((Primitive)procedure).Call(args);
	}
	else if (procedure.Type() == DataType.PROCEDURE){
		/* 在新环境下对procedure的body顺序求值 */
		return EvalSequence(((Procedure)procedure).Body(),  
							((Procedure)procedure).Env().extend_environment(
									((Procedure)procedure).Variables(),
									 args) );
	}
	else{
		System.out.println("error : Apply , procedure is not PROCEDURE");
		System.exit(0);
		return null;
	}
}
```
###3.另外
Data, 数据类型，是Scheme中所用到的所有数据类型的总和。是实现解释性语言的类型无关的关键。<br>
##四、基本过程
基本过程`primitive procedure`, 是由`元语言`java底层所提供的计算以及相关数据结构，所有的复合过程最后都会转换为基本过程的执行，对复合过程的求值本质上就是试图提取内部的基本过程。<br>
基本过程的实现方案：<br>
1.数据类型：<br>
本质上都是一种Data，并且都有统一的调用接口。因此是Data的继承，并实现了`基本过程`接口。<br>
2.数据本质:<br>
虽然`基本过程`是一个类，但是本身不允许其存在多个，因为它只提供一种操作。因此采用单例模式。
```java
interface Primitive{
	Data Call(ArrayList<Data> args);
}

class Opera extends Data implements{
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
基本过程清单：<br>
* = : Equ; 谓词，判断操作数是否相等.
* < : Less; 谓词，判断操作数是否升序排列.
* > : Great; 谓词，判断操作数是否降续排列.
* + : Add; 操作，将所有操作数相加.
* - : Sub; 操作，将第一个操作数减去其他的操作数.
* * : Mul; 操作，将所有操作数相乘.
* / : Div; 谓词，将第一个操作数除以其他的操作数.

###五、数据类型