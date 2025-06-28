package com.nefu.project.base.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.nefu.project.base.service.IKnowledgeService;
import com.nefu.project.common.result.HttpResult;
import com.nefu.project.domain.entity.Knowledge;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


@Tag(name="知识模块")
@RestController
@RequestMapping("/api/knowledge")
@Slf4j
public class KnowledgeController {

    @Autowired
    private IKnowledgeService service;

    @PostMapping
    public HttpResult<String> publish(@RequestBody Knowledge knowledge) {
        return service.publish(knowledge)
                ? HttpResult.success("发布成功")
                : HttpResult.failed("发布失败");
    }

    @GetMapping("/list")
    public HttpResult<Page<Knowledge>> list(@RequestParam(defaultValue = "1") int page,
                                            @RequestParam(defaultValue = "10") int size,
                                            @RequestParam(required = false) String category,
                                            @RequestParam(required = false) String keyword) {
        return HttpResult.success(service.list(page, size, category, keyword));
    }

    @GetMapping("/{uuid}")
    public HttpResult<String> get(@PathVariable String uuid) {
        Knowledge k = service.getByUuid(uuid);
        return k != null ? HttpResult.success(k.toString()) : HttpResult.failed("未找到");
    }

    @PostMapping("/{uuid}")
    public HttpResult<String> delete(@PathVariable String uuid) {
        return service.deleteByUuid(uuid)
                ? HttpResult.success("删除成功")
                : HttpResult.failed("删除失败");
    }

    @PostMapping("/like/{uuid}")
    public HttpResult<String> like(@PathVariable String uuid) {
        return service.like(uuid)
                ? HttpResult.success("点赞成功")
                : HttpResult.failed("点赞失败");
    }
}
