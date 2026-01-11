package com.lbytech.lbytech_backend_new.es;

import com.lbytech.lbytech_backend_new.pojo.vo.BaseResponse;
import com.lbytech.lbytech_backend_new.util.ResultUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Elasticsearch前端控制器
 */
@RestController
@RequestMapping("/es/notebook")
public class NotebookEsController {

    @Autowired
    private INotebookEsService notebookEsService;

    /**
     * 模糊查询笔记
     *
     * @param keyword 关键词
     * @param page    页码
     * @param size    每页数量
     * @return 分页查询结果
     */
    @GetMapping("/getByKeywordContaining")
    public BaseResponse<List<NotebookForEsVO>> getByKeywordContaining(@RequestParam String keyword,
                                                                      @RequestParam(defaultValue = "1") int page,
                                                                      @RequestParam(defaultValue = "10") int size) {
        List<NotebookForEsVO> notebookForEsVOS = notebookEsService.getByKeywordContaining(keyword, page, size);
        return ResultUtil.success(notebookForEsVOS);
    }
}