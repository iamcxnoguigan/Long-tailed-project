package com.l2l.contextsharing.connectors.value;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Logger;

@RestController//从运行时ａｐｐ主动发送变量来接受　为了方便
@RequestMapping
public class pVarController {
    private static Logger log = Logger.getLogger("log");
    @Autowired
    processVar processVar;
    @RequestMapping(value = "/putvar", method = RequestMethod.POST)
    public String say(@RequestBody HashMap<String, Object> param) {
        if(param.containsKey("loc")){
            processVar.setLoc((Integer) param.get("loc"));
        }
        if(param.containsKey("time")){
            processVar.setTime((Integer) param.get("time"));
        }
        if(param.containsKey("num")){
            processVar.setNum((Integer) param.get("num"));
        }
        return "get var";

    }
}
