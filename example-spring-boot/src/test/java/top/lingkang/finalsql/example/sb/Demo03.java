package top.lingkang.finalsql.example.sb;

import org.springframework.cglib.proxy.Enhancer;
import org.springframework.cglib.proxy.MethodInterceptor;
import org.springframework.cglib.proxy.MethodProxy;
import top.lingkang.finalsql.example.sb.mapper.MyMapper;
import top.lingkang.finalsql.example.sb.mapper.SimpleInvocationHandler;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * @author lingkang
 * Created by 2022/5/2
 */
public class Demo03 {
    public static void main(String[] args) throws Exception {
        SimpleInvocationHandler invocationHandler = new SimpleInvocationHandler(MyMapper.class);

        MyMapper newProxyInstance = (MyMapper) Proxy.newProxyInstance(
                MyMapper.class.getClassLoader(),
                new Class[] { MyMapper.class },
                invocationHandler);

    }


    // 生成代理对象
    private static <T> T getProxy(Class<T> cls) {
        Enhancer enhancer = new Enhancer();
        enhancer.setSuperclass(cls);
        enhancer.setCallback(new SimpleInterceptor());
        return (T) enhancer.create();
    }


    static class SimpleInterceptor implements MethodInterceptor {
        @Override
        public Object intercept(Object o, Method method, Object[] objects, MethodProxy methodProxy) throws Throwable {
            System.out.println("entering " + method.getName());
            Object result = methodProxy.invokeSuper(o, objects);
            System.out.println("leaving " + method.getName());
            return result;
        }
    }
}
