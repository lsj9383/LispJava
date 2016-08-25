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
	Data result = null;
	
	switch(exp.Type()){
	case NUMBER:
		return new Data(Double.valueOf(exp.GetSubExps().get(0)));
	case VARIABLE:
		return env.lookup_variable_value(exp.GetSubExps().get(0));
	case LAMBDA:
		return new Procedure( 	Lambda.Variables(exp),
								Lambda.Body(exp),
								env );
	case APPLICATION:
		ArrayList<Data> vals = ListOfValues(operands(exp), env);
		return Apply(Eval(operator(exp), env), vals);
	}
	
	return result;
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