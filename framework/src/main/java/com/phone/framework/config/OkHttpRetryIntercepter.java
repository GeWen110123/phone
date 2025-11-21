package com.phone.framework.config;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.validation.constraints.NotNull;
import java.io.IOException;

@Component
@Scope("prototype")
public class OkHttpRetryIntercepter implements Interceptor{

    private int maxRentry;// 最大重试次数

    public OkHttpRetryIntercepter(int maxRentry){
        this.maxRentry=maxRentry;
    }

    @NotNull
    @Override
    public Response intercept(@NotNull Chain chain) throws IOException {
        /* 递归 4次下发请求，如果仍然失败 则返回 null ,但是 intercept must not return null.
         * 返回 null 会报 IllegalStateException 异常
         * */
        return retry(chain,0);//这个递归真的很🐂
    }
    @SuppressWarnings("finally")
	Response retry(Chain chain,int retryCent){
        Request request = chain.request();
        Response response =  null;
        try{
            response = chain.proceed(request);
        }catch (Exception e){
            if ( maxRentry > retryCent ){
                return retry(chain,retryCent+1);
            }
        }finally {
            return response;
        }
    }
}