package ai.yue.library.test.grpc.controller;

import ai.yue.library.data.jdbc.ipo.PageIPO;
import ai.yue.library.data.jdbc.vo.PageVO;
import ai.yue.library.test.grpc.dao.TableExampleTestDAO;
import ai.yue.library.test.grpc.dataobject.TableExampleTestDO;
import ai.yue.library.test.proto.GetTableExampleResponse;
import ai.yue.library.test.proto.OpenTableExampleTestGrpc;
import ai.yue.library.test.proto.PageTableExampleRequest;
import ai.yue.library.test.proto.PageTableExampleResponse;
import ai.yue.library.web.grpc.util.ProtoUtils;
import com.google.protobuf.Empty;
import com.google.protobuf.StringValue;
import io.grpc.stub.StreamObserver;
import lombok.extern.slf4j.Slf4j;
import net.devh.boot.grpc.client.inject.GrpcClient;
import net.devh.boot.grpc.server.service.GrpcService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@Slf4j
@GrpcService
public class OpenTableExampleTestController extends OpenTableExampleTestGrpc.OpenTableExampleTestImplBase {

    @Autowired
    TableExampleTestDAO tableExampleTestDAO;
    @GrpcClient("yue-library-test")
    OpenTableExampleTestGrpc.OpenTableExampleTestBlockingStub openTableExampleTestBlockingStub;

    @Override
    public void getTableExample(StringValue request, StreamObserver<GetTableExampleResponse> responseObserver) {
        String uuid = request.getValue();
        TableExampleTestDO tableExampleTestDO = tableExampleTestDAO.getByUuid(uuid);
        GetTableExampleResponse.Builder builder = ProtoUtils.toBuilder(tableExampleTestDO, GetTableExampleResponse.Builder.class);

        responseObserver.onNext(builder.build());
        responseObserver.onCompleted();
    }

    @Override
    public void pageTableExample(PageTableExampleRequest request, StreamObserver<PageTableExampleResponse> responseObserver) {
        PageIPO pageIPO = ProtoUtils.toPageIPO(request);

        PageVO<TableExampleTestDO> pageVO = tableExampleTestDAO.page(pageIPO);
        List<TableExampleTestDO> data = pageVO.getData();
        PageTableExampleResponse.Builder builder = PageTableExampleResponse.newBuilder().setCount(pageVO.getCount());
        for (TableExampleTestDO tableExampleTestDO : data) {
            GetTableExampleResponse.Builder builder1 = ProtoUtils.toBuilder(tableExampleTestDO, GetTableExampleResponse.Builder.class);
            builder.addTableExample(builder1);
        }

        responseObserver.onNext(builder.build());
        responseObserver.onCompleted();
    }

    @Override
    public void actGrpcClientInterceptor(Empty request, StreamObserver<GetTableExampleResponse> responseObserver) {
        TableExampleTestDO tableExampleTestDO = tableExampleTestDAO.listAll().get(0);
        String uuid = tableExampleTestDO.getUuid();
        GetTableExampleResponse tableExample = openTableExampleTestBlockingStub.getTableExample(StringValue.of(uuid));
        responseObserver.onNext(tableExample);
        responseObserver.onCompleted();
    }

}
