package top.lingkang.finalsql.example.sb.mapper;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 * @author lingkang
 * Created by 2022/5/2
 */
public class SimpleInvocationHandler implements InvocationHandler {

    private Object obj;

    public SimpleInvocationHandler(Object obj) {
        this.obj = obj;
    }

    /**
     * 处理所有接口的调用
     * @param proxy 代理对象本身
     * @param method 正在被调用的方法
     * @param args 方法的参数
     * @return
     * @throws Throwable
     */
    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        System.out.println("entering " + method.getName());
        System.out.println(args);
        System.out.println(obj);
        Class clazz= (Class) obj;
        Method[] methods = clazz.getMethods();
        Class<?>[] classes=new Class[args.length];
        int i=0;
        for (Object o:args){
            classes[i]=o.getClass();
            i++;
        }
        Method method1 = clazz.getMethod(method.getName(),classes);
        Annotation[] annotations = method1.getAnnotations();
        System.out.println(annotations);

        System.out.println(method);
        System.out.println("leaving " + method.getName());

        return "ok";
    }
}
