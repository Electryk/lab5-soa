package soa.web;

import java.util.Map;
import java.util.HashMap;

import org.apache.camel.ProducerTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;


@Controller
public class SearchController {

	@Autowired
	  private ProducerTemplate producerTemplate;

	@RequestMapping("/")
    public String index() {
        return "index";
    }


    @RequestMapping(value="/search")
    @ResponseBody
    public Object search(@RequestParam("q") String q) {
        if (q.matches("^\\S.* max:\\d+$")) {
            Map<String, Object> headers = new HashMap<String, Object>(2);
            int index = 0;
            
            index = q.lastIndexOf(':');
            headers.put("CamelTwitterKeywords", q.substring(0, index-5));
            headers.put("CamelTwitterCount", Integer.parseInt(q.substring(index+1)));
            
            return producerTemplate.requestBodyAndHeader("direct:search", "", headers);
        }
        else {
            return producerTemplate.requestBodyAndHeader("direct:search", "", "CamelTwitterKeywords", q);
        }
    }
}