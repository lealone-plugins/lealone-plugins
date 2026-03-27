/*
 * Copyright Lealone Database Group.
 * Licensed under the Server Side Public License, v 1.
 * Initial Developer: zhh
 */
package com.lealone.plugins.spring;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CrudController {

    private static final ThreadLocal<CrudService> crudServiceThreadLocal = ThreadLocal
            .withInitial(() -> new CrudService());

    private static CrudService getCrudService() {
        return crudServiceThreadLocal.get();
    }

    // 打开url: http://localhost:8080/insert?name=zhh&age=18
    @GetMapping("/insert")
    public String insert(@RequestParam(value = "name", defaultValue = "zhh") String name,
            @RequestParam("age") int age) {
        return getCrudService().insert(name, age);
    }

    // 打开url: http://localhost:8080/find?name=zhh
    @GetMapping("/find")
    public String find(@RequestParam("name") String name) {
        return getCrudService().find(name);
    }
}
