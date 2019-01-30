package cn.luowq.wx;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

/**
 * @author Binary Wang(https://github.com/binarywang)
 */
@SpringBootApplication
@ComponentScan(basePackages={"cn.luowq","com.github.binarywang.demo.wx"})
public class WxCpDemoApplication {

  public static void main(String[] args) {
    SpringApplication.run(WxCpDemoApplication.class, args);
  }
}
