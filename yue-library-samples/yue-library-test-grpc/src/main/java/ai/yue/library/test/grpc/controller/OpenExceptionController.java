package ai.yue.library.test.grpc.controller;

import ai.yue.library.base.exception.*;
import ai.yue.library.test.proto.OpenExceptionTestGrpc;
import cn.hutool.core.exceptions.ValidateException;
import com.google.protobuf.Empty;
import io.grpc.stub.StreamObserver;
import lombok.extern.slf4j.Slf4j;
import net.devh.boot.grpc.client.inject.GrpcClient;
import net.devh.boot.grpc.server.service.GrpcService;

@Slf4j
@GrpcService
public class OpenExceptionController extends OpenExceptionTestGrpc.OpenExceptionTestImplBase {

    @GrpcClient("yue-library-test")
    OpenExceptionTestGrpc.OpenExceptionTestBlockingStub openExceptionTestBlockingStub;

    @Override
    public void exception(Empty request, StreamObserver<Empty> responseObserver) {
        log.info("11111111111111111");
        int a[] = new int[2];
        System.out.println("Access element three :" + a[3]);
    }

    @Override
    public void resultException(Empty request, StreamObserver<Empty> responseObserver) {
        log.info("22222222222222222");
        throw new ResultException("113234234234");
    }

    @Override
    public void loginException(Empty request, StreamObserver<Empty> responseObserver) {
        throw new LoginException("用户未登录");
    }

    @Override
    public void attackException(Empty request, StreamObserver<Empty> responseObserver) {
        throw new AttackException("AttackException");
    }

    @Override
    public void forbiddenException(Empty request, StreamObserver<Empty> responseObserver) {
        throw new ForbiddenException("forbiddenException");
    }

    @Override
    public void apiVersionDeprecatedException(Empty request, StreamObserver<Empty> responseObserver) {
        throw new ApiVersionDeprecatedException("apiVersionDeprecatedException");
    }

    @Override
    public void paramVoidException(Empty request, StreamObserver<Empty> responseObserver) {
        throw new ParamVoidException();
    }

    @Override
    public void paramException(Empty request, StreamObserver<Empty> responseObserver) {
        throw new ParamException("paramException");
    }

    @Override
    public void validateException(Empty request, StreamObserver<Empty> responseObserver) {
        throw new ValidateException("validateException");
    }

    @Override
    public void actClientFallbackException(Empty request, StreamObserver<Empty> responseObserver) {
        Empty mapResult = openExceptionTestBlockingStub.loginException(request);
        responseObserver.onNext(mapResult);
        responseObserver.onCompleted();
    }

}
