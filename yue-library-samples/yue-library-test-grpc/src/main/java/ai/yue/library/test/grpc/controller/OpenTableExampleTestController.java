package ai.yue.library.test.grpc.controller;

import ai.yue.library.data.jdbc.ipo.PageIPO;
import ai.yue.library.data.jdbc.vo.PageVO;
import ai.yue.library.test.grpc.dao.TableExampleTestDAO;
import ai.yue.library.test.grpc.dataobject.TableExampleTestDO;
import ai.yue.library.test.proto.*;
import ai.yue.library.web.grpc.util.ProtoUtils;
import com.google.protobuf.Any;
import com.google.protobuf.StringValue;
import io.grpc.stub.StreamObserver;
import lombok.extern.slf4j.Slf4j;
import net.devh.boot.grpc.server.service.GrpcService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@GrpcService
public class OpenTableExampleTestController extends OpenTableExampleTestGrpc.OpenTableExampleTestImplBase {

    @Autowired
    TableExampleTestDAO tableExampleTestDAO;

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
    public void mapResult2(StringValue request, StreamObserver<MapResult> responseObserver) {
        String uuid = request.getValue();
        TableExampleTestDO tableExampleTestDO = tableExampleTestDAO.getByUuid(uuid);
        GetTableExampleResponse.Builder builder = ProtoUtils.toBuilder(tableExampleTestDO, GetTableExampleResponse.Builder.class);
        MapResult mapResult = MapResult.newBuilder().setCode(200).setMsg("成功").setFlag(true).setData(Any.pack(builder.build())).build();

        responseObserver.onNext(mapResult);
        responseObserver.onCompleted();
    }

    @Override
    public void listResult2(PageTableExampleRequest request, StreamObserver<ListResult> responseObserver) {
        PageIPO pageIPO = ProtoUtils.toPageIPO(request);

        PageVO<TableExampleTestDO> pageVO = tableExampleTestDAO.page(pageIPO);
        List<TableExampleTestDO> data = pageVO.getData();
        List<Any> list = new ArrayList<>();
        for (TableExampleTestDO tableExampleTestDO : data) {
            GetTableExampleResponse.Builder builder1 = ProtoUtils.toBuilder(tableExampleTestDO, GetTableExampleResponse.Builder.class);
            Any pack = Any.pack(builder1.build());
            list.add(pack);
        }

        ListResult listResult = ListResult.newBuilder().setCode(200).setMsg("成功").setFlag(true).setCount(pageVO.getCount()).addAllData(list).build();
        responseObserver.onNext(listResult);
        responseObserver.onCompleted();
    }

}
