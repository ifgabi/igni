package gg.igni.igniserver;

import java.util.ArrayList;
import java.util.List;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@SpringBootApplication
public class IgniServerApplication {

	public static void main(String[] args) {
		SpringApplication.run(IgniServerApplication.class, args);
	}

}
record Test(int id, String author, String message)
{

}

@Controller
class Testing{

  @GetMapping(value = "/test")
  @ResponseBody
  public List<Test> getTestData()
  {
    List<Test> list = new ArrayList<Test>();
    list.add(new Test(0, "test", "test"));
    list.add(new Test(1, "test2", "test2"));
    return list;
  }
}
