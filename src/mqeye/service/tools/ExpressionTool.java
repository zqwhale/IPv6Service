package mqeye.service.tools;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

public class ExpressionTool {

	
	public static final void test1() throws ScriptException{
		 	 String str = "(a >= 0 && a <= 5)";
	        ScriptEngineManager manager = new ScriptEngineManager();
	        ScriptEngine engine = manager.getEngineByName("js");
	        engine.put("a",4);
	        Object result = engine.eval(str);
	         
	        System.out.println("Result is:" + result.getClass().getName() + "Count out:" + result);
	}
	
	public static final void test2() throws ScriptException{
		 ScriptEngineManager sem=new ScriptEngineManager();
	        ScriptEngine engine=sem.getEngineByExtension("js");
	        double result = (Double)engine.eval("3*3 + 4*4");
	        System.out.println(result);
	}
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
	
		try {
			ExpressionTool.test1();
			ExpressionTool.test2();
		       
		} catch (ScriptException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
		}
		       
		  
		
	}

}
