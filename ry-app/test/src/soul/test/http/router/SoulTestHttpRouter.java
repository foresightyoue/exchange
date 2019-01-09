package soul.test.http.router;

import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;
import soul.test.http.dto.RequestDTO;
import soul.test.http.result.ResultBean;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.web.reactive.function.server.RequestPredicates.GET;
import static org.springframework.web.reactive.function.server.RequestPredicates.POST;
import static org.springframework.web.reactive.function.server.RequestPredicates.path;
import static org.springframework.web.reactive.function.server.RequestPredicates.accept;
import static org.springframework.web.reactive.function.server.RouterFunctions.nest;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;
import static org.springframework.web.reactive.function.server.ServerResponse.ok;


@Component
public class SoulTestHttpRouter {
    private static final AtomicInteger ATOMIC_INTEGER = new AtomicInteger(1);

    public RouterFunction<ServerResponse> routes() {
        return nest(path("/test"),
                route(POST("/helloWorld2").and(accept(APPLICATION_JSON)), this::postHandler)
                        .and(route(POST("/helloWorld"), this::getHandler)
//                                .and(route(GET("/rewrite"), this::rewriteHandler))
                                .and(route(GET("/pdm"), this::pdmHttpGet))
                                .and(route(GET("/oms"), this::omsHttpGet))
                                .and(route(GET("/timeout"), this::testRetry))));
    }

    private Mono<ServerResponse> testRetry(ServerRequest req) {
        int i = ATOMIC_INTEGER.incrementAndGet();
        System.out.println("Retry count: " + i);
        ResultBean resultBean = new ResultBean(1, "msg", "this is retry hello world");
        return ok().body(Mono.just(resultBean), ResultBean.class);
    }

    private Mono<ServerResponse> pdmHttpGet(ServerRequest req) {
        ResultBean resultBean = new ResultBean(1, "msg", "this is pdm get hello world");
        return ok().body(Mono.just(resultBean), ResultBean.class);
    }

    private Mono<ServerResponse> omsHttpGet(ServerRequest req) {
        ResultBean resultBean = new ResultBean(1, "msg", "this is oms get hello world");
        return ok().body(Mono.just(resultBean), ResultBean.class);
    }

    private Mono<ServerResponse> postHandler(ServerRequest req) {
        final Mono<String> string = req.bodyToMono(String.class);
        //ResultBean resultBean = new ResultBean(1, "msg", "post hello world");
        return ok().body(string, String.class);
    }

    private Mono<ServerResponse> getHandler(ServerRequest req) {
        final Mono<RequestDTO> requestBeanMono = req.bodyToMono(RequestDTO.class);
        try {
            TimeUnit.SECONDS.sleep(1);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        ResultBean resultBean = new ResultBean(1, "msg", "post hello world 1s");
        return ok().body(Mono.just(resultBean), ResultBean.class);
    }


}
