package com.nefu.project.base.controller;

import com.nefu.project.base.service.IDiscussService;
import com.nefu.project.domain.entity.Discuss;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.nefu.project.common.result.HttpResult;

import java.util.List;

@Tag(name="评论模块")
@RestController
@RequestMapping("/api/discuss")
public class DiscussController {
    @Autowired
    private IDiscussService iDiscussService;

    @PostMapping
    public HttpResult<String> comment(@RequestBody Discuss discuss) {
        return iDiscussService.comment(discuss) ? HttpResult.success("评论成功") : HttpResult.failed("评论失败");
    }

    @GetMapping("/{knowledgeUuid}")
    public HttpResult<List<Discuss>> list(@PathVariable String knowledgeUuid) {
        return HttpResult.success(iDiscussService.listByKnowledge(knowledgeUuid));
    }

    @PostMapping("/like/{uuid}")
    public HttpResult<String> like(@PathVariable String uuid) {
        return iDiscussService.like(uuid) ? HttpResult.success("点赞成功") : HttpResult.failed("点赞失败");
    }

    @DeleteMapping("/{uuid}")
    public HttpResult<String> delete(@PathVariable String uuid) {
        return iDiscussService.delete(uuid) ? HttpResult.success("删除成功") : HttpResult.failed("删除失败");
    }
}
