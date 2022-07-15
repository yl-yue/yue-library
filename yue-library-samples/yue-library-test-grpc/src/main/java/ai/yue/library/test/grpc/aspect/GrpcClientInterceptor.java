package ai.yue.library.test.grpc.aspect;

import io.grpc.*;
import lombok.extern.slf4j.Slf4j;
import net.devh.boot.grpc.client.interceptor.GrpcGlobalClientInterceptor;

/**
 * Grpc客户端拦截器
 * <p>拦截客户端发起请求：传递链路、传递token</p>
 * <p>拦截客户端获得响应：服务降级</p>
 *
 * @author ylyue
 * @since  2022/7/15
 */
@Slf4j
@GrpcGlobalClientInterceptor
public class GrpcClientInterceptor implements ClientInterceptor {

    @Override
    public <ReqT, RespT> ClientCall<ReqT, RespT> interceptCall(MethodDescriptor<ReqT, RespT> method, CallOptions callOptions, Channel next) {
        ClientCall<ReqT, RespT> clientCall = next.newCall(method, callOptions);
        return new ReqInterceptor(clientCall, method);
    }

    private class ReqInterceptor<ReqT, RespT> extends ForwardingClientCall.SimpleForwardingClientCall<ReqT, RespT> {

        MethodDescriptor<ReqT, RespT> method;

        protected ReqInterceptor(ClientCall<ReqT, RespT> delegate, MethodDescriptor<ReqT, RespT> method) {
            super(delegate);
            this.method = method;
        }

        @Override
        public void start(Listener<RespT> responseListener, Metadata headers) {
            log.info("发起Grpc请求：{}", method.getFullMethodName());
            super.start(new RespInterceptor(responseListener), headers);
        }

    }

    private class RespInterceptor<RespT> extends ForwardingClientCallListener.SimpleForwardingClientCallListener<RespT> {

        protected RespInterceptor(ClientCall.Listener<RespT> delegate) {
            super(delegate);
        }

        @Override
        public void onMessage(RespT message) {
            log.info("获得Grpc响应：{}", message);
            super.onMessage(message);
        }

    }

}
