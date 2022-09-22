package ai.yue.library.test.grpc.aspect;

import ai.yue.library.base.util.IdUtils;
import ai.yue.library.web.grpc.util.ProtoUtils;
import com.google.protobuf.MessageOrBuilder;
import io.grpc.*;
import lombok.extern.slf4j.Slf4j;
import net.devh.boot.grpc.server.interceptor.GrpcGlobalServerInterceptor;

import java.net.SocketAddress;

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
        RespInterceptor respInterceptor = new RespInterceptor(call);
        ServerCall.Listener<ReqT> reqListener = next.startCall(respInterceptor, headers);
        return new ReqInterceptor(reqListener, call);
    }

    /**
     * 服务接收请求拦截
     */
    private class ReqInterceptor<ReqT> extends ForwardingServerCallListener.SimpleForwardingServerCallListener<ReqT> {

        ServerCall call;

        protected ReqInterceptor(ServerCall.Listener reqListener, ServerCall call) {
            super(reqListener);
            this.call = call;
        }

        @Override
        public void onMessage(ReqT message) {
            SocketAddress requestIp = call.getAttributes().get(Grpc.TRANSPORT_ATTR_REMOTE_ADDR);
            log.info("requestIp={}", requestIp);
            log.info("requestGrpcMethod={}", call.getMethodDescriptor().getFullMethodName());
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
        public void sendHeaders(Metadata headers) {
            String traceId = IdUtils.getSimpleUUID();
            log.info("响应拦截，写入链路ID：{}", traceId);
            Metadata.Key<String> key = Metadata.Key.of("traceId", Metadata.ASCII_STRING_MARSHALLER);
            headers.put(key, traceId);
            super.sendHeaders(headers);
        }

    }

}
