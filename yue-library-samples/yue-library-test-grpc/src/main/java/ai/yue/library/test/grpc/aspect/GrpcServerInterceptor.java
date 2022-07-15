package ai.yue.library.test.grpc.aspect;

import ai.yue.library.web.grpc.util.ProtoUtils;
import com.google.protobuf.MessageOrBuilder;
import io.grpc.*;
import lombok.extern.slf4j.Slf4j;
import net.devh.boot.grpc.server.interceptor.GrpcGlobalServerInterceptor;

/**
 * Grpc服务端拦截器
 * <p>服务接收请求拦截</p>
 * <p>服务完成响应拦截</p>
 *
 * @author ylyue
 * @since  2022/7/15
 */
@Slf4j
@GrpcGlobalServerInterceptor
public class GrpcServerInterceptor implements ServerInterceptor {

    @Override
    public <ReqT, RespT> ServerCall.Listener<ReqT> interceptCall(ServerCall<ReqT, RespT> call, Metadata headers, ServerCallHandler<ReqT, RespT> next) {
        ServerCall.Listener<ReqT> reqListener = next.startCall(call, headers);
        return new ReqInterceptor(reqListener, call.getMethodDescriptor());
    }

    /**
     * 服务接收请求拦截
     */
    private class ReqInterceptor<ReqT> extends ForwardingServerCallListener.SimpleForwardingServerCallListener<ReqT> {

        MethodDescriptor methodDescriptor;

        protected ReqInterceptor(ServerCall.Listener delegate, MethodDescriptor methodDescriptor) {
            super(delegate);
            this.methodDescriptor = methodDescriptor;
        }

        @Override
        public void onMessage(ReqT message) {
//            log.info("requestIp={}", requestIp);
            log.info("requestGrpcMethod={}", methodDescriptor.getFullMethodName());
            if (message instanceof MessageOrBuilder) {
                log.info("requestMessage={}", ProtoUtils.toJsonString((MessageOrBuilder) message));
            }
            super.onMessage(message);
        }

    }

    /**
     * 服务完成响应拦截
     */
    private class RespInterceptor<ReqT, RespT> extends ForwardingServerCall.SimpleForwardingServerCall<ReqT, RespT> {

        public RespInterceptor(ServerCall<ReqT, RespT> delegate) {
            super(delegate);
        }

        @Override
        public void sendMessage(RespT message) {
            log.info("响应拦截，写入链路ID");
            super.sendMessage(message);
        }

    }

}
