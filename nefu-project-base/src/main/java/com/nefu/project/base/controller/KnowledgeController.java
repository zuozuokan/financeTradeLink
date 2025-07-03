package com.nefu.project.base.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.nefu.project.base.service.IKnowledgeService;
import com.nefu.project.base.service.INewKnowledgeService;
import com.nefu.project.common.result.HttpResult;
import com.nefu.project.domain.entity.Knowledge;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
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

    @Autowired
    private INewKnowledgeService iNewKnowledgeService;

    @Operation(summary = "发布知识")
    @PostMapping("/add")
    public HttpResult<String> publish(@RequestBody Knowledge knowledge,
                                      @RequestHeader("token") String token) {
        return service.publish(knowledge, token)
                ? HttpResult.success("发布成功")
                : HttpResult.failed("发布失败");
    }

    @Operation(summary = "列出知识列表")
    @GetMapping("/list")
    public HttpResult<Page<Knowledge>> list(@RequestParam(defaultValue = "1") int page,
                                            @RequestParam(defaultValue = "10") int size,
                                            @RequestParam(required = false) String category,
                                            @RequestParam(required = false) String keyword) {
        return HttpResult.success(service.list(page, size, category, keyword));
    }

    @Operation(summary = "列出单个知识")
    @GetMapping("/find/{uuid}")
    public HttpResult get(@PathVariable String uuid) {
        Knowledge k = service.getByUuid(uuid);
        // 更正为直接返回对象
        return k != null ? HttpResult.success(k) : HttpResult.failed("未找到");
    }

    @Operation(summary = "删除知识")
    @PostMapping("/del/{uuid}")
    public HttpResult<String> delete(@PathVariable String uuid) {
        return service.deleteByUuid(uuid)
                ? HttpResult.success("删除成功")
                : HttpResult.failed("删除失败");
    }

    @Operation(summary = "点赞知识")
    @PostMapping("/like")
    public HttpResult<String> like(@RequestParam String knowledgeUuid) {
        return service.like(knowledgeUuid)
                ? HttpResult.success("点赞成功")
                : HttpResult.failed("点赞失败");
    }
    @Operation(summary = "增加浏览量")
    @PostMapping("/view")
    public HttpResult<String> view(@RequestParam String knowledgeUuid) {
        return service.view(knowledgeUuid)
                ? HttpResult.success("浏览加载成功")
                : HttpResult.failed("浏览加载失败");
    }
    @Operation(summary = "获取自己发布知识的列表")
    @PostMapping("/getKnowledgeByUserUuid")
    public HttpResult<Page<Knowledge>> getKnowledgeByUserUuid(@RequestParam String userUuid) {
        return HttpResult.success(service.getKnowledgeByUserUuid(userUuid));
    }



    @Operation(summary = "更新知识")
    @PostMapping("/update/{uuid}")
    public HttpResult<String> update(@PathVariable String uuid, @RequestBody Knowledge updatedData) {
       if (iNewKnowledgeService.updateKnowledage(uuid,updatedData)){
            return HttpResult.success("更新成功");
        }
        return HttpResult.failed("更新失败");
    }


}
