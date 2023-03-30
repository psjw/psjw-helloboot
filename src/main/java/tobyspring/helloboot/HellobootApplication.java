package tobyspring.helloboot;


import org.apache.catalina.startup.Tomcat;
import org.springframework.boot.web.embedded.jetty.JettyServletWebServerFactory;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.boot.web.server.WebServer;
import org.springframework.boot.web.servlet.ServletContextInitializer;
import org.springframework.boot.web.servlet.server.ServletWebServerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class HellobootApplication {

    public static void main(String[] args) {
        ServletWebServerFactory serverFactory = new TomcatServletWebServerFactory();
//        ServletWebServerFactory serverFactory = new JettyServletWebServerFactory();
        //서블릿 생성 -> ServletContextInitializer 인터페이스구현 -> Functional 함수로 사용가능 익명함수를 lambda 변환가능
        WebServer webServer = serverFactory.getWebServer(servletContext -> {
            HelloController helloController = new HelloController();
            servletContext.addServlet("frontcontoller", new HttpServlet() { //1. 서블릿 등록
                @Override
                protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
                    //인증, 보안, 다국어, 공통 기능
                    if(req.getRequestURI().equals("/hello")  && req.getMethod().equals(HttpMethod.GET.name())){ //매핑
                        //1. 요청부분
                        String name = req.getParameter("name");
                        String ret = helloController.hello(name); //바인딩 : 웹요청과 응답을 특정 타입으로 변환
                        //2. 응답부분
                        resp.setStatus(HttpStatus.OK.value());
                        resp.setHeader(HttpHeaders.CONTENT_TYPE, MediaType.TEXT_PLAIN_VALUE);
                        //body 생성
                        resp.getWriter().print(ret);
                    }else if(req.getRequestURI().equals("/user")){

                    }else{
                        resp.setStatus(HttpStatus.NOT_FOUND.value());
                    }
                }
            }).addMapping("/*"); //2. 요청에 따른 매핑 URL 추가(모둔 요청 -> /*)
        });
        webServer.start(); //톰캣 서블릿 컨테이너 동작
    }

}

